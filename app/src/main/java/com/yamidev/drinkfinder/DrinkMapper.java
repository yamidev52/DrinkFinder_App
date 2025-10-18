package com.yamidev.drinkfinder;

import com.yamidev.drinkfinder.domain.model.Drink;

import java.util.ArrayList;
import java.util.List;

public class DrinkMapper {

    public static Drink toDomain(ApiDrink a) {
        if (a == null) return null;

        List<String> ingredients = mergeIngredientsAndMeasures(a);

        return new Drink(
                a.idDrink,
                a.strDrink,
                a.strCategory,
                a.strAlcoholic,
                a.strGlass,
                a.strInstructions,
                a.strDrinkThumb,
                ingredients
        );
    }

    private static List<String> mergeIngredientsAndMeasures(ApiDrink a) {
        // Une strIngredientX con strMeasureX si existen (hasta 15)
        List<String> out = new ArrayList<>();
        String[] ing = {
                a.strIngredient1, a.strIngredient2, a.strIngredient3, a.strIngredient4, a.strIngredient5,
                a.strIngredient6, a.strIngredient7, a.strIngredient8, a.strIngredient9, a.strIngredient10,
                a.strIngredient11, a.strIngredient12, a.strIngredient13, a.strIngredient14, a.strIngredient15
        };
        String[] mea = {
                a.strMeasure1, a.strMeasure2, a.strMeasure3, a.strMeasure4, a.strMeasure5,
                a.strMeasure6, a.strMeasure7, a.strMeasure8, a.strMeasure9, a.strMeasure10,
                a.strMeasure11, a.strMeasure12, a.strMeasure13, a.strMeasure14, a.strMeasure15
        };

        for (int i = 0; i < 15; i++) {
            String ingredient = safe(ing[i]);
            String measure = safe(mea[i]);
            if (!ingredient.isEmpty()) {
                if (!measure.isEmpty()) {
                    out.add(measure + " " + ingredient);
                } else {
                    out.add(ingredient);
                }
            }
        }
        return out;
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}