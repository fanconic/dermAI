package com.example.dermai20;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureActivity extends AppCompatActivity {
    private static final String TAG = "PictureActivity";
    private static final int TAKE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;
    String BACKEND_URL = "";

    @BindView(R.id.inspect_picture)
    ImageView _inspectedPicture;
    @BindView(R.id.yes)
    Button _yes;
    @BindView(R.id.no)
    Button _no;


    /**
     * Execute when the Activity is created.
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);
        BACKEND_URL = getResources().getString(R.string.ml_app) + getString(R.string.predict);

        int requestCode = (int) getIntent().getExtras().get("requestCode");
        int resultCode = (int) getIntent().getExtras().get("resultCode");

        switch (requestCode) {
            case TAKE_IMAGE:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmap = (Bitmap) getIntent().getExtras().get("bitMapImage");
                    _inspectedPicture.setImageBitmap(bitmap);
                }
                break;
            case PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    String image_path = getIntent().getStringExtra("imagePath");
                    Uri imagePath = Uri.parse(image_path);

                    Bitmap bitmap = null;
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imagePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    _inspectedPicture.setImageBitmap(bitmap);
                }
                break;
        }

        _yes.setOnClickListener(v -> {
            try {
                approvePicture();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        _no.setOnClickListener(v -> {
            declinePicture();
        });
    }


    /**
     * Picture is approved
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void approvePicture() throws JSONException {
        Bitmap bitmap = ((BitmapDrawable) _inspectedPicture.getDrawable()).getBitmap();

        // Base64 encode image
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();
        String encoded_image = Base64.encodeToString(byteArray, Base64.DEFAULT);

        // Create JSON Object
        JSONObject json = new JSONObject();
        try {
            json.put("chat_id", 120);
            json.put("encoded_image", encoded_image);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Execute POST request
        JSONObject response_json = HttpConnector.post(BACKEND_URL, json.toString());
        String prediction = response_json.get("prediction").toString();
        float probability = Float.parseFloat(response_json.get("probability").toString());
        String response_code = response_json.get("status").toString();
    }

    /**
     * Picture is Declined
     */
    private void declinePicture() {
        this.finish();
    }
}

