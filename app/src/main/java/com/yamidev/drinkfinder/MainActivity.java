package com.yamidev.drinkfinder;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvDrinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvDrinks = findViewById(R.id.rvDrinks);
        rvDrinks.setLayoutManager(new LinearLayoutManager(this));

        List<Drink> data = new ArrayList<>();
        data.add(new Drink("Mojito", "Cocktail"));
        data.add(new Drink("Piña Colada", "Tropical"));
        data.add(new Drink("Margarita", "Clásico"));
        data.add(new Drink("Daiquiri", "Rum"));
        data.add(new Drink("Caipirinha", "Brasil"));

        rvDrinks.setAdapter(new DrinkAdapter(data, new OnDrinkClick() {
            @Override
            public void onClick(Drink d) {
                Toast.makeText(MainActivity.this, d.name + " seleccionado", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    // ====== Modelo básico ======
    static class Drink {
        final String name;
        final String category;

        Drink(String name, String category) {
            this.name = name;
            this.category = category;
        }
    }

    interface OnDrinkClick {
        void onClick(Drink d);
    }

    // ====== Adapter básico (inner class) ======
    static class DrinkAdapter extends RecyclerView.Adapter<DrinkViewHolder> {

        private final List<Drink> items;
        private final OnDrinkClick listener;

        DrinkAdapter(List<Drink> items, OnDrinkClick listener) {
            this.items = items;
            this.listener = listener;
        }

        @Override
        public DrinkViewHolder onCreateViewHolder(android.view.ViewGroup parent, int viewType) {
            android.view.View view = android.view.LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_drink_card, parent, false);
            return new DrinkViewHolder(view);
        }

        @Override
        public void onBindViewHolder(DrinkViewHolder holder, int position) {
            Drink d = items.get(position);
            holder.tvName.setText(d.name);
            holder.tvCategory.setText(d.category);
            holder.itemView.setOnClickListener(v -> listener.onClick(d));
            holder.img.setImageResource(R.mipmap.ic_launcher); // usa el ícono del proyecto
        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }

    // ====== ViewHolder (inner class) ======
    static class DrinkViewHolder extends RecyclerView.ViewHolder {
        android.widget.ImageView img;
        android.widget.TextView tvName, tvCategory;
        MaterialCardView card;

        DrinkViewHolder(android.view.View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgDrink);
            tvName = itemView.findViewById(R.id.tvDrinkName);
            tvCategory = itemView.findViewById(R.id.tvDrinkCategory);
            card = (MaterialCardView) itemView;
        }
    }
}