package com.ignite.wordfinder;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ignite.wordfinder.db.entity.WordEntity;
import com.ignite.wordfinder.viewmodel.WordListViewModel;

import java.util.ArrayList;
import java.util.List;

public class MyWordsFragment extends Fragment {
    public static final String TAG = "WordListFragment";

    ListView mWordsListView;
    List<WordEntity> mWordsList = new ArrayList<>();
    private Context mContext;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mywords_fragment, container, false);
        mContext = inflater.getContext();
        mWordsListView = view.findViewById(R.id.words_list);
        final WordAdapter adapter = new WordAdapter(mContext, mWordsList);
        mWordsListView.setAdapter(adapter);

        WordListViewModel viewModel = ViewModelProviders.of(this).get(WordListViewModel.class);
        viewModel.getWords().observe(this, new Observer<List<WordEntity>>() {
            @Override
            public void onChanged(@Nullable List<WordEntity> words) {

                mWordsList.clear();
                mWordsList.addAll(words);
                adapter.notifyDataSetChanged();
            }
        });

        return view;
    }
}