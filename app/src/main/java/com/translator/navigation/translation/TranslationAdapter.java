package com.translator.navigation.translation;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.translator.R;
import com.translator.navigation.Translation;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by nsity on 20.03.17.
 */

public class TranslationAdapter extends BaseAdapter {

    private ArrayList<Translation> arrayList;
    private Context context;
    private LayoutInflater layoutInflater;

    public TranslationAdapter(Context context, ArrayList<Translation> arrayList) {
        this.arrayList = arrayList;
        this.layoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public static class ViewHolder {
        TextView inputTextView;
        TextView translationTextView;
        ImageButton isFavoriteImageButton;
        TextView languagesTextView;
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
        return arrayList.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {
        View customView = view;
        final Translation translation = arrayList.get(i);
        final ViewHolder holder;

        if (view == null) {
            holder = new ViewHolder();
            customView = layoutInflater.inflate(R.layout.item_translation, parent, false);

            holder.inputTextView = (TextView) customView.findViewById(R.id.input_text);
            holder.translationTextView = (TextView) customView.findViewById(R.id.translation_text);
            holder.languagesTextView = (TextView) customView.findViewById(R.id.languages_text);
            holder.isFavoriteImageButton = (ImageButton) customView.findViewById(R.id.is_favorite);

            customView.setTag(holder);
        } else {
            holder = (ViewHolder) customView.getTag();
        }


        holder.inputTextView.setText(translation.getInputText());
        holder.translationTextView.setText(translation.getTranslationText());
        holder.languagesTextView.setText(translation.getInputLang().toUpperCase(Locale.ENGLISH) +
                " - " + translation.getTranslationLang().toUpperCase(Locale.ENGLISH));

        setIsFavoriteBackground(holder.isFavoriteImageButton, translation.isFavorite());

        holder.isFavoriteImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translation.setFavorite(!translation.isFavorite());
                setIsFavoriteBackground(holder.isFavoriteImageButton, translation.isFavorite());
                translation.update();
            }
        });

        return customView;
    }


    private void setIsFavoriteBackground(ImageButton button, boolean isFavorite) {
        if(isFavorite) {
            button.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
            button.setColorFilter(ContextCompat.getColor(context, R.color.colorBlueGray3));
        }
    }

    public void update(ArrayList<Translation> arrayList) {
        this.arrayList.clear();
        this.arrayList.addAll(arrayList);
        notifyDataSetChanged();
    }
}
