package com.company.googleplay;

import com.company.scraper.search.SearchAppInfo;
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
    public List<SearchAppInfo> parseSearchRequest(String responseHTML) {
        var result = new ArrayList<SearchAppInfo>();
        var doc = Jsoup.parse(responseHTML);
        for (Element el : doc.body().getElementsByClass("Vpfmgd")){
            result.add(parseApp(el));
        }

        return result;
    }

    private SearchAppInfo parseApp(Element element) {
        var app = new SearchAppInfo(getImageSrc(element), getName(element), getId(element)); //todo
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

    private String getId (Element element) {
        //todo
        var url = element.getElementsByClass("JC71ub").first().attr("href");
        return url.substring(23);
    }

    private String getDeveloper (Element element) {
        return element.getElementsByClass("KoLSrc").first().text();
    }

    private Integer getScore (Element element) {
        return element.getElementsByClass("vQHuPe bUWb7c").size();
    }
}
