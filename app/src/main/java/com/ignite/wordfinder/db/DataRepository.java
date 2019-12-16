package com.ignite.wordfinder.db;

import android.app.Application;
import androidx.lifecycle.LiveData;
import android.os.AsyncTask;

import com.ignite.wordfinder.db.dao.DefinitionDao;
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

    public Long insert(final WordEntity word) throws ExecutionException, InterruptedException {
        AsyncTask<WordEntity, Void, Long> task = new insertAsyncTask(mDatabase.wordDao()).execute(word);
        return task.get();
    }

    private static class insertAsyncTask extends AsyncTask<WordEntity, Void, Long> {

        private WordDao mAsyncTaskDao;

        insertAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final WordEntity... params) {
            if (mAsyncTaskDao.findByName(params[0].getName()) == null) {
                return mAsyncTaskDao.insert(params[0]);
            }
            return null;
        }
    }


    public Boolean wordExists(WordEntity word) throws ExecutionException, InterruptedException {
        AsyncTask<WordEntity, Void, Boolean> task = new existsAsyncTask(mDatabase.wordDao()).execute(word);
        Boolean result = task.get();
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

    public List<DefinitionEntity> loadDefinitions(final int wordId) throws ExecutionException, InterruptedException {
        AsyncTask<Integer, Void, List<DefinitionEntity>> task = new loadDefAsyncTask(mDatabase.definitionDao()).execute(wordId);
        return task.get();
    }

    private static class loadDefAsyncTask extends AsyncTask<Integer, Void, List<DefinitionEntity>> {

        private DefinitionDao mAsyncTaskDao;

        loadDefAsyncTask(DefinitionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<DefinitionEntity> doInBackground(Integer... ids) {
            return mAsyncTaskDao.loadDefinitions(ids[0]);
        }
    }

    public void insert(DefinitionEntity definition) {
        AsyncTask<DefinitionEntity, Void, Void> task = new insertDefAsyncTask(mDatabase.definitionDao()).execute(definition);
    }


    private static class insertDefAsyncTask extends AsyncTask<DefinitionEntity, Void, Void> {

        private DefinitionDao mAsyncTaskDao;

        insertDefAsyncTask(DefinitionDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(DefinitionEntity... list) {
            mAsyncTaskDao.insertAll(list[0]);
            return null;
        }
    }

    public WordEntity getWordByName(String word) {
        WordEntity result = null;
        AsyncTask<String, Void, WordEntity> task = new gwbnAsyncTask(mDatabase.wordDao()).execute(word);
        try {
            result = task.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static class gwbnAsyncTask extends AsyncTask<String, Void, WordEntity> {

        private WordDao mAsyncTaskDao;

        gwbnAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected WordEntity doInBackground(final String... params) {
            return mAsyncTaskDao.getWordByName(params[0]);
        }
    }

    public void deleteByWordId(int wordId) {
        AsyncTask<Integer, Void, Void> task = new dbwiAsyncTask(mDatabase.wordDao()).execute(wordId);
    }


    private static class dbwiAsyncTask extends AsyncTask<Integer, Void, Void> {

        private WordDao mAsyncTaskDao;

        dbwiAsyncTask(WordDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Integer... params) {
            mAsyncTaskDao.deleteByWordId(params[0]);
            return null;
        }
    }


//    public LiveData<List<WordEntity>> searchProducts(String query) {
//        return mDatabase.productDao().searchAllProducts(query);
//    }
}
