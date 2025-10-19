package com.yamidev.drinkfinder.drink;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.yamidev.drinkfinder.Drink;
import com.yamidev.drinkfinder.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkVH> implements Filterable {

    public interface OnItemClick { void onClick(Drink drink); }

    private final List<Drink> items = new ArrayList<>();
    private final List<Drink> all   = new ArrayList<>();

    private OnItemClick listener;

    // Criterios de filtro
    private String q = "";
    private String category = "Todas";
    private String alcoholic = "Todos"; // Alcoholic, Non alcoholic, Optional alcohol, Todos

    public void setOnItemClick(OnItemClick l) { this.listener = l; }

    public void setItems(List<Drink> newItems) {
        all.clear();
        items.clear();
        if (newItems != null) {
            all.addAll(newItems);
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    /** Actualiza criterios y aplica filtro */
    public void applyFilter(String query, String category, String alcoholic) {
        this.q = query == null ? "" : query.trim();
        this.category = category == null ? "Todas" : category;
        this.alcoholic = alcoholic == null ? "Todos" : alcoholic;
        getFilter().filter(""); // dispara el filtro
    }

    @NonNull @Override
    public DrinkVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drink_card, parent, false);
        return new DrinkVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DrinkVH h, int position) {
        Drink d = items.get(position);
        h.tvName.setText(d.getName() != null ? d.getName() : "");
        h.tvCategory.setText(d.getCategory() != null ? d.getCategory() : "");
        Glide.with(h.img.getContext())
                .load(d.getThumbnail())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(h.img);
        h.itemView.setOnClickListener(v -> { if (listener != null) listener.onClick(d); });
        h.btnShare.setOnClickListener(v -> {
            shareDrinkWithImage(v.getContext(), d);
        });

        h.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(d);
            }
        });

    }

    @Override public int getItemCount() { return items.size(); }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override protected FilterResults performFiltering(CharSequence constraint) {
                List<Drink> filtered = new ArrayList<>();
                String qLower = q.toLowerCase(Locale.ROOT);

                for (Drink d : all) {
                    boolean byName = qLower.isEmpty()
                            || (d.getName() != null && d.getName().toLowerCase(Locale.ROOT).contains(qLower));

                    boolean byCategory = "Todas".equals(category)
                            || (d.getCategory() != null && d.getCategory().equalsIgnoreCase(category));

                    boolean byAlcoholic = "Todos".equals(alcoholic)
                            || (d.getAlcoholic() != null && d.getAlcoholic().equalsIgnoreCase(alcoholic));

                    if (byName && byCategory && byAlcoholic) filtered.add(d);
                }
                FilterResults r = new FilterResults();
                r.values = filtered;
                r.count = filtered.size();
                return r;
            }

            @Override protected void publishResults(CharSequence constraint, FilterResults results) {
                items.clear();
                if (results.values != null) items.addAll((List<Drink>) results.values);
                notifyDataSetChanged();
            }
        };
    }

    static class DrinkVH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvCategory;
        ImageButton btnShare;

        DrinkVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDrink);
            tvName = itemView.findViewById(R.id.tvDrinkName);
            tvCategory = itemView.findViewById(R.id.tvDrinkCategory);
            btnShare = itemView.findViewById(R.id.btnShare);
        }
    }

    private void shareDrinkText(Context ctx, Drink drink) {
        String body = buildShareText(drink);
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, drink.getName());
        intent.putExtra(Intent.EXTRA_TEXT, body);
        ctx.startActivity(Intent.createChooser(intent, "Compartir bebida…"));
    }

    private String buildShareText(Drink d) {
        return "🍹 " + d.getName() + "\n" +
                "Categoría: " + d.getCategory() + "\n" +
                "Tipo: " + d.getAlcoholic() + "\n\n" +
                "Instrucciones: " + d.getInstructions() + "\n\n" +
                "Imagen: " + d.getThumbnail() + "\n" +
                "#DrinkFinderApp";
    }

    private void shareDrinkWithImage(Context ctx, Drink drink) {
        String imageUrl = drink.getThumbnail();
        String body = buildShareText(drink);

        Glide.with(ctx)
                .asBitmap()
                .load(imageUrl)
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
                            ctx.startActivity(Intent.createChooser(share, "Compartir bebida…"));
                        } catch (IOException e) {
                            shareDrinkText(ctx, drink);
                        }
                    }

                    @Override public void onLoadCleared(@Nullable Drawable placeholder) {}
                    @Override public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        shareDrinkText(ctx, drink);
                    }
                });
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