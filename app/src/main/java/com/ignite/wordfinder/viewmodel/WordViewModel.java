package com.ignite.wordfinder.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.ignite.wordfinder.db.DataRepository;
import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class WordViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
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
}

