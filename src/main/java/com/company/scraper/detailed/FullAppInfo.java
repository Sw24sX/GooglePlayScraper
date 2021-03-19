package com.company.scraper.detailed;

import com.company.scraper.App;

import java.util.ArrayList;
import java.util.List;

public class FullAppInfo extends App {

    public final String developer;
    public final Double score;
    public final ArrayList<String> images;
    public final String description;

    public FullAppInfo(App app, String developer, Double score, String description) {
        super(app.id, app.name, app.imageSrc);
        this.developer = developer;
        this.score = score;
        this.images = new ArrayList<>();
        this.description = description;
    }

    public FullAppInfo(App app) {
        super(app.id, app.name, app.imageSrc);
        this.developer = null;
        this.score = null;
        this.images = new ArrayList<>();
        this.description = null;
    }
}
