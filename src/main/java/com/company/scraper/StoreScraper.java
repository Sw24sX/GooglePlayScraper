package com.company.scraper;

import com.company.scraper.comments.StoreCommentScraper;
import com.company.scraper.search.App;
import com.company.scraper.search.StoreSearchScraper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public class StoreScraper {

    private final StoreSearchScraper searchScraper;
    //private final StoreCommentScraper commentScraper;

    public StoreScraper(StoreSearchScraper searchScraper) {
        this.searchScraper = searchScraper;
    }

    public List<App> search (String query) throws IOException, URISyntaxException {
        return searchScraper.search(query);
    }

}
