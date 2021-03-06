package com.translator.navigation;

import android.content.Intent;
import android.content.res.Configuration;
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
import android.widget.RelativeLayout;

import com.translator.R;
import com.translator.navigation.translation.OnChangedTranslateFragmentListener;
import com.translator.navigation.translation.OnChangedStateFragmentListener;
import com.translator.navigation.translation.TranslationFragment;
import com.translator.system.Preferences;

import java.util.Locale;

public class MainActivity extends AppCompatActivity implements OnChangedTranslateFragmentListener {

    private Fragment translateFragment, historyFragment, favoritesFragment, settingsFragment;
    private FragmentManager fragmentManager;
    private Toolbar toolbar;
    private RelativeLayout translateLayout;

    private BottomNavigationView bottomNavigationView;

    int selectedPosition = 0;

    private CoordinatorLayout rootView;


    private OnChangedStateFragmentListener onChangedStateFragmentListener;


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


        onChangedStateFragmentListener = (OnChangedStateFragmentListener) fragments[TRANSLATE_ITEM];


        rootView = (CoordinatorLayout) findViewById(R.id.root_view);

        checkKeyboardUp();


        translateLayout = (RelativeLayout) findViewById(R.id.translate_layout);

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
    public void checkKeyboardUp(){
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int heightDiff = rootView.getRootView().getHeight() - (r.bottom - r.top);

                if (heightDiff > 200) { // if more than 200 pixels, its probably a keyboard...
                    //ok now we know the keyboard is up...
                    bottomNavigationView.setVisibility(View.GONE);

                } else {
                    //ok now we know the keyboard is down...
                    bottomNavigationView.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomNavigationView.setVisibility(View.VISIBLE);
                        }
                    });
                    //bottomNavigationView.setVisibility(View.VISIBLE);
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


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // если сменили язык, то загружаем новые языки
        if(!Locale.getDefault().getLanguage().equals(Preferences.get(Preferences.ui_lang, getApplicationContext()))) {
            Intent i = new Intent(MainActivity.this, SplashActivity.class);
            startActivity(i);

            // close this activity
            finish();
            overridePendingTransition(0, 0);
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void makeAction(int action, Translation translation) {

        switch (action) {
            case TranslationFragment.ACTION_SHOW_FRAGMENT:
                displayView(R.id.action_translate);
                updateNavigationBarState(R.id.action_translate);

                onChangedStateFragmentListener.updateFragmentState(action, translation);
                break;

            case TranslationFragment.ACTION_UPDATE_FAVORITE_BUTTON: case TranslationFragment.ACTION_DELETE_TRANSLATION:
                onChangedStateFragmentListener.updateFragmentState(action, translation);
                break;
        }

    }
}
