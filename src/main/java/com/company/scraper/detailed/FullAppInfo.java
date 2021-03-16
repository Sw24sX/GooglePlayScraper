package com.company.scraper.detailed;

import com.company.scraper.App;

import java.util.ArrayList;
import java.util.List;

public class FullAppInfo extends App {

    public final String developer;
    //public final String price;
    public final Double score;
    public final ArrayList<String> images;
    public final String description;
    //public final String currentVersion;
    //public final String size;
    public List<Comment> comments;

    public FullAppInfo(App app, String developer,
                       //String price,
                       Double score,
                       String description
                       //String currentVersion,
                       //String size
    ) {
        super(app.id, app.name, app.imageSrc);
        this.developer = developer;
        //this.price = price;
        this.score = score;
        this.images = new ArrayList<>();
        this.description = description;
        //this.currentVersion = currentVersion;
        //this.size = size;
        this.comments = new ArrayList<>();
    }

    //todo temp
    public FullAppInfo(App app) {
        super(app.id, app.name, app.imageSrc);
        this.developer = null;
        //this.price = null;
        this.score = null;
        this.images = new ArrayList<>();
        this.description = null;
        //this.currentVersion = null;
        //this.size = null;
        this.comments = new ArrayList<>();
    }
}
