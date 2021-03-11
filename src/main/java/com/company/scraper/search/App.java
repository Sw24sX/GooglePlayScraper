package com.company.scraper.search;

import java.util.HashMap;
import java.util.Map;

public class App {

    public final String id;
    public final String imageSrc;
    public final String name;
    public final Map<String, String> otherParameters;

    public App(String image, String name, String id) {
        this.id = id;
        this.imageSrc = image;
        this.name = name;
        this.otherParameters = new HashMap<>();
    }
}
