package com.ignite.wordfinder.viewmodel;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ignite.wordfinder.db.DataRepository;
import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

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
    public Long insert(WordEntity word) throws ExecutionException, InterruptedException {
        return mRepository.insert(word);
    }


    /**
     * Expose the LiveData Words query so the UI can observe it.
     */
    public LiveData<List<WordEntity>> getWords() {
        return mWordsList;
    }

    public void insert(DefinitionEntity definition) {
        mRepository.insert(definition);
    }

    public WordEntity getWordByName(String word) {
        return mRepository.getWordByName(word);
    }

    public List<DefinitionEntity> loadDefinitions(int wordId) throws ExecutionException, InterruptedException {
        return mRepository.loadDefinitions(wordId);
    }

    public void deleteByWordId(int wordId) {
        mRepository.deleteByWordId(wordId);
    }

//
//    public LiveData<List<WordEntity>> searchWords(String query) {
//        return mRepository.searchWords(query);
//    }
}
