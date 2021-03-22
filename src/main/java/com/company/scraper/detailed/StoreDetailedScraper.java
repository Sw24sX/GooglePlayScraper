package com.company.scraper.detailed;

import com.company.scraper.App;
import com.company.scraper.search.SearchAppInfo;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

public abstract class StoreDetailedScraper {

    protected final String appDetailBaseUrl;
    private final CloseableHttpClient httpClient;

    public StoreDetailedScraper(String appDetailBaseUrl) {
        this.appDetailBaseUrl = appDetailBaseUrl;
        httpClient = HttpClients.createDefault();
    }

    public final FullAppInfo getDetailedInfo(App app) throws URISyntaxException, IOException, ParseException {
        var builder = new URIBuilder(buildDetailedInfoUrl(app));
        setQueryDetailedInfoParameters(builder, app);

        var response = httpClient.execute(new HttpGet(builder.build()));
        var entity = response.getEntity();
        return parseDetailInfoRequest(EntityUtils.toString(entity), app);
    }

    public String buildDetailedInfoUrl(App app) {
        return appDetailBaseUrl;
    }

    public abstract void setQueryDetailedInfoParameters(URIBuilder builder, App app);
    public abstract FullAppInfo parseDetailInfoRequest(String responseHTML, App app) throws ParseException;
}
