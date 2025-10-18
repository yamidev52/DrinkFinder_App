package com.yamidev.drinkfinder.ui.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yamidev.drinkfinder.Drink;
import com.yamidev.drinkfinder.DrinkAdapter;
import com.yamidev.drinkfinder.DrinkRepository;
import com.yamidev.drinkfinder.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private static final String KEY_CURRENT_QUERY = "search_current_query";
    private static final String KEY_SELECTED_CATEGORY = "search_selected_category";
    private static final String KEY_SELECTED_ALCOHOLIC = "search_selected_alcoholic";

    private DrinkAdapter adapter;
    private DrinkRepository repo;

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> alcoholicAdapter;

    private String currentQuery = "";
    private String selectedCategory = "Todas";
    private String selectedAlcoholic = "Todos";

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pendingSearch;

    private TextInputEditText etQuery;
    private android.widget.Spinner spnCategory;
    private android.widget.Spinner spnAlcoholic;

    private TextWatcher textWatcher;

    public SearchFragment() {
        super(R.layout.fragment_search);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            currentQuery = savedInstanceState.getString(KEY_CURRENT_QUERY, "");
            selectedCategory = savedInstanceState.getString(KEY_SELECTED_CATEGORY, "Todas");
            selectedAlcoholic = savedInstanceState.getString(KEY_SELECTED_ALCOHOLIC, "Todos");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rvDrinks);
        adapter = new DrinkAdapter();
        rv.setAdapter(adapter);

        repo = new DrinkRepository();

        spnCategory = view.findViewById(R.id.spnCategory);
        spnAlcoholic = view.findViewById(R.id.spnAlcoholic);
        TextInputLayout tilQuery = view.findViewById(R.id.tilQuery);
        etQuery = view.findViewById(R.id.etQuery);

        categoryAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        categoryAdapter.add("Todas");
        spnCategory.setAdapter(categoryAdapter);

        alcoholicAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_dropdown_item,
                Arrays.asList("Todos", "Alcoholic", "Non alcoholic", "Optional alcohol"));
        spnAlcoholic.setAdapter(alcoholicAdapter);

        spnCategory.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view1, int position, long id) {
                selectedCategory = (String) parent.getItemAtPosition(position);
                adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
            }

            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

        spnAlcoholic.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view12, int position, long id) {
                selectedAlcoholic = (String) parent.getItemAtPosition(position);
                adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
            }

            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) { }
        });

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

        textWatcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
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

            @Override public void afterTextChanged(Editable s) { }
        };
        etQuery.addTextChangedListener(textWatcher);

        repo.listCategories(new DrinkRepository.Result<List<String>>() {
            @Override
            public void onSuccess(List<String> cats) {
                if (cats != null) categoryAdapter.addAll(cats);
                categoryAdapter.notifyDataSetChanged();
                applyCategorySelection();
            }

            @Override
            public void onError(Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        applyCategorySelection();
        applyAlcoholicSelection();
        if (!currentQuery.isEmpty()) {
            etQuery.removeTextChangedListener(textWatcher);
            etQuery.setText(currentQuery);
            etQuery.setSelection(currentQuery.length());
            etQuery.addTextChangedListener(textWatcher);
            adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
        }

        if (savedInstanceState == null && currentQuery.isEmpty()) {
            triggerRemoteSearch(randomDrink());
        } else if (!currentQuery.isEmpty()) {
            triggerRemoteSearch(currentQuery);
        }
    }

    private void applyCategorySelection() {
        if (categoryAdapter == null || spnCategory == null) return;
        int index = categoryAdapter.getPosition(selectedCategory);
        if (index >= 0) {
            spnCategory.setSelection(index);
        } else if (categoryAdapter.getCount() > 0) {
            spnCategory.setSelection(0);
        }
    }

    private void applyAlcoholicSelection() {
        if (alcoholicAdapter == null || spnAlcoholic == null) return;
        int index = alcoholicAdapter.getPosition(selectedAlcoholic);
        if (index >= 0) {
            spnAlcoholic.setSelection(index);
        } else if (alcoholicAdapter.getCount() > 0) {
            spnAlcoholic.setSelection(0);
        }
    }

    private void triggerRemoteSearch(String query) {
        if (query.isEmpty()) {
            adapter.setItems(new ArrayList<>());
            return;
        }
        repo.searchByName(query, new DrinkRepository.Result<List<Drink>>() {
            @Override
            public void onSuccess(List<Drink> data) {
                adapter.setItems(data);
                adapter.applyFilter(currentQuery, selectedCategory, selectedAlcoholic);
            }

            @Override
            public void onError(Throwable t) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String safeText(TextInputEditText et) {
        CharSequence cs = et.getText();
        return cs == null ? "" : cs.toString().trim();
    }

    private String randomDrink() {
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_CURRENT_QUERY, currentQuery);
        outState.putString(KEY_SELECTED_CATEGORY, selectedCategory);
        outState.putString(KEY_SELECTED_ALCOHOLIC, selectedAlcoholic);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (pendingSearch != null) {
            handler.removeCallbacks(pendingSearch);
            pendingSearch = null;
        }
        if (etQuery != null && textWatcher != null) {
            etQuery.removeTextChangedListener(textWatcher);
        }
        spnCategory = null;
        spnAlcoholic = null;
        etQuery = null;
    }
}
