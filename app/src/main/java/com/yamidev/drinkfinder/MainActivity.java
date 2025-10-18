package com.yamidev.drinkfinder;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.yamidev.drinkfinder.ui.navigation.MainPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String KEY_SELECTED_TAB = "main_selected_tab";

    private int selectedTab = MainPagerAdapter.PAGE_SEARCH;

    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private TabLayoutMediator tabMediator;

    private final ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @Override
        public void onPageSelected(int position) {
            selectedTab = position;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MainPagerAdapter(this));
        viewPager.registerOnPageChangeCallback(pageChangeCallback);
        viewPager.setOffscreenPageLimit(2);

        tabLayout = findViewById(R.id.tabs);
        tabMediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case MainPagerAdapter.PAGE_HOME:
                    tab.setText(R.string.tab_home);
                    tab.setIcon(AppCompatResources.getDrawable(this, R.drawable.hogar));
                    tab.setContentDescription(R.string.tab_home);
                    break;
                case MainPagerAdapter.PAGE_FAVORITES:
                    tab.setText(R.string.tab_favorites);
                    tab.setIcon(AppCompatResources.getDrawable(this, R.drawable.favorito));
                    tab.setContentDescription(R.string.tab_favorites);
                    break;
                case MainPagerAdapter.PAGE_SEARCH:
                default:
                    tab.setText(R.string.tab_search);
                    tab.setIcon(AppCompatResources.getDrawable(this, R.drawable.lupa));
                    tab.setContentDescription(R.string.tab_search);
                    break;
            }
        });
        tabMediator.attach();

        if (savedInstanceState != null) {
            selectedTab = savedInstanceState.getInt(KEY_SELECTED_TAB, MainPagerAdapter.PAGE_SEARCH);
        }

        int itemCount = viewPager.getAdapter() != null ? viewPager.getAdapter().getItemCount() : 0;
        if (selectedTab < 0 || selectedTab >= itemCount) {
            selectedTab = MainPagerAdapter.PAGE_SEARCH;
        }

        viewPager.setCurrentItem(selectedTab, false);
        TabLayout.Tab tab = tabLayout.getTabAt(selectedTab);
        if (tab != null) {
            tab.select();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_SELECTED_TAB, selectedTab);
    }

    @Override
    protected void onDestroy() {
        if (viewPager != null) {
            viewPager.unregisterOnPageChangeCallback(pageChangeCallback);
        }
        if (tabMediator != null) {
            tabMediator.detach();
        }
        super.onDestroy();
    }
}
