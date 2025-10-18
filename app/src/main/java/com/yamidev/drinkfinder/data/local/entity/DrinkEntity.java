package com.yamidev.drinkfinder.data.local.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;

@Entity(tableName = "favorite_drinks")
public class DrinkEntity {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id_drink")
    private final String id;

    @ColumnInfo(name = "drink_name")
    private final String name;

    @ColumnInfo(name = "drink_category")
    private final String category;

    @ColumnInfo(name = "drink_alcoholic")
    private final String alcoholic;

    @ColumnInfo(name = "drink_glass")
    private final String glass;

    @ColumnInfo(name = "drink_instructions")
    private final String instructions;

    @ColumnInfo(name = "drink_thumbnail")
    private final String thumbnail;

    @ColumnInfo(name = "drink_ingredients")
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

    @NonNull
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getAlcoholic() { return alcoholic; }
    public String getGlass() { return glass; }
    public String getInstructions() { return instructions; }
    public String getThumbnail() { return thumbnail; }
    public List<String> getIngredients() { return ingredients; }
}