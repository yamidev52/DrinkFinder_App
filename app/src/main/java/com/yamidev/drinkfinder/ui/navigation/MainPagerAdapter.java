package com.yamidev.drinkfinder.ui.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.yamidev.drinkfinder.ui.favorites.FavoritesFragment;
import com.yamidev.drinkfinder.ui.home.HomeFragment;
import com.yamidev.drinkfinder.ui.search.SearchFragment;

public class MainPagerAdapter extends FragmentStateAdapter {

    public static final int PAGE_SEARCH = 0;
    public static final int PAGE_HOME = 1;
    public static final int PAGE_FAVORITES = 2;

    private static final int PAGE_COUNT = 3;

    public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case PAGE_HOME:
                return new HomeFragment();
            case PAGE_FAVORITES:
                return new FavoritesFragment();
            case PAGE_SEARCH:
            default:
                return new SearchFragment();
        }
    }

    @Override
    public int getItemCount() {
        return PAGE_COUNT;
    }
}
