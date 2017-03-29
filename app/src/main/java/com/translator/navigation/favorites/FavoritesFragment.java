package com.translator.navigation.favorites;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.navigation.history.History;
import com.translator.navigation.history.TranslationAdapter;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class FavoritesFragment extends Fragment {

    private ListView favoritesListView;

    private RelativeLayout translationsLayout;
    private LinearLayout noTranslationsLayout;

    private Favorite favorite;
    private ArrayList<Translation> arrayList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);
        favoritesListView = (ListView) rootView.findViewById(R.id.favorites_list_view);

        translationsLayout = (RelativeLayout) rootView.findViewById(R.id.translations_layout);
        noTranslationsLayout = (LinearLayout) rootView.findViewById(R.id.no_translations_layout);

        setHasOptionsMenu(true);

        favorite = new Favorite(getActivity());
        arrayList = favorite.getTranslations();
        if(arrayList.size() != 0) {
            translationsLayout.setVisibility(View.VISIBLE);
            noTranslationsLayout.setVisibility(View.GONE);
        } else {
            translationsLayout.setVisibility(View.GONE);
            noTranslationsLayout.setVisibility(View.VISIBLE);
        }

        TranslationAdapter adapter = new TranslationAdapter(getActivity(), arrayList);
        favoritesListView.setAdapter(adapter);



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


    private void deleteAll() {
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.text_favorites))
                .setMessage(R.string.delete_all_message_in_favorites)
                .setCancelable(true)
                .setPositiveButton(getResources().getString(R.string.yes).toUpperCase(),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
