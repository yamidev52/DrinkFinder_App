package com.yamidev.drinkfinder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.lifecycle.Observer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yamidev.drinkfinder.drink.DrinkRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class DetailFragment extends Fragment {


    private ImageView imgDrinkDetail;
    private TextView tvCategoryDetail, tvInstructionsDetail, tvIngredientsDetail;
    private CollapsingToolbarLayout collapsingToolbar;
    private FloatingActionButton fabShare, fabFavorite;
    private boolean isCurrentlyFavorite = false;


    private DrinkRepository repo;
    private Drink currentDrink;

    public DetailFragment() {
        super(R.layout.fragment_detail);
    }

    /*
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        imgDrinkDetail = view.findViewById(R.id.img_drink_detail);
        tvCategoryDetail = view.findViewById(R.id.tv_category_detail);
        tvInstructionsDetail = view.findViewById(R.id.tv_instructions_detail);
        tvIngredientsDetail = view.findViewById(R.id.tv_ingredients_detail);
        collapsingToolbar = view.findViewById(R.id.collapsing_toolbar);
        fabShare = view.findViewById(R.id.fab_share);
        fabFavorite = view.findViewById(R.id.fab_favorite);

        repo = new DrinkRepository(requireContext());

        Toolbar toolbar = view.findViewById(R.id.toolbar_detail);
        AppCompatActivity activity = (AppCompatActivity) requireActivity();
        activity.setSupportActionBar(toolbar);

        NavController navController = NavHostFragment.findNavController(this);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(collapsingToolbar, toolbar, navController, appBarConfiguration);

        if (getArguments() != null) {
            String drinkId = getArguments().getString("drinkId");
            if (drinkId != null && !drinkId.isEmpty()) {
                fetchDrinkDetails(drinkId);
                observeFavoriteStatus(drinkId);
            } else {
                showError("No se recibió un ID de bebida.");
            }
        }

        fabShare.setOnClickListener(v -> {
            if (currentDrink != null) {
                shareDrinkWithImage(requireContext(), currentDrink);
            }
        });

        fabFavorite.setOnClickListener(v -> {
            if (currentDrink != null) {
                if (isCurrentlyFavorite) {
                    repo.deleteFavoriteDrink(currentDrink.getId());
                } else {
                    repo.saveFavoriteDrink(currentDrink);
                }
            }
        });
    }

    private void fetchDrinkDetails(String id) {
        repo.getById(id, new DrinkRepository.Result<Drink>() {
            @Override
            public void onSuccess(Drink drink) {
                if (drink != null && isAdded()) {
                    currentDrink = drink;
                    bindDataToViews(drink);
                } else {
                    showError("No se encontraron detalles para esta bebida.");
                }
            }

            @Override
            public void onError(Throwable t) {
                showError("Error al cargar los detalles: " + t.getMessage());
            }
        });
    }

    private void observeFavoriteStatus(String id) {
        repo.isFavorite(id).observe(getViewLifecycleOwner(), isFavorite -> {
            isCurrentlyFavorite = isFavorite;
            if (isFavorite) {
                fabFavorite.setImageResource(R.drawable.ic_favorite_filled);
            } else {
                fabFavorite.setImageResource(R.drawable.ic_favorite_border);
            }
        });
    }

    private void bindDataToViews(Drink drink) {
        collapsingToolbar.setTitle(drink.getName());
        tvCategoryDetail.setText(drink.getCategory());
        tvInstructionsDetail.setText(drink.getInstructions());


        StringBuilder ingredientsText = new StringBuilder();
        for (String ingredient : drink.getIngredients()) {
            ingredientsText.append("• ").append(ingredient).append("\n");
        }
        tvIngredientsDetail.setText(ingredientsText.toString().trim());

        Glide.with(requireContext())
                .load(drink.getThumbnail())
                .into(imgDrinkDetail);
    }

    private void showError(String message) {
        if (isAdded()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        }
        Log.e("DetailFragment", message);
    }



    private void shareDrinkWithImage(Context ctx, Drink drink) {
        String body = buildShareText(drink);
        Glide.with(ctx)
                .asBitmap()
                .load(drink.getThumbnail())
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        try {
                            Uri uri = saveBitmapToCache(ctx, resource, drink.getName());
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("image/*");
                            share.putExtra(Intent.EXTRA_STREAM, uri);
                            share.putExtra(Intent.EXTRA_TEXT, body);
                            share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            ctx.startActivity(Intent.createChooser(share, "Compartir receta…"));
                        } catch (IOException e) {
                            shareDrinkText(ctx, drink, body);
                        }
                    }

                    @Override public void onLoadCleared(@Nullable Drawable placeholder) {}
                    @Override public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        shareDrinkText(ctx, drink, body);
                    }
                });
    }

    private void shareDrinkText(Context ctx, Drink drink, String body) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_SUBJECT, drink.getName());
        i.putExtra(Intent.EXTRA_TEXT, body);
        ctx.startActivity(Intent.createChooser(i, "Compartir receta…"));
    }

    private String buildShareText(Drink d) {
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
}