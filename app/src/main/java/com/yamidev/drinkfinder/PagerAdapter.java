package com.yamidev.drinkfinder;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yamidev.drinkfinder.fragments.FavoriteFragment;

public class PagerAdapter extends FragmentStateAdapter {

    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new FavoriteFragment();
            case 1:
                return new SearchFragment();
            case 2:
                return new FavoritesFragment();
            default:
                return new FavoriteFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3; // número de pestañas
    }
}