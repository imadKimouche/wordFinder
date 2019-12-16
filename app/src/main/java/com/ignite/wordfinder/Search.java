package com.ignite.wordfinder;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ignite.wordfinder.db.entity.DefinitionEntity;
import com.ignite.wordfinder.db.entity.WordEntity;
import com.ignite.wordfinder.viewmodel.WordListViewModel;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import cz.msebera.android.httpclient.Header;

import static android.app.Activity.RESULT_OK;


public class Search extends Fragment {

    List<WordEntity> wordList = new ArrayList<>();
    List<DefinitionEntity> definitions = new ArrayList<>();
    HashMap<Integer, List<DefinitionEntity>> wordDefinitions = new HashMap<>();
    ExpandableListView wordsLv;
    ExpandableListAdapter adapter;
    Button findButton;
    ImageButton voiceButton;
    EditText editText;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);


        final WordListViewModel viewModel =
                ViewModelProviders.of(this).get(WordListViewModel.class);
        wordsLv = view.findViewById(R.id.wordsLv);
        findButton = view.findViewById(R.id.findButton);
        voiceButton = view.findViewById(R.id.voice);
        editText = view.findViewById(R.id.editText);
        adapter = new WordAdapter(getContext(), wordList, wordDefinitions);
        wordsLv.setAdapter(adapter);
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String word = editText.getText().toString();
                if (!word.isEmpty()) {
                    HttpRequest.find(word, new AsyncHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            WordEntity wordObj = new WordEntity(word);
                            wordList.add(wordObj);
                            try {
                                Long idLong = viewModel.insert(wordObj);
                                int id = idLong == null ? 0 : idLong.intValue();
                                wordObj.setId(id);
                                definitions = getDefinitions(new JSONObject(new String(responseBody)).getJSONObject("meaning"), viewModel.getWordByName(word).getId());
                                for (int i = 0; i < definitions.size(); i++) {
                                    DefinitionEntity definition = definitions.get(i);
                                    viewModel.insert(definition);
                                }
                                wordDefinitions.put(wordObj.getId(), definitions);
                                ((WordAdapter) adapter).notifyDataSetChanged();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.i("failed", Arrays.toString(responseBody));
                            Toast.makeText(getContext(), "Word not found", Toast.LENGTH_LONG).show();
                        }
                    });
                    editText.getText().clear();
                }
            }
        });

        voiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSpeechToText();
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

    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, 1);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String text = result.get(0);
                    editText.setText(text);
                }
                break;
            }
        }
    }
}
