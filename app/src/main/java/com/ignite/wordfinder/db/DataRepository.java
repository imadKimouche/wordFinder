package com.ignite.wordfinder.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;

/**
 * Repository handling the work with words and definitions.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private MediatorLiveData<List<WordEntity>> mObservableWords;

    private DataRepository(final AppDatabase database) {
        mDatabase = database;
        mObservableWords = new MediatorLiveData<>();

        mObservableWords.addSource(mDatabase.wordDao().loadAllWords(),
                new Observer<List<WordEntity>>() {
                    @Override
                    public void onChanged(@Nullable List<WordEntity> wordEntities) {
                        if (mDatabase.getDatabaseCreated().getValue() != null) {
                            mObservableWords.postValue(wordEntities);
                        }
                    }
                });
    }

    public static DataRepository getInstance(final AppDatabase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<WordEntity>> getWords() {
        return mObservableWords;
    }

    public LiveData<WordEntity> loadWord(final int wordId) {
        return mDatabase.wordDao().loadWord(wordId);
    }

    public LiveData<List<DefinitionEntity>> loadDefinitions(final int wordId) {
        return mDatabase.definitionDao().loadDefinitions(wordId);
    }

//    public LiveData<List<WordEntity>> searchProducts(String query) {
//        return mDatabase.productDao().searchAllProducts(query);
//    }
}
