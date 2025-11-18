package com.yamidev.drinkfinder.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.Ignore;

@Entity(
        tableName = "comments",
        indices = {@Index("drinkId")}
)
public class CommentEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @NonNull
    @ColumnInfo(name = "drinkId")
    public String drinkId;

    @NonNull
    @ColumnInfo(name = "text")
    public String text;

    @ColumnInfo(name = "username")
    public String username;

    @ColumnInfo(name = "createdAt")
    public long createdAt;

    @ColumnInfo(name = "imageUri")
    public String imageUri;

    // Constructor vacío para Room
    public CommentEntity() { }

    @Ignore
    public CommentEntity(@NonNull String drinkId,
                         @NonNull String text,
                         String username,
                         long createdAt,
                         String imageUri) {
        this.drinkId = drinkId;
        this.text = text;
        this.username = username;
        this.createdAt = createdAt;
        this.imageUri = imageUri;
    }

    // Helpers para mapear Comment <-> CommentEntity
    public static CommentEntity fromDomain(Comment comment) {
        CommentEntity e = new CommentEntity(
                comment.getDrinkId(),
                comment.getText(),
                comment.getUsername(),
                comment.getCreatedAt(),
                comment.getImageUri()
        );
        e.id = comment.getId();
        return e;
    }

    public Comment toDomain() {
        return new Comment(
                id,
                drinkId,
                text,
                username,
                createdAt,
                imageUri
        );
    }
}