package com.yamidev.drinkfinder;

import androidx.annotation.Nullable;

import com.yamidev.drinkfinder.DrinkMapper;
import com.yamidev.drinkfinder.CocktailApi;
import com.yamidev.drinkfinder.RetrofitClient;
import com.yamidev.drinkfinder.ApiDrink;
import com.yamidev.drinkfinder.DrinkResponse;
import com.yamidev.drinkfinder.Drink;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkRepository {

    public interface Result<T> {
        void onSuccess(T data);
        void onError(Throwable t);
    }

    private final CocktailApi api;

    public DrinkRepository() {
        this.api = RetrofitClient.getApi();
    }

    public void searchByName(String name, Result<List<Drink>> cb) {
        api.searchByName(name).enqueue(new Callback<DrinkResponse>() {
            @Override public void onResponse(Call<DrinkResponse> call, Response<DrinkResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    cb.onSuccess(new ArrayList<>()); // vacío si no hay resultados
                    return;
                }
                cb.onSuccess(mapList(response.body().drinks));
            }
            @Override public void onFailure(Call<DrinkResponse> call, Throwable t) { cb.onError(t); }
        });
    }

    public void getById(String id, Result<Drink> cb) {
        api.lookupById(id).enqueue(new Callback<DrinkResponse>() {
            @Override public void onResponse(Call<DrinkResponse> call, Response<DrinkResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().drinks == null || response.body().drinks.isEmpty()) {
                    cb.onSuccess(null);
                    return;
                }
                cb.onSuccess(DrinkMapper.toDomain(response.body().drinks.get(0)));
            }
            @Override public void onFailure(Call<DrinkResponse> call, Throwable t) { cb.onError(t); }
        });
    }

    public void filterByCategory(String category, Result<List<Drink>> cb) {
        api.filterByCategory(category).enqueue(new Callback<DrinkResponse>() {
            @Override public void onResponse(Call<DrinkResponse> call, Response<DrinkResponse> response) {
                if (!response.isSuccessful() || response.body() == null) {
                    cb.onSuccess(new ArrayList<>());
                    return;
                }
                cb.onSuccess(mapList(response.body().drinks));
            }
            @Override public void onFailure(Call<DrinkResponse> call, Throwable t) { cb.onError(t); }
        });
    }

    /** Devuelve los nombres de categorías (si la API trae objetos “strCategory”). */
    public void listCategories(Result<List<String>> cb) {
        api.listCategories("list").enqueue(new Callback<DrinkResponse>() {
            @Override public void onResponse(Call<DrinkResponse> call, Response<DrinkResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().drinks == null) {
                    cb.onSuccess(new ArrayList<>());
                    return;
                }
                List<String> cats = new ArrayList<>();
                for (ApiDrink d : response.body().drinks) {
                    // Algunas respuestas de list.php?c=list devuelven objetos con sólo strCategory
                    if (d != null && d.strCategory != null) cats.add(d.strCategory);
                }
                cb.onSuccess(cats);
            }
            @Override public void onFailure(Call<DrinkResponse> call, Throwable t) { cb.onError(t); }
        });
    }

    // Helpers
    private List<Drink> mapList(@Nullable List<ApiDrink> apiDrinks) {
        List<Drink> out = new ArrayList<>();
        if (apiDrinks == null) return out;
        for (ApiDrink a : apiDrinks) {
            Drink d = DrinkMapper.toDomain(a);
            if (d != null) out.add(d);
        }
        return out;
    }
}