package com.yamidev.drinkfinder.drink;

import android.widget.Filter;
import android.widget.Filterable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.yamidev.drinkfinder.R;
import com.yamidev.drinkfinder.Drink;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.DrinkVH> implements Filterable {

    public interface OnItemClick { void onClick(Drink drink); }

    private final List<Drink> items = new ArrayList<>();     // visibles
    private final List<Drink> all   = new ArrayList<>();     // copia completa para filtrar

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
        DrinkVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDrink);
            tvName = itemView.findViewById(R.id.tvDrinkName);
            tvCategory = itemView.findViewById(R.id.tvDrinkCategory);
        }
    }
}