package com.company.googleplay;

import com.company.scraper.App;
import com.company.scraper.detailed.Comment;
import com.company.scraper.detailed.FullAppInfo;
import com.company.scraper.detailed.StoreDetailedScraper;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

public class GooglePlayDetailInfoScraper extends StoreDetailedScraper {
    public GooglePlayDetailInfoScraper(String appDetailBaseUrl) {
        super(appDetailBaseUrl);
    }

    @Override
    public void setQueryDetailedInfoParameters(URIBuilder builder, App app) {
        builder.setParameter("id", app.id);
    }

    @Override
    public FullAppInfo parseDetailInfoRequest(String responseHTML, App app) {
        return new FullAppInfo(app);
    }

    @Override
    public void setQueryCommentsParameters(URIBuilder builder, FullAppInfo searchAppInfo) {

    }

    @Override
    public List<Comment> parseDetailCommentsRequest(String responseHTML) {
        return null;
    }
}
