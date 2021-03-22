package com.company.googleplay;

import com.company.scraper.App;
import com.company.scraper.detailed.FullAppInfo;
import com.company.scraper.detailed.StoreDetailedScraper;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GooglePlayDetailInfoScraper extends StoreDetailedScraper {
    public GooglePlayDetailInfoScraper(String appDetailBaseUrl) {
        super(appDetailBaseUrl);
    }

    public GooglePlayDetailInfoScraper() {
        super("https://play.google.com/store/apps/details");
    }

    @Override
    public void setQueryDetailedInfoParameters(URIBuilder builder, App app) {
        builder.setParameter("id", app.id);
        builder.setParameter("hl", "ru");
        builder.setParameter("gl", "ru");
    }

    @Override
    public FullAppInfo parseDetailInfoRequest(String responseHTML, App app) throws ParseException {
        var document = Jsoup.parse(responseHTML);
        var developer = document.getElementsByClass("R8zArc").first().text();
        var scoreStr = document.getElementsByClass("BHMmbe").first().text();
        var score = (Double)NumberFormat.getNumberInstance().parse(scoreStr);
        var images = findImages(document);
        var description = findDescription(document);
        return new FullAppInfo(app, developer, score, description, images);
    }

    private List<String> findImages(Document document) {
        var images = new ArrayList<String>();
        for (Element element :document.getElementsByClass("T75of DYfLw")) {
            var src = element.attr("src");
            if (src.equals(""))
                src = element.attr("data-src");
            images.add(src);
        }
        //todo need fix
        return images;
    }

    private String findDescription(Document document) {
        return document.getElementsByClass("DWPxHb").first().text();
    }
}
