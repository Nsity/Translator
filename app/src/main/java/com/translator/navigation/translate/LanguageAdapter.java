package com.translator.navigation.translate;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.translator.R;

import java.util.ArrayList;

/**
 * Created by nsity on 22.03.17.
 */

public class LanguageAdapter extends BaseAdapter {

    private ArrayList<Language> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;
    private String selectedLang;

    public LanguageAdapter(Context context, ArrayList<Language> arrayList, String selectedLang) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
        this.selectedLang = selectedLang;
    }

    public static class ViewHolder {
        TextView langTextView;
        ImageButton isCheckedImageButton;
        RelativeLayout relativeLayout;

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
            holder.relativeLayout = (RelativeLayout) customView.findViewById(R.id.relative_layout);

            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }


        holder.langTextView.setText(language.getFullName());

        if(language.getName().equals(selectedLang)) {
            holder.isCheckedImageButton.setVisibility(View.VISIBLE);
            holder.relativeLayout.setBackgroundColor(context.getResources().getColor(R.color.grey_color_3));
        }else {
            holder.isCheckedImageButton.setVisibility(View.GONE);
            holder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }

        return customView;
    }

    public void update(String selectedLang) {
        this.selectedLang = selectedLang;
        notifyDataSetChanged();
    }
}
