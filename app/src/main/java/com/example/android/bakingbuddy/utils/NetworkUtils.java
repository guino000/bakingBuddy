package com.example.android.bakingbuddy.utils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public final class NetworkUtils {
    public static String getResponseFromHttpUrl(String url) throws IOException{
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(!response.isSuccessful()){
            throw new IOException("Unexpected code " + response);
        }
        return response.toString();
    }
}
