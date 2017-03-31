package com.translator.navigation.favorites;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.translator.R;
import com.translator.navigation.history.TranslationAdapter;

import java.util.ArrayList;

/**
 * Created by fedorova on 31.03.2017.
 */

public class FavoritesFragmentNew extends TranslationFragment {

    private Favorite favorite;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        favorite = new Favorite(getActivity());

        arrayList = new ArrayList<>();
        adapter = new TranslationAdapter(getActivity(), arrayList);
        listView.setAdapter(adapter);


        noTranslationsImageView.setImageDrawable(getResources().getDrawable(R.mipmap.ic_bookmark_black_48dp));

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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


        updateView(false);
    }

    @Override
    protected void updateView(boolean isSearch) {
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
    protected void search() {
        String searchQuery = searchEditText.getText().toString();
        favorite.search(searchQuery);
        updateView(true);
    }

    @Override
    protected void deleteAll() {
        try {
            searchEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
