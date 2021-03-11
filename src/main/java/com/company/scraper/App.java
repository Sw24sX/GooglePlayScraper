package com.company.scraper;

public abstract class App {
    public final String id;
    public final String name;
    public final String imageSrc;

    public App(String id, String name, String imageSrc) {
        this.id = id;
        this.name = name;
        this.imageSrc = imageSrc;
    }
}
