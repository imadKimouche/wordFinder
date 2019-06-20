package com.ignite.wordfinder.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.ignite.wordfinder.db.entity.DefinitionEntity;

import java.util.List;

@Dao
public interface DefinitionDao {
    @Query("SELECT * FROM definitions where wordId = :wordId")
    LiveData<List<DefinitionEntity>> loadDefinitions(int wordId);

    @Query("SELECT * FROM definitions where wordId = :wordId")
    List<DefinitionEntity> loadDefinitionsSync(int wordId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DefinitionEntity> definitions);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DefinitionEntity>[] lists);
}
