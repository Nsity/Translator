package com.translator.navigation.translation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.translator.R;
import com.translator.system.Language;

import java.util.ArrayList;

/**
 * Created by nsity on 22.03.17.
 */

public class LanguageAdapter extends BaseAdapter {

    private ArrayList<Language> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public LanguageAdapter(Context context, ArrayList<Language> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder {
        TextView langTextView;
        ImageButton isCheckedImageButton;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return arrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View customView = view;
        final Language language = arrayList.get(i);
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_language, parent, false);

            holder.langTextView = (TextView) customView.findViewById(R.id.lang_text);
            holder.isCheckedImageButton = (ImageButton) customView.findViewById(R.id.is_checked);

            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }


        holder.langTextView.setText(language.getFullName());

        return customView;
    }
}
