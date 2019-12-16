package com.ignite.wordfinder.db.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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
    long insert(WordEntity word);

    @Query("select * from words where id = :wordId")
    LiveData<WordEntity> loadWord(int wordId);

    @Query("select * from words where id = :wordId")
    WordEntity loadWordSync(int wordId);

    @Query("select * from words where name = :word")
    WordEntity getWordByName(String word);

    @Query("DELETE FROM words WHERE id = :wordId")
    void deleteByWordId(long wordId);

//    @Query("SELECT products.* FROM products JOIN productsFts ON (products.id = productsFts.rowid) "
//            + "WHERE productsFts MATCH :query")
//    LiveData<List<ProductEntity>> searchAllProducts(String query);
}
