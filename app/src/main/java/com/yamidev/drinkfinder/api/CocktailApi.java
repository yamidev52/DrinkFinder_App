package com.yamidev.drinkfinder.api;

import com.yamidev.drinkfinder.drink.DrinkResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CocktailApi {

    // Buscar por nombre (ej: "margarita")
    @GET("search.php")
    Call<DrinkResponse> searchByName(@Query("s") String name);

    // Detalle por id (ej: "11007")
    @GET("lookup.php")
    Call<DrinkResponse> lookupById(@Query("i") String idDrink);

    // Filtrar por categoría (devuelve id, nombre y thumb)
    @GET("filter.php")
    Call<DrinkResponse> filterByCategory(@Query("c") String category);

    // Listado de categorías
    @GET("list.php")
    Call<DrinkResponse> listCategories(@Query("c") String listParam); // usar "list"
}