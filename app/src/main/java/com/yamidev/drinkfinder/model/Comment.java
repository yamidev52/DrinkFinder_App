package com.yamidev.drinkfinder.model;

// Este es el modelo de comentarios @Yamil

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Comment {
    private final String id;
    private final String author;
    private final String text;
    private final long timestamp;
    private final List<String> imageUrls;

    public Comment(String author, String text, List<String> imageUrls) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
        this.imageUrls = imageUrls != null ? imageUrls : new ArrayList<>();
    }

    public Comment(String author, String text) {
        this(author, text, null);
    }

    public String getId() { return id; }
    public String getAuthor() { return author; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
    public List<String> getImageUrls() { return imageUrls; }
}