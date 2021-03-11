package com.company.scraper.search;

import com.company.scraper.search.App;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public abstract class StoreSearchScraper {
    private final String searchUrl;
    private final CloseableHttpClient httpClient;

    public StoreSearchScraper(String searchUrl) {
        this.searchUrl = searchUrl;
        httpClient = HttpClients.createDefault();
    }

    public final List<App> search(String query) throws URISyntaxException, IOException {
        var builder = new URIBuilder(buildSearchUrl(searchUrl, query));
        setQueryParameters(builder, query);

        var response = httpClient.execute(new HttpGet(builder.build()));
        var entity = response.getEntity();
        return parseSearchRequest(EntityUtils.toString(entity));
    }

    public  String buildSearchUrl(String defaultSearchUrl, String query) {
        return defaultSearchUrl;
    }


    public abstract void setQueryParameters(URIBuilder builder, String query);
    public abstract List<App> parseSearchRequest(String responseHTML);
}
