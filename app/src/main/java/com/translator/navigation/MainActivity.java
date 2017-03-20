package com.translator.navigation;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import android.widget.LinearLayout;

import com.translator.R;
import com.translator.navigation.favorites.FavoritesFragment;
import com.translator.navigation.history.HistoryFragment;

public class MainActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private LinearLayout translateLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        translateLayout = (LinearLayout) findViewById(R.id.translate_layout);


        fragmentManager = getSupportFragmentManager();
        fragment = new TranslateFragment();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(R.id.main_container, fragment).commit();

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        setUpToolbar(item.getItemId());

                        switch (item.getItemId()) {
                            case R.id.action_translate:
                                fragment = new TranslateFragment();
                                break;
                            case R.id.action_history:
                                fragment = new HistoryFragment();
                                break;
                            case R.id.action_favorites:
                                fragment = new FavoritesFragment();
                                break;
                            case R.id.action_settings:
                                fragment = new SettingsFragment();
                                break;
                        }
                        final FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.main_container, fragment).commit();
                        return true;
                    }
                });
    }


    private void setUpToolbar(int id) {
        if(toolbar == null)
            return;

        switch (id) {
            case R.id.action_translate:
                toolbar.setTitle(getString(R.string.text_translator));
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                translateLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.action_history:
                toolbar.setTitle(getString(R.string.text_history));
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                translateLayout.setVisibility(View.GONE);
                break;
            case R.id.action_favorites:
                toolbar.setTitle(getString(R.string.text_favorites));
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                translateLayout.setVisibility(View.GONE);
                break;
            case R.id.action_settings:
                toolbar.setTitle(getString(R.string.text_settings));
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                translateLayout.setVisibility(View.GONE);
                break;
        }
    }
}
