package com.ignite.wordfinder.db.dao;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ignite.wordfinder.db.entity.WordEntity;

import java.util.List;

@Dao
public interface WordDao {
    @Query("SELECT * FROM words")
    LiveData<List<WordEntity>> loadAllWords();

    @Query("SELECT * FROM words where name LIKE  :name")
    WordEntity findByName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<WordEntity> words);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WordEntity word);

    @Query("select * from words where id = :wordId")
    LiveData<WordEntity> loadWord(int wordId);

    @Query("select * from words where id = :wordId")
    WordEntity loadWordSync(int wordId);

    @Query("select * from words where name = :word")
    WordEntity getWordByName(String word);

//    @Query("SELECT products.* FROM products JOIN productsFts ON (products.id = productsFts.rowid) "
//            + "WHERE productsFts MATCH :query")
//    LiveData<List<ProductEntity>> searchAllProducts(String query);
}
