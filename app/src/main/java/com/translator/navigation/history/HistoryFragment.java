package com.translator.navigation.history;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.system.CommonFunctions;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class HistoryFragment extends Fragment {

    private ListView historyListView;
    private LinearLayout noTranslationsLayout;

    private ArrayList<Translation> arrayList;
    private History history;
    private TranslationAdapter adapter;
    private TextView noTranslationsTextView;
    private EditText searchEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        historyListView = (ListView) rootView.findViewById(R.id.history_list_view);

        noTranslationsLayout = (LinearLayout) rootView.findViewById(R.id.no_translations_layout);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edit_text);
        noTranslationsTextView = (TextView) rootView.findViewById(R.id.no_translations_text);

        setHasOptionsMenu(true);


        history = new History(getActivity());

        arrayList = new ArrayList<>();
        adapter = new TranslationAdapter(getActivity(), arrayList);


        historyListView.setAdapter(adapter);

        updateView(false);



        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String searchQuery = searchEditText.getText().toString();
                history.search(searchQuery);
                updateView(true);

            }
        });


        historyListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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

                                        history.deleteItem(i);

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

    private void updateView(boolean isSearch) {

        if(isSearch) {
            noTranslationsTextView.setText(getString(R.string.not_found));
        } else {
            noTranslationsTextView.setText(getString(R.string.no_translations_in_history));
        }

        arrayList = history.getTranslations();

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
