package com.ignite.wordfinder;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ignite.wordfinder.databinding.MywordsFragmentBinding;
import com.ignite.wordfinder.db.entity.WordEntity;
import com.ignite.wordfinder.model.Word;
import com.ignite.wordfinder.viewmodel.WordListViewModel;

import java.util.List;

public class MyWordsFragment extends Fragment {
    public static final String TAG = "WordListFragment";

    private WordAdapter mWordAdapter;

    private MywordsFragmentBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.mywords_fragment, container, false);

        mWordAdapter = new WordAdapter(mWordClickCallback);
        mBinding.wordsList.setAdapter(mWordAdapter);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final WordListViewModel viewModel =
                ViewModelProviders.of(this).get(WordListViewModel.class);

        mBinding.wordsSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Editable query = mBinding.wordsSearchBox.getText();
                if (query == null || query.toString().isEmpty()) {
                    subscribeUi(viewModel.getWords());
                } else {
//                    subscribeUi(viewModel.searchProducts("*" + query + "*"));
                }
            }
        });

        subscribeUi(viewModel.getWords());
    }

    private void subscribeUi(LiveData<List<WordEntity>> liveData) {
        // Update the list when the data changes
        liveData.observe(this, new Observer<List<WordEntity>>() {
            @Override
            public void onChanged(@Nullable List<WordEntity> myWords) {
                if (myWords != null) {
                    mBinding.setIsLoading(false);
                    mWordAdapter.setWordList(myWords);
                } else {
                    mBinding.setIsLoading(true);
                }
                mBinding.executePendingBindings();
            }
        });
    }

    private final WordClickCallback mWordClickCallback = new WordClickCallback() {
        @Override
        public void onClick(Word word) {

            if (getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED)) {
                ((MainActivity) getActivity()).show(word);
            }
        }
    };


}
