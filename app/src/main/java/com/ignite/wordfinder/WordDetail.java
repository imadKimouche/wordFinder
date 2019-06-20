package com.ignite.wordfinder;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;
import com.ignite.wordfinder.viewmodel.WordListViewModel;
import com.ignite.wordfinder.viewmodel.WordViewModel;

import java.util.List;

public class WordDetail extends AppCompatActivity {

    private WordEntity word;
    private WordViewModel mViewModel;
    private TextView mWord;
    private TextView mDefinition;
    LiveData<List<DefinitionEntity>> definitions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_detail);
        Intent intent = getIntent();
        word = (WordEntity) intent.getSerializableExtra("word");
        mWord = findViewById(R.id.word);
        mDefinition = findViewById(R.id.definition);
        mWord.setText(word.getName());
        mViewModel = ViewModelProviders.of(this).get(WordViewModel.class);
        definitions = mViewModel.loadDefinitions(word.getId());

        mDefinition.setText(loadDefinitions(definitions.getValue()));
    }

    private String loadDefinitions(List<DefinitionEntity> definitions) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < definitions.size(); i++) {
            result.append(definitions.get(i).getText());
        }
        return result.toString();
    }
}