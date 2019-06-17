package com.ignite.wordfinder;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

final class HttpRequest {
    private static final String BASE_URL = "https://googledictionaryapi.eu-gb.mybluemix.net/";
    private static AsyncHttpClient client = new AsyncHttpClient();


    private static String getUrl(String word) {
        return BASE_URL + "?define=" + word + "&lang=fr";
    }


    public static void find(String word, AsyncHttpResponseHandler handler) {
        client.get(getUrl(word), handler);
    }

}