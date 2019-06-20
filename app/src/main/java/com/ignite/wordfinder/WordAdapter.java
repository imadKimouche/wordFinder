package com.ignite.wordfinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;



public class WordAdapter extends ArrayAdapter<WordEntity> {

    private List<WordEntity> mWordsList;

    public WordAdapter(Context context, List<WordEntity> words) {
        super(context, 0, words);
        mWordsList = words;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.word_row, parent, false);
        }

        FeedViewHolder viewHolder = (FeedViewHolder) convertView.getTag();
        if (viewHolder == null) {
            viewHolder = new FeedViewHolder();
            viewHolder.title = convertView.findViewById(R.id.title);
            convertView.setTag(viewHolder);
        }

        WordEntity word = mWordsList.get(position);
        if (word != null) {
            viewHolder.title.setText(word.getName());
        }


        return convertView;
    }

    private class FeedViewHolder {
        public TextView title;
    }

    @Override
    public int getCount() {
        return mWordsList != null ? mWordsList.size() : 0;
    }

    public void setmWordsList(List<WordEntity> words) {
        mWordsList = words;
    }

    public void addWord(WordEntity word) {
        mWordsList.add(word);
    }

}
