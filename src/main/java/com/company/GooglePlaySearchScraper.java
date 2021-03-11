package com.company;

import com.company.scraper.search.App;
import com.company.scraper.search.StoreSearchScraper;
import org.apache.http.client.utils.URIBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.util.ArrayList;
import java.util.List;

public class GooglePlaySearchScraper extends StoreSearchScraper {

    public GooglePlaySearchScraper(String searchUrl) {
        super(searchUrl);
    }

    @Override
    public void setQueryParameters(URIBuilder builder, String query) {
        builder.setParameter("q", query)
                .setParameter("c", "apps");
    }

    @Override
    public List<App> parseSearchRequest(String responseHTML) {
        var result = new ArrayList<App>();
        var doc = Jsoup.parse(responseHTML);
        for (Element el : doc.body().getElementsByClass("Vpfmgd")){
            result.add(parseApp(el));
        }

        return result;
    }

    private App parseApp(Element element) {
        var app = new App(getImageSrc(element), getName(element));
        app.otherParameters.put("developer", getDeveloper(element));
        app.otherParameters.put("score", getScore(element).toString());
        return app;
    }

    private String getImageSrc (Element element) {
        return element.getElementsByClass("QNCnCf").first().attr("data-src");
    }

    private String getName (Element element) {
        return element.getElementsByClass("nnK0zc").first().text();
    }

    private String getDeveloper (Element element) {
        return element.getElementsByClass("KoLSrc").first().text();
    }

    private Integer getScore (Element element) {
        return element.getElementsByClass("vQHuPe bUWb7c").size();
    }
}
