package com.ignite.wordfinder.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.ignite.wordfinder.db.App;
import com.ignite.wordfinder.db.DataRepository;
import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private final LiveData<WordEntity> mObservableWord;

    public ObservableField<WordEntity> product = new ObservableField<>();

    private final int mWordId;

    private final LiveData<List<DefinitionEntity>> mObservableDefinitions;

    public WordViewModel(@NonNull Application application, DataRepository repository,
                         final int wordId) {
        super(application);
        mWordId = wordId;

        mObservableDefinitions = repository.loadDefinitions(mWordId);
        mObservableWord = repository.loadWord(mWordId);
    }

    /**
     * Expose the LiveData Comments query so the UI can observe it.
     */
    public LiveData<List<DefinitionEntity>> getComments() {
        return mObservableDefinitions;
    }

    public LiveData<WordEntity> getObservableProduct() {
        return mObservableWord;
    }

    public void setProduct(WordEntity word) {
        this.product.set(word);
    }

    /**
     * A creator is used to inject the product ID into the ViewModel
     * <p>
     * This creator is to showcase how to inject dependencies into ViewModels. It's not
     * actually necessary in this case, as the product ID can be passed in a public method.
     */
    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;

        private final int mWordId;

        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int wordId) {
            mApplication = application;
            mWordId = wordId;
            mRepository = ((App) application).getRepository();
        }

        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new WordViewModel(mApplication, mRepository, mWordId);
        }
    }
}

