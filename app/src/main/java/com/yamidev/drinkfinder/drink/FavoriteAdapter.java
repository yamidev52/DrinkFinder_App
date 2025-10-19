package com.yamidev.drinkfinder.drink;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.yamidev.drinkfinder.Drink;
import com.yamidev.drinkfinder.R;
import java.util.ArrayList;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteVH> {

    public interface OnItemClick {
        void onClick(Drink drink);
    }

    private final List<Drink> items = new ArrayList<>();
    private OnItemClick listener;

    public void setOnItemClick(OnItemClick l) {
        this.listener = l;
    }

    public void setItems(List<Drink> newItems) {
        items.clear();
        if (newItems != null) {
            items.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavoriteVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_drink_card, parent, false);
        return new FavoriteVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteVH h, int position) {
        Drink d = items.get(position);
        h.tvName.setText(d.getName() != null ? d.getName() : "");
        h.tvCategory.setText(d.getCategory() != null ? d.getCategory() : "");
        Glide.with(h.img.getContext())
                .load(d.getThumbnail())
                .placeholder(R.mipmap.ic_launcher)
                .centerCrop()
                .into(h.img);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(d);
        });

        h.btnShare.setVisibility(View.GONE);
        h.btnFavorite.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class FavoriteVH extends RecyclerView.ViewHolder {
        ImageView img;
        TextView tvName, tvCategory;
        ImageButton btnShare, btnFavorite;

        FavoriteVH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDrink);
            tvName = itemView.findViewById(R.id.tvDrinkName);
            tvCategory = itemView.findViewById(R.id.tvDrinkCategory);
            btnShare = itemView.findViewById(R.id.btnShare);
            btnFavorite = itemView.findViewById(R.id.btnFavoritos);
        }
    }
}