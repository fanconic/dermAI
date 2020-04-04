package com.example.dermai20;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";
    private static final int TAKE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;

    @BindView(R.id.camera)
    Button _camera;
    @BindView(R.id.gallery)
    Button _gallery;


    /**
     * Execute when the Activity is created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);

        String fname = getIntent().getStringExtra("fname");
        String greetings = "Welcome " + fname + "!";
        TextView text = findViewById(R.id.welcome_text);
        text.setText(greetings);

        _camera.setOnClickListener(v -> {
            startCamera();
        });

        _gallery.setOnClickListener(v -> {
            startGallery();
        });
    }

    /**
     * Starts the camera
     */
    private void startCamera() {
        Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture, TAKE_IMAGE);
    }

    /**
     * Forwards to phone Gallery
     */
    private void startGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, PICK_IMAGE);
    }

    /**
     * Wait result of other activity (Gallery or Camera) and forward it to PictureActivity.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent(getApplicationContext(), PictureActivity.class);
        intent.putExtra("data", (Bitmap) data.getExtras().get("data"));
        intent.putExtra("requestCode", requestCode);
        intent.putExtra("resultCode", resultCode);
        startActivity(intent);
        this.finish();
    }

}
