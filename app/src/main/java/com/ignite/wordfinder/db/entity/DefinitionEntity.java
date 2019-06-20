package com.ignite.wordfinder.db.entity;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import com.ignite.wordfinder.model.Definition;

@Entity(tableName = "definitions",
        foreignKeys = {
                @ForeignKey(entity = WordEntity.class,
                        parentColumns = "id",
                        childColumns = "wordId",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "wordId")
        })
public class DefinitionEntity implements Definition {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int wordId;
    private String text;
    private String type;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getWordId() {
        return wordId;
    }

    public void setWordId(int wordId) {
        this.wordId = wordId;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setText(String text) {
        this.text = text;
    }

    public DefinitionEntity() {
    }

    @Ignore
    public DefinitionEntity(int wordId, String text, String type) {
        this.wordId = wordId;
        this.text = text;
        this.type = type;
    }
}
