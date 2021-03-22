package com.company;

import com.company.googleplay.GooglePlayDetailInfoScraper;
import com.company.googleplay.GooglePlayReviewsScraper;
import com.company.googleplay.GooglePlaySearchScraper;
import com.company.scraper.App;
import com.company.scraper.StoreScraper;
import com.company.scraper.detailed.Review;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

enum Sort {MostRelevant, Newest, Rating}



public class Main {
    // using Apache HttpClient
    public static void main(String[] args) throws IOException, URISyntaxException, ParseException {
        var apps = new GooglePlaySearchScraper().search("find");
        var detailApp = new GooglePlayDetailInfoScraper().getDetailedInfo(apps.get(0));
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
