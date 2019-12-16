package com.ignite.wordfinder.db;


import android.content.Context;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ignite.wordfinder.db.dao.DefinitionDao;
import com.ignite.wordfinder.db.dao.WordDao;
import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;

@Database(entities = {WordEntity.class, DefinitionEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase sInstance;

    @VisibleForTesting
    public static final String DATABASE_NAME = "words-db";

    public abstract WordDao wordDao();

    public abstract DefinitionDao definitionDao();

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (AppDatabase.class) {
                if (sInstance == null) {
                    sInstance =
                            Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                                    .build();
                }
            }
        }
        return sInstance;
    }


    /**
     * Check whether the database already exists and expose it via {@link #getDatabaseCreated()}
     */
    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DATABASE_NAME).exists()) {
//            setDatabaseCreated();
        }
    }

    private static void insertData(final AppDatabase database, final List<WordEntity> words,
                                   final List<DefinitionEntity> definitions) {
        database.runInTransaction(new Runnable() {
            @Override
            public void run() {
                database.wordDao().insertAll(words);
            }
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return mIsDatabaseCreated;
    }

//    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("CREATE VIRTUAL TABLE IF NOT EXISTS `productsFts` USING FTS4("
//                    + "`name` TEXT, `description` TEXT, content=`products`)");
//            database.execSQL("INSERT INTO productsFts (`rowid`, `name`, `description`) "
//                    + "SELECT `id`, `name`, `description` FROM products");
//
//        }
//    };
}
