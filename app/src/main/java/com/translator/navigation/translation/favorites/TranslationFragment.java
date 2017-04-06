package com.translator.navigation.translation.favorites;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.navigation.translation.history.TranslationAdapter;
import com.translator.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by fedorova on 31.03.2017.
 */

public abstract class TranslationFragment extends Fragment {

    protected LinearLayout noTranslationsLayout;
    protected ArrayList<Translation> arrayList;
    protected TranslationAdapter adapter;
    protected TextView noTranslationsTextView;
    protected EditText searchEditText;
    protected ImageButton clearButton;
    protected ListView listView;

    protected ImageView noTranslationsImageView;


    protected OnChangedFragmentInterface onShowTranslationInterface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        listView = (ListView) rootView.findViewById(R.id.history_list_view);
        noTranslationsLayout = (LinearLayout) rootView.findViewById(R.id.no_translations_layout);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edit_text);
        noTranslationsTextView = (TextView) rootView.findViewById(R.id.no_translations_text);
        clearButton = (ImageButton) rootView.findViewById(R.id.clear_button);

        noTranslationsImageView = (ImageView) rootView.findViewById(R.id.no_translations_image_view);


        onShowTranslationInterface = (OnChangedFragmentInterface) getActivity();

        setHasOptionsMenu(true);

        //-----------------------//
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(CommonFunctions.StringIsNullOrEmpty(searchEditText.getText().toString())) {
                    clearButton.setVisibility(View.GONE);
                } else {
                    clearButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                search();
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText("");
                clearButton.setVisibility(View.GONE);
            }
        });

        clearButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View arg0, MotionEvent me) {
                if (me.getAction() == MotionEvent.ACTION_DOWN) {
                    clearButton.setColorFilter(getResources().getColor(R.color.listSelector));
                    return false;
                } else if (me.getAction() == MotionEvent.ACTION_UP) {
                    clearButton.setColorFilter(Color.BLACK); // or null
                    return false;
                }
                return false;
            }

        });


        searchEditText.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    searchEditText.clearFocus();
                    try {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return true;
                }
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Translation translation = arrayList.get(i);
                onShowTranslationInterface.showFragment(translation);
            }
        });

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.delete_all_menu, menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                deleteAll();
                return false;
        }

        return super.onOptionsItemSelected(item);
    }

    protected abstract void updateView(boolean isSearch);
    protected abstract void search();
    protected abstract void deleteAll();


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden) {
            if(CommonFunctions.StringIsNullOrEmpty(searchEditText.getText().toString())) {
                updateView(false);
            } else {
                search();
            }

        }

    }
}
