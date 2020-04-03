package com.example.dermai20;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpConnector extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "HttpConnector";

    /**
     * Performs a HTML POST request to the backend.
     *
     * @param url:         backend URL
     * @param json_string: body of the POST request
     * @return code: code of successful post
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static JSONObject post(String url, String json_string) throws NullPointerException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json_string);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        OkHttpResponseFuture future = new OkHttpResponseFuture();
        client.newCall(request).enqueue(future);

        Response response = null;
        try {
            response = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        String result;
        JSONObject json_response = null;

        try {
            result = response.body().string();
            json_response = new JSONObject(result);
            Log.d("TAG", "JSON Return Response: " + json_response.get("status").toString());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return json_response;
    }
}
