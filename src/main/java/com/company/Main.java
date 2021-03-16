package com.company;

import com.company.googleplay.GooglePlayDetailInfoScraper;
import com.company.googleplay.GooglePlaySearchScraper;
import com.company.scraper.StoreScraper;
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

import java.net.URISyntaxException;
import java.io.IOException;
import java.util.ArrayList;

enum Sort {MostRelevant, Newest, Rating}

public class Main {

    // using Apache HttpClient
    public static void main(String[] args) throws IOException, URISyntaxException {
        HttpClient httpClient = HttpClientBuilder.create().build();
        var appId = "com.discord";
        var path = "https://play.google.com/_/PlayStoreUi/data/batchexecute";
        var request = new HttpPost(path);
        request.addHeader("content-type", "application/x-www-form-urlencoded");

        var queryParams = new ArrayList<NameValuePair>();
        queryParams.add(new BasicNameValuePair("hl", "ru"));
        queryParams.add(new BasicNameValuePair("gl", "ru"));
        request.setEntity(new UrlEncodedFormEntity(queryParams, "UTF-8"));

        var count = 199;
        var params = new StringEntity(buildPayloadFormatForFirstPage(appId, Sort.Newest, count, 1));
        request.setEntity(params);
        var response = httpClient.execute(request);
        var entity = response.getEntity();
        var result = EntityUtils.toString(entity);
        var jsonObj = new JSONArray(result);
    }

    public static String buildPayloadFormatForFirstPage(String appId, Sort sort, Integer count, Integer filterScoreWith) {
        return "f.req=%5B%5B%5B%22UsvDTd%22%2C%22%5Bnull%2Cnull%2C%5B2%2C" +
                sort.ordinal() +
                "%2C%5B" +
                count +
                "%2Cnull%2Cnull%5D%2Cnull%2C%5Bnull%2C" +
                "null" + // filterScoreWith
                "%5D%5D%2C%5B%5C%22" +
                appId +
                "%5C%22%2C7%5D%5D%22%2Cnull%2C%22generic%22%5D%5D%5D";
    }

    public static String buildPayloadFormatForPaginatedPage(String appId, Sort sort, Integer count, Integer filterScoreWith, String paginationToken) {
        return "f.req=%5B%5B%5B%22UsvDTd%22%2C%22%5Bnull%2Cnull%2C%5B2%2C" +
                sort.ordinal() +
                "%2C%5B" +
                count +
                "%2Cnull%2C%5C%22" +
                paginationToken +
                "%5C%22%5D%2Cnull%2C%5Bnull%2C" +
                "null" + // filterScoreWith
                "%5D%5D%2C%5B%5C%22" +
                appId +
                "%5C%22%2C7%5D%5D%22%2Cnull%2C%22generic%22%5D%5D%5D";
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
