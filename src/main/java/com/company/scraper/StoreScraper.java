package com.company.scraper;

import com.company.scraper.detailed.FullAppInfo;
import com.company.scraper.review.IReviewScraper;
import com.company.scraper.review.Review;
import com.company.scraper.detailed.StoreDetailedScraper;
import com.company.scraper.search.SearchAppInfo;
import com.company.scraper.search.StoreSearchScraper;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.List;

public class StoreScraper {
    private final StoreSearchScraper searchScraper;
    private final StoreDetailedScraper detailedScraper;
    private final IReviewScraper reviewScraper;

    public StoreScraper(StoreSearchScraper searchScraper, StoreDetailedScraper detailedScraper,
                        IReviewScraper reviewScraper) {
        this.searchScraper = searchScraper;
        this.detailedScraper = detailedScraper;
        this.reviewScraper = reviewScraper;
    }

    public List<SearchAppInfo> search (String query) throws IOException, URISyntaxException {
        return searchScraper.search(query);
    }

    public FullAppInfo detailed (App app) throws ParseException, IOException, URISyntaxException {
        return detailedScraper.getDetailedInfo(app);
    }

    public List<Review> reviews (App app) throws InterruptedException, IOException, URISyntaxException {
        return reviewScraper.getComments(app);
    }
}
