package com.ignite.wordfinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;


public class Home extends Fragment {

    TextView wordTv;
    TextView definitionTv;
    Button addButton;
    private AdView mAdView;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("4D2FA927DC6D5F6A452EB6D8020BA721").build();
        mAdView.loadAd(adRequest);

        wordTv = view.findViewById(R.id.word);
        definitionTv = view.findViewById(R.id.definition);
        addButton = view.findViewById(R.id.add);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            definitionTv.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);
        }

        try {
            JSONObject json = new JSONObject(loadJSONFromAsset());
            JSONArray names = json.names();
            String name = names.getString(new Random().nextInt(json.length()));

            wordTv.setText(name);
            definitionTv.setText(json.getString(name));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        return view;

    }


    public String loadJSONFromAsset() {
        String json;
        try {
            InputStream is = getContext().getResources().openRawResource(R.raw.data);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
