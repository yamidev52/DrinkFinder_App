package com.yamidev.drinkfinder.model;

public class Comment {

    private long id;
    private String drinkId;
    private String text;
    private String username;
    private long createdAt;
    private String imageUri; // opcional

    public Comment(long id,
                   String drinkId,
                   String text,
                   String username,
                   long createdAt,
                   String imageUri) {
        this.id = id;
        this.drinkId = drinkId;
        this.text = text;
        this.username = username;
        this.createdAt = createdAt;
        this.imageUri = imageUri;
    }

    public Comment(String drinkId,
                   String text,
                   String username,
                   long createdAt,
                   String imageUri) {
        this(0, drinkId, text, username, createdAt, imageUri);
    }

    public long getId() { return id; }
    public String getDrinkId() { return drinkId; }
    public String getText() { return text; }
    public String getUsername() { return username; }
    public long getCreatedAt() { return createdAt; }
    public String getImageUri() { return imageUri; }
}