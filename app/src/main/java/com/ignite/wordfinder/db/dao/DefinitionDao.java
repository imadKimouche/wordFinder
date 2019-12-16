package com.ignite.wordfinder.db.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.ignite.wordfinder.db.entity.DefinitionEntity;

import java.util.List;

@Dao
public interface DefinitionDao {
    @Query("SELECT * FROM definitions where wordId = :wordId")
    List<DefinitionEntity> loadDefinitions(int wordId);

    @Query("SELECT * FROM definitions where wordId = :wordId")
    List<DefinitionEntity> loadDefinitionsSync(int wordId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(DefinitionEntity definitions);
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    void insertAll(List<DefinitionEntity>[] lists);
}
