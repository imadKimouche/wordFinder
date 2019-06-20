package com.ignite.wordfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ignite.wordfinder.db.DataRepository;
import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private final DataRepository mRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        mRepository = new DataRepository(application);
    }

    public void insert(List<DefinitionEntity> definitions) {
        mRepository.insert(definitions);
    }

    public WordEntity getWordByName(String word) {
        return mRepository.getWordByName(word);
    }

    public LiveData<List<DefinitionEntity>> loadDefinitions(int wordId) {
        return mRepository.loadDefinitions(wordId);
    }
}

