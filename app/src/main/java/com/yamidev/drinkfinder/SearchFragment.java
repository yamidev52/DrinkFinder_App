package com.yamidev.drinkfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yamidev.drinkfinder.drink.DrinkAdapter;
import com.yamidev.drinkfinder.drink.DrinkRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchFragment extends Fragment {

    private DrinkAdapter adapter;
    private DrinkRepository repo;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TabLayoutMediator mediator;

    private ArrayAdapter<String> categoryAdapter;
    private ArrayAdapter<String> alcoholicAdapter;

    private String currentQuery = "";
    private String selectedCategory = "Todas";
    private String selectedAlcoholic = "Todos";

    private String[] tabTitles = new String[]{"Bebidas", "Buscar", "Favoritos"};

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable pendingSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);


        MaterialToolbar toolbar = v.findViewById(R.id.toolbar);

        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        RecyclerView rv = v.findViewById(R.id.rvDrinks);
        adapter = new DrinkAdapter();
        rv.setAdapter(adapter);

        repo = new DrinkRepository();

        // Spinners
        android.widget.Spinner spnCategory = v.findViewById(R.id.spnCategory);
        android.widget.Spinner spnAlcoholic = v.findViewById(R.id.spnAlcoholic);

        categoryAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, new ArrayList<>());
        categoryAdapter.add("Todas");
        spnCategory.setAdapter(categoryAdapter);

        alcoholicAdapter = new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item,
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

        TextInputLayout tilQuery = v.findViewById(R.id.tilQuery);
        TextInputEditText etQuery = v.findViewById(R.id.etQuery);

        tilQuery.setEndIconOnClickListener(view -> {
            currentQuery = safeText(etQuery);
            triggerRemoteSearch(currentQuery);
        });

        etQuery.setOnEditorActionListener((view, actionId, event) -> {
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
                Toast.makeText(new HomeActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                Toast.makeText(new HomeActivity(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void shareDrinkText(Context ctx, Drink drink) {
        String body = buildShareText(drink);
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, drink.getName());
        i.putExtra(Intent.EXTRA_TEXT, body);
        ctx.startActivity(Intent.createChooser(i, "Compartir receta…"));
    }

    private String buildShareText(Drink d) {
        // Ajusta los campos a tu modelo
        return "🍹 " + d.getName() + "\n" +
                "Categoría: " + d.getCategory() + "\n" +
                "Ingredientes:\n" + String.join("\n", d.getIngredients()) + "\n\n" +
                "Instrucciones:\n" + d.getInstructions() + "\n\n" +
                "#DrinkFinderApp";
    }

    private Uri saveBitmapToCache(Context ctx, Bitmap bmp, String name) throws IOException {
        File imagesFolder = new File(ctx.getCacheDir(), "images");
        if (!imagesFolder.exists()) imagesFolder.mkdirs();
        File file = new File(imagesFolder, name.replaceAll("\\s+", "_") + ".jpg");
        FileOutputStream stream = new FileOutputStream(file);
        bmp.compress(Bitmap.CompressFormat.JPEG, 95, stream);
        stream.flush();
        stream.close();
        return androidx.core.content.FileProvider.getUriForFile(
                ctx, ctx.getPackageName() + ".fileprovider", file

        );
    }

    private void shareDrinkWithImage(Context ctx, Drink drink) {
        String imageUrl = drink.getThumbnail();
        String body = buildShareText(drink);

        Glide.with(ctx)
                .asBitmap()
                .load(imageUrl)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                        try {
                            Uri uri = saveBitmapToCache(ctx, resource, drink.getName());
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            share.putExtra(Intent.EXTRA_TEXT, body);
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            ctx.startActivity(Intent.createChooser(share, "Compartir receta…"));
                        } catch (IOException e) {
                            // Fallback a texto si falla guardar imagen
                            shareDrinkText(ctx, drink);
                        }
                    }
                    @Override public void onLoadCleared(@androidx.annotation.Nullable Drawable placeholder) {}
                    @Override public void onLoadFailed(@androidx.annotation.Nullable Drawable errorDrawable) {
                        // Fallback a texto si falla carga
                        shareDrinkText(ctx, drink);
                    }
                });
    }
}