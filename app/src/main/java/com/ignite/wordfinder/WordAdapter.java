package com.ignite.wordfinder;

import android.databinding.DataBindingUtil;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ignite.wordfinder.databinding.WordItemBinding;
import com.ignite.wordfinder.model.Word;

import java.util.List;
import java.util.Objects;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.WordViewHolder> {

    List<? extends Word> mWordList;

    @Nullable
    private final WordClickCallback mWordClickCallback;

    public WordAdapter(@Nullable WordClickCallback clickCallback) {
        mWordClickCallback = clickCallback;
        setHasStableIds(true);
    }

    public void setWordList(final List<? extends Word> wordList) {
        if (mWordList == null) {
            mWordList = wordList;
            notifyItemRangeInserted(0, wordList.size());
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mWordList.size();
                }

                @Override
                public int getNewListSize() {
                    return wordList.size();
                }

                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mWordList.get(oldItemPosition).getId() ==
                            wordList.get(newItemPosition).getId();
                }

                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    Word newWord = wordList.get(newItemPosition);
                    Word oldWord = mWordList.get(oldItemPosition);
                    return newWord.getId() == oldWord.getId()
                            && newWord.getName().equals(oldWord.getName());
                }
            });
            mWordList = wordList;
            result.dispatchUpdatesTo(this);
        }
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WordItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), R.layout.word_item,
                        parent, false);
        binding.setCallback(mWordClickCallback);
        return new WordViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        holder.binding.setWord(mWordList.get(position));
        holder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mWordList == null ? 0 : mWordList.size();
    }

    @Override
    public long getItemId(int position) {
        return mWordList.get(position).getId();
    }

    static class WordViewHolder extends RecyclerView.ViewHolder {

        final WordItemBinding binding;

        public WordViewHolder(WordItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
