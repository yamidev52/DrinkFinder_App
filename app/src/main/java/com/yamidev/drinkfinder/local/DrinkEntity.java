package com.yamidev.drinkfinder.local;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "favorite_drinks")
public class DrinkEntity {
    @PrimaryKey
    @NonNull
    private final String id;
    private final String name;
    private final String category;
    private final String alcoholic;
    private final String glass;
    private final String instructions;
    private final String thumbnail;
    private final List<String> ingredients;

    public DrinkEntity(@NonNull String id, String name, String category, String alcoholic, String glass, String instructions, String thumbnail, List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.alcoholic = alcoholic;
        this.glass = glass;
        this.instructions = instructions;
        this.thumbnail = thumbnail;
        this.ingredients = ingredients;
    }

    // Getters
    @NonNull public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getAlcoholic() { return alcoholic; }
    public String getGlass() { return glass; }
    public String getInstructions() { return instructions; }
    public String getThumbnail() { return thumbnail; }
    public List<String> getIngredients() { return ingredients; }
}
