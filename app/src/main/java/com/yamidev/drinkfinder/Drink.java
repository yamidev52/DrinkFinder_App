package com.yamidev.drinkfinder;

import java.util.ArrayList;
import java.util.List;

public class Drink {
    private final String id;
    private final String name;
    private final String category;
    private final String alcoholic;     // "Alcoholic" / "Non alcoholic"
    private final String glass;
    private final String instructions;
    private final String thumbnail;

    // Guardamos ingredientes y medidas ya “emparejados”
    private final List<String> ingredients; // ej. "50ml Vodka", "1/2 Lime", etc.

    public Drink(String id,
                 String name,
                 String category,
                 String alcoholic,
                 String glass,
                 String instructions,
                 String thumbnail,
                 List<String> ingredients) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.alcoholic = alcoholic;
        this.glass = glass;
        this.instructions = instructions;
        this.thumbnail = thumbnail;
        this.ingredients = ingredients != null ? ingredients : new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getAlcoholic() { return alcoholic; }
    public String getGlass() { return glass; }
    public String getInstructions() { return instructions; }
    public String getThumbnail() { return thumbnail; }
    public List<String> getIngredients() { return ingredients; }
}