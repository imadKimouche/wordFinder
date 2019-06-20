package com.ignite.wordfinder;

import android.arch.lifecycle.ViewModelProviders;
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

import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;
import com.ignite.wordfinder.model.Word;
import com.ignite.wordfinder.viewmodel.WordListViewModel;
import com.ignite.wordfinder.viewmodel.WordViewModel;
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
    List<WordEntity> wordList = new ArrayList<>();
    List<DefinitionEntity> definitions = new ArrayList<>();
    ListView listView;
    Button findButton;
    EditText editText;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        mContext = inflater.getContext();


        final WordListViewModel viewModel =
                ViewModelProviders.of(this).get(WordListViewModel.class);
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
                            wordList.add(new WordEntity(word));
                            adapter.notifyDataSetChanged();
                            try {
                                viewModel.insert(new WordEntity(word));
                                definitions = getDefinitions(new JSONObject(new String(responseBody)).getJSONObject("meaning"), viewModel.getWordByName(word).getId());
                                viewModel.insert(definitions);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.i("failed", Arrays.toString(responseBody));
                            Toast.makeText(mContext, "Word not found", Toast.LENGTH_LONG).show();
                        }
                    });
                    editText.getText().clear();
                }
            }
        });

        return view;
    }

    private List<DefinitionEntity> getDefinitions(JSONObject object, int wordId) {
        List<DefinitionEntity> definitions = new ArrayList<>();
        ;
        try {
            Iterator<String> keys = object.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray array = object.getJSONArray(key);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject def = array.getJSONObject(i);
                    definitions.add(new DefinitionEntity(wordId, def.getString("definition"), key));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return definitions;
    }

}
