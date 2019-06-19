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
    private final MediatorLiveData<List<WordEntity>> mObservableWords;

    public WordListViewModel(Application application) {
        super(application);

        mObservableWords = new MediatorLiveData<>();
        // set by default null, until we get data from the database.
        mObservableWords.setValue(null);

        mRepository = ((App) application).getRepository();
        LiveData<List<WordEntity>> words = mRepository.getWords();

        // observe the changes of the products from the database and forward them
        mObservableWords.addSource(words, new Observer<List<WordEntity>>() {
            @Override
            public void onChanged(@Nullable List<WordEntity> value) {
                mObservableWords.setValue(value);
            }
        });
    }

    /**
     * Expose the LiveData Words query so the UI can observe it.
     */
    public LiveData<List<WordEntity>> getWords() {
        return mObservableWords;
    }
//
//    public LiveData<List<WordEntity>> searchWords(String query) {
//        return mRepository.searchWords(query);
//    }
}
