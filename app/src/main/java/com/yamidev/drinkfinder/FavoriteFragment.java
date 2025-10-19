package com.yamidev.drinkfinder;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import com.yamidev.drinkfinder.drink.DrinkAdapter;
import com.yamidev.drinkfinder.drink.DrinkRepository;
import com.yamidev.drinkfinder.drink.FavoriteAdapter;

public class FavoriteFragment extends Fragment {

    private DrinkRepository repo;
    private FavoriteAdapter adapter;

    public FavoriteFragment() {
        super(R.layout.fragment_favorites);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv = view.findViewById(R.id.rv_favorites);
        repo = new DrinkRepository(requireContext());
        adapter = new FavoriteAdapter();
        rv.setAdapter(adapter);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(toolbar);

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.menu_home, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.favoriteFragment) {

                    Toast.makeText(requireContext(), "Ya estás en Favoritos", Toast.LENGTH_SHORT).show();
                    return true;
                }
                if (menuItem.getItemId() == R.id.searchFragment) {

                    Navigation.findNavController(requireView()).navigate(R.id.action_favorites_to_search);
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        adapter.setOnItemClick(drink -> {
            Bundle bundle = new Bundle();
            bundle.putString("drinkId", drink.getId());
            Navigation.findNavController(view).navigate(R.id.action_favorites_to_detail, bundle);
        });


        repo.getFavoriteDrinks().observe(getViewLifecycleOwner(), favoriteDrinks -> {
            adapter.setItems(favoriteDrinks);
        });
    }
}