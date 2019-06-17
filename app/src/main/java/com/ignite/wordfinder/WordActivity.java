package com.ignite.wordfinder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordActivity extends AppCompatActivity {

    String word;
    HashMap<String, List<String>> definitions;
    TextView mWord;
    TextView mDefinition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word);
        mWord = findViewById(R.id.word);
        mDefinition = findViewById(R.id.definition);
        Intent intent = getIntent();
        word = intent.getExtras().getString("word");
        definitions = (HashMap<String, List<String>>) intent.getSerializableExtra("definitions");
        setTitle(word);
        mWord.setText(word);
        String definitionsString = getDefinitionString(definitions);
        mDefinition.setText(definitionsString);
    }

    private String getDefinitionString(HashMap<String, List<String>> map) {
        String defString = "";

        for (Map.Entry<String, List<String>> entry : map.entrySet()) {
            defString += entry.getKey() + "\n\n";
            List<String> list = entry.getValue();
            for (int i = 0; i < list.size(); i++) {
                defString += list.get(i) + "\n\n";
            }
        }
        return defString;
    }
}
