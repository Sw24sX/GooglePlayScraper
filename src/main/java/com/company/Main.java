package com.company;

import com.company.scraper.StoreScraper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.URISyntaxException;
import java.io.IOException;

public class Main {

    // using Apache HttpClient
    public static void main(String[] args) throws IOException, URISyntaxException {
        var targetUrl = "https://play.google.com/store/search?q=dix&c=apps";
        var searchScraper = new GooglePlaySearchScraper("https://play.google.com/store/search");
        var scraper = new StoreScraper(searchScraper);
        var apps = scraper.search("discord");
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
