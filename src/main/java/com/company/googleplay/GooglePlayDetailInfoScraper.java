package com.company.googleplay;

import com.company.scraper.App;
import com.company.scraper.detailed.Comment;
import com.company.scraper.detailed.FullAppInfo;
import com.company.scraper.detailed.StoreDetailedScraper;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
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
        var document = Jsoup.parse(responseHTML);
        var name = findName(document);
        var developer = document.getElementsByClass("R8zArc").first().text();
        var score = Double.parseDouble(document.getElementsByClass("BHMmbe").first().text());
        var images = findImages(document);
        //todo get full description
        return new FullAppInfo(app);
    }

    private String findName(Document document) {
        return document.getElementsByClass("AHFaub").first()
                .getElementsByTag("span").first().text();
    }

    private List<String> findImages(Document document) {
        var images = new ArrayList<String>();
        for (Element element :document.getElementsByClass("T75of DYfLw")) {
            images.add(element.attr("src"));
        }
        return images;
    }

    @Override
    public void setQueryCommentsParameters(URIBuilder builder, FullAppInfo searchAppInfo) {

    }

    @Override
    public List<Comment> parseDetailCommentsRequest(String responseHTML) {
        return null;
    }
}
