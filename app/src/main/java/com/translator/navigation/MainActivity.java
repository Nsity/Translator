package com.translator.navigation;

import android.graphics.Rect;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.translator.R;
import com.translator.navigation.favorites.FavoritesFragment;
import com.translator.navigation.history.HistoryFragment;
import com.translator.navigation.translation.TranslateFragment;
import com.translator.system.CommonFunctions;
import com.translator.system.Language;
import com.translator.system.Preferences;
import com.translator.system.network.CallBack;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Fragment translateFragment, historyFragment, favoritesFragment, settingsFragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private RelativeLayout translateLayout;

    private BottomNavigationView bottomNavigationView;

    int selectedPosition = 0;

    private CoordinatorLayout rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        rootView = (CoordinatorLayout) findViewById(R.id.root_view);

        checkKeyBoardUp();


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        translateLayout = (RelativeLayout) findViewById(R.id.translate_layout);

        /*TranslationManager.getLanguages(getApplicationContext(), null, new CallBack() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(String message) {
            }
        });*/


        fragmentManager = getSupportFragmentManager();
        translateFragment = new TranslateFragment();
        historyFragment = new HistoryFragment();
        favoritesFragment = new FavoritesFragment();
        settingsFragment = new SettingsFragment();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

        displayView(R.id.action_translate);
        updateNavigationBarState(R.id.action_translate);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                            displayView(item.getItemId());
                        return true;
                    }
                });
    }


    private void displayView(int itemId) {
        setUpToolbar(itemId);


        final FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch (itemId) {
            case R.id.action_translate:
                selectedPosition = 0;
               // transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.main_container, translateFragment).commit();
                break;
            case R.id.action_history:
                selectedPosition = 1;
                //transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.main_container, historyFragment).commit();
                break;
            case R.id.action_favorites:
                selectedPosition = 2;
               // transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.main_container, favoritesFragment).commit();
                break;
            case R.id.action_settings:
                selectedPosition = 3;
               // transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.main_container, settingsFragment).commit();
                break;
        }
    }

    private void setUpToolbar(int id) {
        if(toolbar == null)
            return;

        switch (id) {
            case R.id.action_translate:
                getSupportActionBar().setDisplayShowTitleEnabled(false);
                toolbar.setTitle(getString(R.string.text_translator));
                toolbar.setTitleMarginStart(0);
                translateLayout.setVisibility(View.VISIBLE);
                break;
            case R.id.action_history:
                translateLayout.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                toolbar.setTitle(getString(R.string.text_history));
                toolbar.setTitleMarginStart(36);
                break;
            case R.id.action_favorites:
                translateLayout.setVisibility(View.GONE);
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                toolbar.setTitle(getString(R.string.text_favorites));
                toolbar.setTitleMarginStart(36);
                break;
            /*case R.id.action_settings:
                toolbar.setTitle(getString(R.string.text_settings));
                getSupportActionBar().setDisplayShowTitleEnabled(true);
                translateLayout.setVisibility(View.GONE);
                break;*/
        }
    }

    /**
     * Метод, который скрывает bottomNavigationView, когда на экране есть клавиатура
     */
    public void checkKeyBoardUp(){
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                    //ok now we know the keyboard is up...
                    bottomNavigationView.setVisibility(View.GONE);

                }else{
                    //ok now we know the keyboard is down...
                    bottomNavigationView.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    @Override
    public void onBackPressed() {
        if(selectedPosition != 0) {
            displayView(R.id.action_translate);
            updateNavigationBarState(R.id.action_translate);
        } else {
            super.onBackPressed();
        }

    }


    private void updateNavigationBarState(int actionId){
        Menu menu = bottomNavigationView.getMenu();
        menu.findItem(actionId).setChecked(true);
    }
}
