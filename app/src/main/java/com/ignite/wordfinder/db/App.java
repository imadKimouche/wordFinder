package com.ignite.wordfinder.db;


import android.app.Application;

/**
 * Android Application class. Used for accessing singletons.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public AppDatabase getDatabase() {
        return AppDatabase.getInstance(this);
    }

    public DataRepository getRepository() {
        return DataRepository.getInstance(this);
    }
}
