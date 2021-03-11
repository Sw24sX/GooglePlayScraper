package com.company.scraper.search;

import com.company.scraper.App;

import java.util.HashMap;
import java.util.Map;

public class SearchAppInfo extends App {

    public final Map<String, String> otherParameters;

    public SearchAppInfo(String image, String name, String id) {
        super(id, name, image);
        this.otherParameters = new HashMap<>();
    }
}
