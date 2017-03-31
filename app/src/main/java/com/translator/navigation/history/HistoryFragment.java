package com.translator.navigation.history;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.translator.R;
import com.translator.navigation.Translation;
import com.translator.system.CommonFunctions;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by nsity on 18.03.17.
 */

public class HistoryFragment extends Fragment {

    private LinearLayout noTranslationsLayout;
    private ArrayList<Translation> arrayList;
    private History history;
    private TranslationAdapter adapter;
    private TextView noTranslationsTextView;
    private EditText searchEditText;
    private ImageButton clearButton;

    private  CustomTextWatcher obj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        ListView historyListView = (ListView) rootView.findViewById(R.id.history_list_view);
        noTranslationsLayout = (LinearLayout) rootView.findViewById(R.id.no_translations_layout);
        searchEditText = (EditText) rootView.findViewById(R.id.search_edit_text);
        noTranslationsTextView = (TextView) rootView.findViewById(R.id.no_translations_text);
        clearButton = (ImageButton) rootView.findViewById(R.id.clear_button);

        setHasOptionsMenu(true);

        //-----------------------//

        obj = new CustomTextWatcher();
        searchEditText.addTextChangedListener(obj);


        history = new History(getActivity());

        arrayList = new ArrayList<>();
        adapter = new TranslationAdapter(getActivity(), arrayList);
        historyListView.setAdapter(adapter);


        updateView(false);

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

    private class CustomTextWatcher implements TextWatcher {
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
    }


    private void search() {
        String searchQuery = searchEditText.getText().toString();
        history.search(searchQuery);
        updateView(true);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        searchEditText.removeTextChangedListener(obj);

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
            history.loadFromDB();
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
        try {
            searchEditText.clearFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchEditText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }


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
