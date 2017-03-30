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


    private final static int TRANSLATE_ITEM = 0;
    private final static int HISTORY_ITEM = 1;
    private final static int FAVORITES_ITEM = 2;

    private Fragment[] fragments = new Fragment[FAVORITES_ITEM + 1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentManager fm = getSupportFragmentManager();
        fragments[TRANSLATE_ITEM] = fm.findFragmentById(R.id.firstFragment);
        fragments[HISTORY_ITEM] = fm.findFragmentById(R.id.secondFragment);
        fragments[FAVORITES_ITEM] = fm.findFragmentById(R.id.thirdFragment);

        FragmentTransaction transaction = fm.beginTransaction();
        for (Fragment fragment : fragments) {
            transaction.hide(fragment);
        }
        transaction.commit();


        rootView = (CoordinatorLayout) findViewById(R.id.root_view);

        checkKeyBoardUp();



        translateLayout = (RelativeLayout) findViewById(R.id.translate_layout);

        /*TranslationManager.getLanguages(getApplicationContext(), null, new CallBack() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onFail(String message) {
            }
        });*/




     /*   fragmentManager = getSupportFragmentManager();
        translateFragment = new TranslateFragment();
        historyFragment = new HistoryFragment();
        favoritesFragment = new FavoritesFragment();
        settingsFragment = new SettingsFragment();*/

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




    private int getPosition(int itemId) {
        switch (itemId) {
            case R.id.action_translate:
                return TRANSLATE_ITEM;
            case R.id.action_history:
                return HISTORY_ITEM;
            case R.id.action_favorites:
                return FAVORITES_ITEM;
        }
        return 0;
    }


    private void displayView(int itemId) {
        setUpToolbar(itemId);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();

        int position = getPosition(itemId);

        //http://stackoverflow.com/questions/4932462/animate-the-transition-between-fragments
        if(selectedPosition != position) {
            if(selectedPosition < position) {
                //right
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right);
            } else {
                //left
                transaction.setCustomAnimations(R.anim.enter_from_left,
                        R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }

        selectedPosition = position;

        for (int i = 0; i < fragments.length; i++) {
            if (i == selectedPosition) {
                transaction.show(fragments[i]);
            } else {
                transaction.hide(fragments[i]);
            }
        }

        transaction.commit();
    }

   /* private void displayView(int itemId) {
        setUpToolbar(itemId);

        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        int position = getPosition(itemId);

        //http://stackoverflow.com/questions/4932462/animate-the-transition-between-fragments
        if(selectedPosition != position) {
            if(selectedPosition < position) {
                //right
                transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                        R.anim.enter_from_left, R.anim.exit_to_right);
            } else {
                //left
                transaction.setCustomAnimations(R.anim.enter_from_left,
                        R.anim.exit_to_right, R.anim.enter_from_right, R.anim.exit_to_left);
            }
        }

        selectedPosition = position;

        switch (itemId) {
            case R.id.action_translate:
                transaction.replace(R.id.main_container, translateFragment).commit();
                break;
            case R.id.action_history:
                transaction.replace(R.id.main_container, historyFragment).commit();
                break;
            case R.id.action_favorites:
                transaction.replace(R.id.main_container, favoritesFragment).commit();
                break;
        }
    }*/

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
