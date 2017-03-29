package com.translator.navigation.history;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
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
import com.translator.system.CommonFunctions;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class HistoryFragment extends Fragment {

    private ListView historyListView;
    private RelativeLayout translationsLayout;
    private LinearLayout noTranslationsLayout;

    private ArrayList<Translation> arrayList;
    private History history;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        historyListView = (ListView) rootView.findViewById(R.id.history_list_view);

        translationsLayout = (RelativeLayout) rootView.findViewById(R.id.translations_layout);
        noTranslationsLayout = (LinearLayout) rootView.findViewById(R.id.no_translations_layout);

        setHasOptionsMenu(true);


        history = new History(getActivity());
        arrayList = history.getTranslations();
        if(arrayList.size() != 0) {
            translationsLayout.setVisibility(View.VISIBLE);
            noTranslationsLayout.setVisibility(View.GONE);
        } else {
            translationsLayout.setVisibility(View.GONE);
            noTranslationsLayout.setVisibility(View.VISIBLE);
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
    public void onHiddenChanged(boolean hidden) {
        if(!hidden) {
            Log.i("TAG", "asdad");
        }

        super.onHiddenChanged(hidden);
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

                                history.delete();
                                arrayList = history.getTranslations();

                                TranslationAdapter adapter = new TranslationAdapter(getActivity(), arrayList);
                                historyListView.setAdapter(adapter);

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
