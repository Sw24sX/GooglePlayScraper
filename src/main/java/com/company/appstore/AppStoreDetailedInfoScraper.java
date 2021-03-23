package com.company.appstore;

import com.company.scraper.App;
import com.company.scraper.detailed.FullAppInfo;
import com.company.scraper.detailed.StoreDetailedScraper;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.print.Doc;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class AppStoreDetailedInfoScraper extends StoreDetailedScraper {
    public AppStoreDetailedInfoScraper(String appDetailBaseUrl) {
        super(appDetailBaseUrl);
    }

    public AppStoreDetailedInfoScraper() {
        super("https://apps.apple.com/ru/app/");
    }

    public String buildDetailedInfoUrl(App app) {
        return appDetailBaseUrl + app.id;
    }

    @Override
    public void setQueryDetailedInfoParameters(URIBuilder builder, App app) {
    }

    @Override
    public FullAppInfo parseDetailInfoRequest(String responseHTML, App app) throws ParseException {
        var document = Jsoup.parse(responseHTML);
        var description = getDescription(document);
        var score = getScore(document);
        var images = getImages(document);
        var developer = getDeveloper(document);
        var result = new FullAppInfo(app, developer, score, description, images);
        return result;
    }

    private String getDeveloper(Document doc) {
        return doc.getElementsByClass("product-header__identity app-header__identity")
                .first()
                .children()
                .first()
                .text();
    }

    private String getDescription(Document doc) {
        return doc.getElementsByClass("section__description").first().text();
    }

    private Double getScore(Document doc) throws ParseException {
        var scoreStr = doc.getElementsByClass("we-customer-ratings__averages__display").first().text();
        return Double.parseDouble(scoreStr);
    }

    private List<String> getImages(Document doc) {
        var result = new ArrayList<String>();
        var screenshots = doc.getElementsByClass("l-row l-row--peek we-screenshot-viewer__screenshots-list").first();
        for(var element : screenshots.children()) {
            var src = element.getElementsByClass("we-artwork__source").first().attr("srcset");
            result.add(src);
        }
        return result;
    }
}
