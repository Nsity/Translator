package com.translator.navigation.favorites;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.navigation.history.History;
import com.translator.navigation.history.TranslationAdapter;
import com.translator.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class FavoritesFragment extends Fragment {

    private ListView favoritesListView;

    private LinearLayout noTranslationsLayout;

    private Favorite favorite;
    private ArrayList<Translation> arrayList;

    private TranslationAdapter adapter;
    private TextView noTranslationsTextView;
    private EditText searchEditText;
    private ImageButton clearButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        favoritesListView = (ListView) rootView.findViewById(R.id.favorites_list_view);

        noTranslationsLayout = (LinearLayout) rootView.findViewById(R.id.no_translations_layout);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edit_text);
        noTranslationsTextView = (TextView) rootView.findViewById(R.id.no_translations_text);
        clearButton = (ImageButton) rootView.findViewById(R.id.clear_button);

        setHasOptionsMenu(true);

        favorite = new Favorite(getActivity());

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


        arrayList = new ArrayList<>();
        adapter = new TranslationAdapter(getActivity(), arrayList);
        favoritesListView.setAdapter(adapter);

        updateView(false);

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchEditText.setText("");
                clearButton.setVisibility(View.GONE);
            }
        });


        favoritesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle(getString(R.string.text_delete))
                        .setMessage(R.string.delete_chosen_item)
                        .setCancelable(true)
                        .setPositiveButton(getResources().getString(R.string.yes).toUpperCase(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        favorite.deleteItem(i);

                                        updateView(false);

                                        dialog.dismiss();
                                    }
                                })
                        .setNegativeButton(getResources().getString(R.string.no).toUpperCase(),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                        .create();
                alertDialog.show();

                return true;
            }
        });

        return rootView;
    }


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


    private void search() {
        String searchQuery = searchEditText.getText().toString();
        favorite.search(searchQuery);
        updateView(true);
    }


    private void updateView(boolean isSearch) {

        if(isSearch) {
            noTranslationsTextView.setText(getString(R.string.not_found));
        } else {
            favorite.loadFromDB();
            noTranslationsTextView.setText(getString(R.string.no_translations_in_favorites));
        }

        arrayList = favorite.getTranslations();

        adapter.update(arrayList);

        if(arrayList.size() != 0) {
            searchEditText.setVisibility(View.VISIBLE);
            noTranslationsLayout.setVisibility(View.GONE);
        } else {

            if(isSearch) {
                searchEditText.setVisibility(View.VISIBLE);
            } else {
                searchEditText.setVisibility(View.GONE);
            }

            noTranslationsLayout.setVisibility(View.VISIBLE);
        }
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


    private void deleteAll() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.text_favorites))
                .setMessage(R.string.delete_all_message_in_favorites)
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.yes).toUpperCase(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                favorite.delete();


                                updateView(false);

                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getResources().getString(R.string.no).toUpperCase(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                .create();
        alertDialog.show();
    }
}
