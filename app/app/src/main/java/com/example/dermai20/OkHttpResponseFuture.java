package com.example.dermai20;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.N)
public class OkHttpResponseFuture extends CompletableFuture<Response> implements Callback {
    public void onResponse(Call call, Response response) {
            super.complete(response);
    }

    public void onFailure(Call call, IOException e){
            super.completeExceptionally(e);
    }
}