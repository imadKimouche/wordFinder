package com.ignite.wordfinder;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.ignite.wordfinder.db.entity.DefinitionEntity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Search()).commit();
            bottomNavigationView.setSelectedItemId(R.id.Search);
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Home()).commit();
                break;
            case R.id.Search:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Search()).commit();
                break;
            case R.id.MyWords:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyWords()).commit();
                break;
        }
        return true;
    }
}
