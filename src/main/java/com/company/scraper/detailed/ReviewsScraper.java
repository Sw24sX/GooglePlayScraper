package com.company.scraper.detailed;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public abstract class ReviewsScraper {

    private final String appDetailBaseUrl;
    private final CloseableHttpClient httpClient;

    public ReviewsScraper(String appDetailBaseUrl) {
        this.appDetailBaseUrl = appDetailBaseUrl;
        httpClient = HttpClients.createDefault();
    }

    public final void getComments(FullAppInfo app) throws URISyntaxException, IOException {
        var builder = new URIBuilder(buildCommentUrl(app));
        setQueryCommentsParameters(builder, app);

        var response = httpClient.execute(new HttpGet(builder.build()));
        var entity = response.getEntity();
        app.comments = parseDetailCommentsRequest(EntityUtils.toString(entity));
    }

    public String buildCommentUrl(FullAppInfo searchAppInfo) {
        return appDetailBaseUrl;
    }

    public abstract void setQueryCommentsParameters(URIBuilder builder, FullAppInfo searchAppInfo);
    public abstract List<Comment> parseDetailCommentsRequest(String responseHTML);
}
