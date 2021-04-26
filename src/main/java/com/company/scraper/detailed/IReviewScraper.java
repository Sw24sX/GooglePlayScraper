package com.company.scraper.detailed;

import com.company.scraper.App;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

public interface IReviewScraper {
    List<Review> getComments(App app) throws IOException, URISyntaxException;
}
