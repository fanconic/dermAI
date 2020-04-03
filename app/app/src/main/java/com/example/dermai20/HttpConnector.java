package com.example.dermai20;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpConnector extends AppCompatActivity {
    private static final String TAG = "HttpConnector";
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     *  Performs a HTML POST request to the backend.
     *
     * @param url: backend URL
     * @param json_string: body of the POST request
     * @return code: code of successful post
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String post(String url, String json_string){
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
        JSONObject json_response;
        String code = "FAIL";

        try {
            result = response.body().string();
            json_response = new JSONObject(result);
            code = json_response.get("status").toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        Log.d("TAG", "JSON Return Response: " + code);
        return code;
    }
}
