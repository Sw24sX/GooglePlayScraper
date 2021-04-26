package com.company.googleplay;

import com.company.scraper.App;
import com.company.scraper.detailed.Review;
import com.company.scraper.detailed.ReviewsScraper;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GooglePlayReviewsScraper{
    private String language;
    private String country;
    private List<Review> reviews;

    public GooglePlayReviewsScraper() {
        reviews = new ArrayList<>();
        language = "ru";
        country = "ru";
    }

    public final List<Review> getComments(App app) throws IOException {
        return getComments(app.id);
    }

    public final List<Review> getComments(String appId) throws IOException {
        reviews = new ArrayList<>();
        HttpClient httpClient = HttpClientBuilder.create().build();
        var request = BuildUrlRequestReviews.build(appId);
        var response = httpClient.execute(request);
        var entity = response.getEntity();
        var result = EntityUtils.toString(entity);
        while (true) {
            var cleared = clearRequest(result);
            var reviews = (JSONArray)cleared.get(0);
            ParseReview(reviews);

            // Если страница последняя => токена нет
            if(cleared.length() < 2)
                break;
            var token = ((JSONArray)cleared.get(cleared.length() - 1)).getString(cleared.length() - 1);
            request = BuildUrlRequestReviews.build(appId, token);
            response = httpClient.execute(request);
            entity = response.getEntity();
            result = EntityUtils.toString(entity);
        }
        return reviews;
    }

    public void setLanguage(String language) {
        // todo add validator
        this.language = language;
    }

    public void setCountry(String country) {
        // todo add validator
        this.country = country;
    }

    public void ParseReview(JSONArray array) {
        for (var el :array) {
            var t = el.hashCode();
            var review = (JSONArray)el;
            var reviewId = review.getString(0);
            var user = (JSONArray)review.get(1);
            var name = user.getString(0);
            var userImageSrc = ((JSONArray)((JSONArray)user.get(1)).get(3)).getString(2);
            var score = review.getInt(2);
            var reviewText = review.getString(4);
            var date = new Date(((JSONArray)review.get(5)).getInt(0));  // todo need fix, incorrect date
            var likes = review.getInt(6);
            String reviewCreatedVersion = null;
            if(review.length() > 11) {
                if(review.get(10).hashCode() != 0)
                    reviewCreatedVersion = review.getString(10);
            }

            var comment = new Review(
                    reviewId,
                    name,
                    userImageSrc,
                    reviewText,
                    score,
                    null, //todo need parse date
                    likes,
                    reviewCreatedVersion
            );

            var developerObj = review.get(7);
            if(developerObj.hashCode() != 0){
                var developer = (JSONArray)developerObj;
                var answerText = developer.getString(1);
                var answerDate = new Date(((JSONArray)developer.get(2)).getInt(0));
                comment.addAnswer(answerText, null); //todo need parse date
            }

            reviews.add(comment);
        }
    }

    private JSONArray clearRequest(String request) {
        var cleared = new JSONArray(request.substring(6));
        return new JSONArray(((JSONArray)cleared.get(0)).getString(2));
    }
}

class BuildRequestReviews {
    // [[[ "classCode", "[xz,xz,[xz,sort,[count,xz,\"tokenPage\"],xz,[xz,xz]],[\"appId"\, xz]]", xz, "generic"]]]
    // Sort 1 - most relevant, 2 - newest, 3 - rating
    final static String pattern = "[[[\"UsvDTd\",\"[null,null,[2,%d,[%d,null,%s],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]";

    public static String build(Integer sort, Integer count, String appId, String token) {
        if(token == null)
            token = "null";
        var parameters = String.format(pattern, sort, count, token, appId);
        return "f.req=" + URLEncoder.encode(parameters);
    }

    public static String build(Integer sort, Integer count, String appId) {
        return build(sort, count, null, appId);
    }
}

class BuildUrlRequestReviews {
    final static String path = "https://play.google.com/_/PlayStoreUi/data/batchexecute?hl=ru&gl=ru";

    public static HttpPost build(String appId) throws UnsupportedEncodingException {
        return build(appId, null);
    }

    public static HttpPost build(String appId, String token) throws UnsupportedEncodingException {
        var sort = 1;
        var count = 199;

        var request = new HttpPost(path);
        request.addHeader("content-type", "application/x-www-form-urlencoded");
        var params = new StringEntity(BuildRequestReviews.build(sort, count, appId, token));
        request.setEntity(params);
        return request;
    }
}



// 0 reviewId
// 1 :
//      0 = Name
//      1:
//          0 -
//          1 -
//          2 -
//          3:
//              0 -
//              1 -
//              2 = userImageSrc
// 2: Score
// 3 -
// 4 = review
// 5:
//      0: at  (date.timestamp)
//      1:
// 6 = likes
// 7:
//      0 = Name developer
//      1 = Answer developer
//      2 :
//          0 = date
//          1 -
// 8 -
// 9 -
// 10 review created version
// 11 -
// 12 -