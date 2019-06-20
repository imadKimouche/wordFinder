package com.ignite.wordfinder.db;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.ignite.wordfinder.db.dao.WordDao;
import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Repository handling the work with words and definitions.
 */
public class DataRepository {

    private static DataRepository sInstance;

    private final AppDatabase mDatabase;
    private LiveData<List<WordEntity>> mWordsList;

    public DataRepository(Application application) {
        mDatabase = AppDatabase.getInstance(application);
        mWordsList = mDatabase.wordDao().loadAllWords();
    }

    public static DataRepository getInstance(final Application application) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(application);
                }
            }
        }
        return sInstance;
    }

    /**
     * Get the list of products from the database and get notified when the data changes.
     */
    public LiveData<List<WordEntity>> getWords() {
        return mWordsList;
    }

    public void insert(final WordEntity word) {
        AsyncTask<WordEntity, Void, Void> task = new insertAsyncTask(mDatabase.wordDao()).execute(word);

    }

    private static class insertAsyncTask extends AsyncTask<WordEntity, Void, Void> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final WordEntity... params) {
            if (mAsyncTaskDao.findByName(params[0].getName()) == null) {
                mAsyncTaskDao.insert(params[0]);
            }
            return null;
        }
    }


    public Boolean wordExists(WordEntity word) throws ExecutionException, InterruptedException {
        AsyncTask<WordEntity, Void, Boolean> task = new existsAsyncTask(mDatabase.wordDao()).execute(word);
        Boolean result = task.get();
        Log.i("bool", String.valueOf(result));
        return result;
    }

    private static class existsAsyncTask extends AsyncTask<WordEntity, Void, Boolean> {

        private WordDao mAsyncTaskDao;

        existsAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final WordEntity... params) {
            return mAsyncTaskDao.findByName(params[0].getName()) != null;
        }
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
