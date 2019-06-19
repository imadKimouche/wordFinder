package com.ignite.wordfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {
    private Context mContext;
    List<String> wordList = new ArrayList<>();
    List<Pair<String, HashMap<String, List<String>>>> words = new ArrayList();
    ListView listView;
    Button findButton;
    EditText editText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mContext = inflater.getContext();



        listView = view.findViewById(R.id.list);
        findButton = view.findViewById(R.id.findButton);
        editText = view.findViewById(R.id.editText);
        final ArrayAdapter adapter = new ArrayAdapter(mContext, android.R.layout.simple_list_item_1, wordList);
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
                            Log.i("failed", Arrays.toString(responseBody));
                            Toast.makeText(mContext, "Word not found", Toast.LENGTH_LONG);
                        }
                    });
                    editText.getText().clear();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, WordActivity.class);
                intent.putExtra("word", words.get(position).first);
                intent.putExtra("definitions", words.get(position).second);
                startActivity(intent);
            }
        });

        return view;
    }

    private HashMap<String, List<String>> getDefinitions(JSONObject object) {
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
