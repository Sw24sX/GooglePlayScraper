package com.company.scraper.detailed;

import com.company.scraper.App;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

public abstract class ReviewsScraper {

    protected final String appDetailBaseUrl;
    private final CloseableHttpClient httpClient;


    public ReviewsScraper(String appDetailBaseUrl) {
        this.appDetailBaseUrl = appDetailBaseUrl;
        httpClient = HttpClients.createDefault();
    }

    public final List<Review> getComments(App app) throws URISyntaxException, IOException {
        var request = new HttpPost(buildCommentUrl(app));
        setQueryCommentsParameters(request, app);

        var response = httpClient.execute(request);
        var entity = response.getEntity();
        return parseDetailCommentsRequest(EntityUtils.toString(entity));
    }

    public String buildCommentUrl(App searchAppInfo) {
        return appDetailBaseUrl;
    }

    public abstract void setQueryCommentsParameters(HttpPost builder, App searchAppInfo) throws UnsupportedEncodingException;
    public abstract List<Review> parseDetailCommentsRequest(String responseHTML);
}
