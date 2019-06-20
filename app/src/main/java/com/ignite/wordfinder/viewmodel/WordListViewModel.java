package com.ignite.wordfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.ignite.wordfinder.db.App;
import com.ignite.wordfinder.db.DataRepository;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;

public class WordListViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final LiveData<List<WordEntity>> mWordsList;

    public WordListViewModel(Application application) {
        super(application);
        mRepository = new DataRepository(application);
        mWordsList = mRepository.getWords();
    }

    /**
     * Adds a word to the words list.
     */
    public void insert(WordEntity word) {
        mRepository.insert(word);
    }


    /**
     * Expose the LiveData Words query so the UI can observe it.
     */
    public LiveData<List<WordEntity>> getWords() {
        return mWordsList;
    }
//
//    public LiveData<List<WordEntity>> searchWords(String query) {
//        return mRepository.searchWords(query);
//    }
}
