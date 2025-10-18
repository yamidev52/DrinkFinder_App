package com.yamidev.drinkfinder.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yamidev.drinkfinder.Drink;
import com.yamidev.drinkfinder.R;
import com.yamidev.drinkfinder.drink.DrinkAdapter;
import com.yamidev.drinkfinder.drink.DrinkRepository;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends AppCompatActivity {

    private DrinkAdapter adapter;
    private DrinkRepository repo;

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> alcoholicAdapter;

    private String currentQuery = "";
    private String selectedCategory = "Todas";
    private String selectedAlcoholic = "Todos";

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private String[] tabTitles = new String[]{"Bebidas", "Buscar", "Favoritos"};

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pendingSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);


        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(tabTitles[position]);
            }
        }).attach();

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView rv = findViewById(R.id.rvDrinks);
        adapter = new DrinkAdapter();
        rv.setAdapter(adapter);

        repo = new DrinkRepository();

        // Spinners
        android.widget.Spinner spnCategory = findViewById(R.id.spnCategory);
        android.widget.Spinner spnAlcoholic = findViewById(R.id.spnAlcoholic);

        categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        categoryAdapter.add("Todas");
        spnCategory.setAdapter(categoryAdapter);

        alcoholicAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("Todos", "Alcoholic", "Non alcoholic", "Optional alcohol"));
        spnAlcoholic.setAdapter(alcoholicAdapter);

        spnCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedCategory = (String) parent.getItemAtPosition(position);
                adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        spnAlcoholic.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                selectedAlcoholic = (String) parent.getItemAtPosition(position);
                adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        TextInputLayout tilQuery = findViewById(R.id.tilQuery);
        TextInputEditText etQuery = findViewById(R.id.etQuery);

        tilQuery.setEndIconOnClickListener(v -> {
            currentQuery = safeText(etQuery);
            triggerRemoteSearch(currentQuery);
        });

        etQuery.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                currentQuery = safeText(etQuery);
                triggerRemoteSearch(currentQuery);
                return true;
            }
            return false;
        });

        etQuery.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s == null ? "" : s.toString();
                if (pendingSearch != null) handler.removeCallbacks(pendingSearch);
                pendingSearch = () -> {
                    if (currentQuery.trim().length() >= 2) {
                        triggerRemoteSearch(currentQuery.trim());
                    } else {
                        adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
                    }
                };
                handler.postDelayed(pendingSearch, 400);
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });


        repo.listCategories(new DrinkRepository.Result<List<String>>() {
            @Override public void onSuccess(List<String> cats) {
                if (cats != null) categoryAdapter.addAll(cats);
                categoryAdapter.notifyDataSetChanged();
            }
            @Override public void onError(Throwable t) {
                Toast.makeText(HomeFragment.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        triggerRemoteSearch(randomDrink());
    }

    private void triggerRemoteSearch(String query) {
        if (query.isEmpty()) {
            adapter.setItems(new ArrayList<>());
            return;
        }
        repo.searchByName(query, new DrinkRepository.Result<List<Drink>>() {
            @Override public void onSuccess(List<Drink> data) {
                adapter.setItems(data);
                adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
            }
            @Override public void onError(Throwable t) {
                Toast.makeText(HomeFragment.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String safeText(TextInputEditText et) {
        CharSequence cs = et.getText();
        return cs == null ? "" : cs.toString().trim();
    }

    private String randomDrink(){
        String[] drinks = {
                "margarita",
                "ron",
                "beer",
                "cocktail",
                "mojito",
                "gin",
                "vodka",
                "rum",
                "tequila",
                "whiskey",
                "coffee"
        };
        String random = String.valueOf((int) (Math.random() * drinks.length));
        return drinks[Integer.parseInt(random)];

    }
}