package com.company;

import com.company.googleplay.GooglePlayDetailInfoScraper;
import com.company.googleplay.GooglePlaySearchScraper;
import com.company.scraper.StoreScraper;
import com.company.scraper.detailed.Comment;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

enum Sort {MostRelevant, Newest, Rating}

class BuildRequestReviews {
    // [[[ "classCode", "[xz,xz,[xz,sort,[count,xz,\"tokenPage\"],xz,[xz,xz]],[\"appId"\, xz]]", xz, "generic"]]]
    // Sort 1 - most relevant, 2 - newest, 3 - rating
    final static String pattern = "[[[\"UsvDTd\",\"[null,null,[2,%d,[%d,null,%s],null,[]],[\\\"%s\\\",7]]\",null,\"generic\"]]]";

    public static String build(Integer sort, Integer count, String token, String appId) {
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
        var sort = 1;
        var count = 100;

        var request = new HttpPost(path);
        request.addHeader("content-type", "application/x-www-form-urlencoded");
        var params = new StringEntity(BuildRequestReviews.build(sort, count, appId));
        request.setEntity(params);
        return request;
    }
}

public class Main {
    // using Apache HttpClient
    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        var appId = "com.discord";
        var request = BuildUrlRequestReviews.build(appId);
        var response = httpClient.execute(request);
        var entity = response.getEntity();
        var result = EntityUtils.toString(entity);
        var cleared = clearRequest(result);
        var reviews = cleared.get(0);
        var token = ((JSONArray)cleared.get(cleared.length() - 1)).getString(cleared.length() - 1);
    }

    public static List<Comment> ParseReview(JSONArray array) {
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
        //      2 - (date answer)
        // 8 -
        // 9 -
        // 10 review created version
        // 11 -
        // 12 -
        return null;
    }

    public static JSONArray clearRequest(String request) {
        final String regex = "\\)]}'\\n\\n([\\s\\S]+)";
        var cleared = new JSONArray(request.substring(6));
        return new JSONArray(((JSONArray)cleared.get(0)).getString(2));
    }


    public static void Test() throws IOException, URISyntaxException {
        var targetUrl = "https://play.google.com/store/search?q=dix&c=apps";
        var searchScraper = new GooglePlaySearchScraper("https://play.google.com/store/search");
        var scraper = new StoreScraper(searchScraper);
        var apps = scraper.search("discord");
        var app = apps.get(0);
        var detailScraper = new GooglePlayDetailInfoScraper("https://play.google.com/store/apps/details");
    }

    public static String GetResponse(String query) throws URISyntaxException, IOException {
        URIBuilder builder = new URIBuilder("https://play.google.com/store/search/");
        builder.setParameter("q", query)
                .setParameter("c", "apps");

        var get = new HttpGet(builder.build());
        CloseableHttpClient httpClient = HttpClients.createDefault();
        var response = httpClient.execute(get);
        HttpEntity entity = response.getEntity();
        return EntityUtils.toString(entity);
    }
}
