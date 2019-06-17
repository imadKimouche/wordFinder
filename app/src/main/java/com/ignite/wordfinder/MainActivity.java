package com.ignite.wordfinder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    List<String> wordList = new ArrayList<>();
    List<Pair<String, HashMap<String, List<String>>>> words = new ArrayList();
    ListView listView;
    Button findButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.list);
        findButton = findViewById(R.id.findButton);
        editText = findViewById(R.id.editText);
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, wordList);
        listView.setAdapter(adapter);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String word = editText.getText().toString();
                if (!word.isEmpty()) {
                    HttpRequest.find(word, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            Log.i("success", String.valueOf(statusCode));
                            wordList.add(word);
                            adapter.notifyDataSetChanged();
                            try {
                                HashMap<String, List<String>> map;
                                map = getDefinitions(new JSONObject(new String(responseBody)).getJSONObject("meaning"));
                                words.add(new Pair<>(word, map));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.i("failed", responseBody.toString());
                            Toast.makeText(getApplicationContext(), "Word not found", Toast.LENGTH_LONG);
                        }
                    });
                    editText.getText().clear();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), WordActivity.class);
                intent.putExtra("word", words.get(position).first);
                intent.putExtra("definitions", words.get(position).second);
                startActivity(intent);
            }
        });

    }


    private static HashMap<String, List<String>> getDefinitions(JSONObject object) {
        HashMap<String, List<String>> map = new HashMap<>();
        try {
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray array = object.getJSONArray(key);
                List<String> definitions = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject def = array.getJSONObject(i);
                    definitions.add(def.getString("definition"));
                }
                map.put(key, definitions);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }

}
