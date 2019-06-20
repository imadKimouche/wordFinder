package com.ignite.wordfinder.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.ignite.wordfinder.model.Word;

import java.io.Serializable;

@Entity(tableName = "words")
public class WordEntity implements Word, Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WordEntity() {
    }

    @Ignore
    public WordEntity(String name) {
        this.name = name;
    }

    public WordEntity(Word word) {
        this.id = word.getId();
        this.name = word.getName();
    }
}
