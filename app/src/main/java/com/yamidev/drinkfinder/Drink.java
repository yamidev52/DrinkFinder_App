package com.yamidev.drinkfinder;

public class Drink {
    public final String id;
    public final String title;
    public final String subtitle;
    public final String imageUrl;

    public Drink(String id, String title, String subtitle, String imageUrl) {
        this.id = id;
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
    }
}