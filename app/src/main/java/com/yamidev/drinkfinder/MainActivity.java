package com.yamidev.drinkfinder;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.yamidev.drinkfinder.drink.DrinkRepository;
import com.yamidev.drinkfinder.drink.DrinkAdapter;


import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrinkAdapter adapter;
    private DrinkRepository repo;



    @SuppressLint({"MissingInflatedId", "ResourceType"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView rv = findViewById(R.id.rvDrinks);
        adapter = new DrinkAdapter();
        rv.setAdapter(adapter);


        repo = new DrinkRepository(getApplicationContext());

        loadByName("margarita");

    }

    private void loadByName(String query) {
        repo.searchByName(query, new DrinkRepository.Result<List<Drink>>() {
            @Override public void onSuccess(List<Drink> data) {
                adapter.setItems(data);
            }
            @Override public void onError(Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadByCategory(String category) {
        repo.filterByCategory(category, new DrinkRepository.Result<List<Drink>>() {
            @Override public void onSuccess(List<Drink> data) {adapter.setItems(data);}
            @Override public void onError(Throwable t) {
                Toast.makeText(MainActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
