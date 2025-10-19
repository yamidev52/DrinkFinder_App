package com.yamidev.drinkfinder.drink;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import com.yamidev.drinkfinder.Drink;
import com.yamidev.drinkfinder.api.ApiDrink;
import com.yamidev.drinkfinder.api.CocktailApi;
import com.yamidev.drinkfinder.api.RetrofitClient;
import com.yamidev.drinkfinder.local.AppDatabase;
import com.yamidev.drinkfinder.local.DrinkDao;
import com.yamidev.drinkfinder.local.DrinkEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DrinkRepository {

    public interface Result<T> {
        void onSuccess(T data);
        void onError(Throwable t);
    }

    private final CocktailApi api;
    private final DrinkDao drinkDao;
    private final ExecutorService executor;

    public DrinkRepository(Context context) {
        this.api = RetrofitClient.getApi();
        this.drinkDao = AppDatabase.getInstance(context).drinkDao();
        this.executor = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Drink>> getFavoriteDrinks() {
        LiveData<List<DrinkEntity>> entityLiveData = drinkDao.getAllFavorites();
        return Transformations.map(entityLiveData, DrinkMapper::entityListToDomainList);
    }

    public LiveData<Boolean> isFavorite(String drinkId) {
        LiveData<DrinkEntity> entityLiveData = drinkDao.getFavoriteById(drinkId);
        return Transformations.map(entityLiveData, entity -> entity != null);
    }

    public void saveFavoriteDrink(Drink drink) {
        executor.execute(() -> {
            DrinkEntity entity = DrinkMapper.domainToEntity(drink);
            drinkDao.insertFavorite(entity);
        });
    }

    public void deleteFavoriteDrink(String drinkId) {
        executor.execute(() -> drinkDao.deleteFavoriteById(drinkId));
    }

    public void searchByName(String name, Result<List<Drink>> cb) {
        api.searchByName(name).enqueue(new Callback<DrinkResponse>() {
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

    public void listCategories(Result<List<String>> cb) {
        api.listCategories("list").enqueue(new Callback<DrinkResponse>() {
            @Override public void onResponse(Call<DrinkResponse> call, Response<DrinkResponse> response) {
                if (!response.isSuccessful() || response.body() == null || response.body().drinks == null) {
                    cb.onSuccess(new ArrayList<>());
                    return;
                }
                List<String> cats = new ArrayList<>();
                for (ApiDrink d : response.body().drinks) {
                    if (d != null && d.strCategory != null) cats.add(d.strCategory);
                }
                cb.onSuccess(cats);
            }
            @Override public void onFailure(Call<DrinkResponse> call, Throwable t) { cb.onError(t); }
        });
    }

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