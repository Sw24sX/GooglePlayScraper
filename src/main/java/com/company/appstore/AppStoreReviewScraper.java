package com.company.appstore;

import com.company.scraper.App;
import com.company.scraper.detailed.IReviewScraper;
import com.company.scraper.detailed.Review;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class AppStoreReviewScraper implements IReviewScraper {
    private final String language;

    public AppStoreReviewScraper(String language) {
        this.language = language;
    }

    public AppStoreReviewScraper() {
        this.language = "ru";
    }

    @Override
    public List<Review> getComments(App app) throws IOException, URISyntaxException, InterruptedException {
        final var httpClient = HttpClients.createDefault();
        final var builder = new BuildUrl(app.id, language);
        var result = new ArrayList<Review>();
        while(true) {
            var get = builder.next();
            var response = EntityUtils.toString(httpClient.execute(get).getEntity());
            var responseJson = new JSONObject(response);
            if(!responseJson.has("data")){
                Thread.sleep(1000);
                System.out.println("Все хреново, мы привысили лимит запросов"); //Todo delete this))
                throw new IOException();
            }
            var reviews = (JSONArray)responseJson.get("data");
            for (var i = 0; i < reviews.length(); i++) {
                var review = reviews.getJSONObject(i);
                result.add(parseReview(review));
            }
            Thread.sleep(100);
            if (!responseJson.has("next"))
                break;
        }

        return result;
    }

    private Review parseReview(JSONObject reviewAttr) {
        var attr = reviewAttr.getJSONObject("attributes");
        var dateString = attr.getString("date").substring(0, 19);
        var date = LocalDateTime.parse(dateString);
        var review = attr.getString("review");
        var rating = attr.getInt("rating");
        var isEdited = attr.getBoolean("isEdited");
        var userName = attr.getString("userName");
        var reviewId = reviewAttr.getString("id");
        var type = reviewAttr.getString("type");

        var result = new Review(reviewId, userName, null, review, rating, date, 0, null);

        if(attr.has("developerResponse")){
            var developerResponse = attr.getJSONObject("developerResponse");
            var modifiedDateString = developerResponse.getString("modified").substring(0, 19);
            var modifiedDate = LocalDateTime.parse(modifiedDateString);
            var developerResponseId = developerResponse.getInt("id");
            var textDeveloperResponse = developerResponse.getString("body");
            result.addAnswer(textDeveloperResponse, modifiedDate);
        }

        return result;
    }
}

class BuildUrl {
    private final String baseUrl;
    private final Parameters parameters;
    private final Headers headers;
    private Integer offset;

    public BuildUrl(String id, String language) throws IOException {
        baseUrl = String.format("https://amp-api.apps.apple.com/v1/catalog/%s/apps/%s/reviews", language, id.substring(2));
        parameters = new Parameters();
        headers = new Headers(token(id, language));
        offset = 100;
    }

    public HttpGet next() throws URISyntaxException {
        parameters.changeOffset(offset);
        var builder = new URIBuilder(baseUrl);
        builder.setParameters(parameters.getParameters());
        var result = new HttpGet(builder.build());
        result.setHeaders(headers.getHeaders());
        offset += 10;
        return result;
    }

    private static String token(String id, String lang) throws IOException {
        var httpClient = HttpClients.createDefault();
        var url = String.format("https://apps.apple.com/%s/app/%s", lang, id);
        var entity = httpClient.execute(new HttpGet(url)).getEntity();
        var entityText = EntityUtils.toString(entity);

        var start = "token%22%3A%22";
        var end = "%22";
        var matcher = Pattern.compile(start + "(.+?)" + end).matcher(entityText);
        if(!matcher.find())
            throw new IOException();

        var result = matcher.group();
        return result.substring(start.length(), result.length() - end.length());
    }
}

class Parameters {
    public List<NameValuePair> getParameters() {
        return parameters;
    }

    public void changeOffset(Integer value) {
        parameters.set(1, new BasicNameValuePair("offset", value.toString()));
    }

    private final List<NameValuePair> parameters;

    public Parameters() {
        parameters = Arrays.asList(
                new BasicNameValuePair("l", "ru"),
                new BasicNameValuePair("offset", "10"),
                new BasicNameValuePair("platform", "web"),
                new BasicNameValuePair("additionalPlatforms", "appletv%2Cipad%2Ciphone%2Cmac")
        );
    }
}

class Headers {
    public Header[] getHeaders() {
        return headers;
    }

    private final Header[] headers;

    public Headers(String token) {
        headers = new Header[] {
                new BasicHeader("authority", "amp-api.apps.apple.com"),
                new BasicHeader("authorization", "Bearer " + token),
                new BasicHeader("content-type", "application/x-www-form-urlencoded; charset=UTF-8"),
                new BasicHeader("origin", "https://apps.apple.com"),
                new BasicHeader("sec-fetch-site", "same-site"),
                new BasicHeader("sec-fetch-mode", "cors"),
                new BasicHeader("sec-fetch-dest", "empty"),
                new BasicHeader("referer", "https://apps.apple.com/"),
                new BasicHeader("accept-language", "ru,en;q=0.9")
        };
    }
}
