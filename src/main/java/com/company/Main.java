package com.company;

import com.company.googleplay.GooglePlayDetailInfoScraper;
import com.company.googleplay.GooglePlaySearchScraper;
import com.company.scraper.StoreScraper;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

enum Sort {MostRelevant, Newest, Rating}


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

class BuildUrl {
    private final String baseUrl;
    private final Parameters parameters;
    private final Headers headers;
    private Integer offset;

    public BuildUrl(String id, String language) throws IOException {
        baseUrl = String.format("https://amp-api.apps.apple.com/v1/catalog/%s/apps/%s/reviews", language, id);
        parameters = new Parameters();
        headers = new Headers(token(id, language));
        offset = 10;
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
        var url = String.format("https://apps.apple.com/%s/app/id%s", lang, id);
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

public class Main {
    // using Apache HttpClient
    public static void main(String[] args) throws IOException, URISyntaxException, ParseException {

        final var httpClient = HttpClients.createDefault();
        final var builder = new BuildUrl("1440976872", "ru");
        var result = new ArrayList<JSONObject>();
        while(true) {
            var get = builder.next();
            var response = EntityUtils.toString(httpClient.execute(get).getEntity());
            var responseJson = new JSONObject(response);
            var reviews = (JSONArray)responseJson.get("data");
            for (var i = 0; i < reviews.length(); i++) {
                var review = reviews.getJSONObject(i);
                result.add(review);
            }

            if (!responseJson.has("next"))
                break;
        }


        // data (array)
        // next (str)


        // attributes (Obj)
        //      date (str)
        //      developerResponse (obj) (необязательно)
        //          modified (str)
        //          id (int)
        //          body (str)
        //      review (str)
        //      rating (int)
        //      isEdited (bool)
        //      userName (str)
        //      title (str)
        // id (str)
        // type (str)
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
