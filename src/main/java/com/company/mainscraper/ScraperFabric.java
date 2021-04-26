package com.company.mainscraper;

import com.company.appstore.AppStoreDetailedInfoScraper;
import com.company.appstore.AppStoreReviewScraper;
import com.company.appstore.AppStoreSearchScraper;
import com.company.googleplay.GooglePlayDetailInfoScraper;
import com.company.googleplay.GooglePlayReviewsScraper;
import com.company.googleplay.GooglePlaySearchScraper;
import com.company.scraper.StoreScraper;

public class ScraperFabric {
    public static StoreScraper GooglePlayScraper() {
        var search = new GooglePlaySearchScraper();
        var detail = new GooglePlayDetailInfoScraper();
        var reviews = new GooglePlayReviewsScraper();

        return new StoreScraper(search, detail, reviews);
    }

    public static StoreScraper AppStoreScraper() {
        var search = new AppStoreSearchScraper();
        var detail = new AppStoreDetailedInfoScraper();
        var reviews = new AppStoreReviewScraper();

        return new StoreScraper(search, detail, reviews);
    }
}
