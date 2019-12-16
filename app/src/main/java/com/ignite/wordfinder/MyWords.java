package com.ignite.wordfinder;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;
import com.ignite.wordfinder.viewmodel.WordListViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MyWords extends Fragment {

    HashMap<Integer, List<DefinitionEntity>> wordDefinitions = new HashMap<>();
    ExpandableListView wordsLv;
    ExpandableListAdapter adapter;
    EditText searchBox;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_words, container, false);


        final WordListViewModel viewModel =
                ViewModelProviders.of(this).get(WordListViewModel.class);
        wordsLv = view.findViewById(R.id.wordsLv);
        searchBox = view.findViewById(R.id.words_search_box);
        viewModel.getWords().observe(getActivity(), new Observer<List<WordEntity>>() {
            @Override
            public void onChanged(@Nullable final List<WordEntity> words) {
                Collections.sort(words, new Comparator<WordEntity>() {
                    @Override
                    public int compare(WordEntity o1, WordEntity o2) {
                        return o1.getName().compareToIgnoreCase(o2.getName());
                    }
                });
                for (int i = 0; i < words.size(); i++) {
                    try {
                        wordDefinitions.put(words.get(i).getId(), viewModel.loadDefinitions(words.get(i).getId()));
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                adapter = new WordAdapter(getContext(), words, wordDefinitions);
                wordsLv.setAdapter(adapter);
                ((WordAdapter) adapter).notifyDataSetChanged();

                searchBox.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        String search = s.toString();
                        List<WordEntity> toDisplay = new ArrayList<>();

                        for (WordEntity word : words) {
                            if (word.getName().toLowerCase().contains(search.toLowerCase())) {
                                toDisplay.add(word);

                                adapter = new WordAdapter(getContext(), toDisplay, wordDefinitions);
                                wordsLv.setAdapter(adapter);
                                ((WordAdapter) adapter).notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        });

        return view;
    }
}
