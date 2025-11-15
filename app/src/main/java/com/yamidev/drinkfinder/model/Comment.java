package com.yamidev.drinkfinder.model;

// Este es el modelo de comentarios @Yamil

import java.util.UUID;

public class Comment {
    private final String id;
    private final String author;
    private final String text;
    private final long timestamp;

    public Comment(String author, String text) {
        this.id = UUID.randomUUID().toString();
        this.author = author;
        this.text = text;
        this.timestamp = System.currentTimeMillis();
    }

    public String getId() { return id; }
    public String getAuthor() { return author; }
    public String getText() { return text; }
    public long getTimestamp() { return timestamp; }
}