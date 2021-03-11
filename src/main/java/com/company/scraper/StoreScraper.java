package com.company.scraper;

import com.company.scraper.search.SearchAppInfo;
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

    public List<SearchAppInfo> search (String query) throws IOException, URISyntaxException {
        return searchScraper.search(query);
    }

}
