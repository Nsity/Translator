package com.translator.navigation.history;

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
import android.widget.ListView;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.navigation.TranslationAdapter;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class HistoryFragment extends Fragment {

    private ListView historyListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        historyListView = (ListView) rootView.findViewById(R.id.history_list_view);


        setHasOptionsMenu(true);

        ArrayList<Translation> arrayList = new ArrayList<>();

        for(int i=0; i<10;i++){
            Translation translation1 = new Translation(1);
            translation1.setInputText("мама");
            translation1.setTranslationText("mom");
            translation1.setInputLang("RU");
            translation1.setTranslationLang("EN");
            translation1.setFavorite(false);

            arrayList.add(translation1);
        }

        TranslationAdapter adapter = new TranslationAdapter(getActivity(), arrayList);
        historyListView.setAdapter(adapter);


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
                .setTitle(getString(R.string.text_history))
                .setMessage(R.string.delete_all_message_in_history)
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
