package com.ignite.wordfinder.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import com.ignite.wordfinder.model.Word;

@Entity(tableName = "words")
public class WordEntity implements Word {
    @PrimaryKey
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
    public WordEntity(int id, String name, String description, int price) {
        this.id = id;
        this.name = name;
    }

    public WordEntity(Word word) {
        this.id = word.getId();
        this.name = word.getName();
    }
}
