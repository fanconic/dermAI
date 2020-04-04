package com.example.dermai20;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PictureActivity extends AppCompatActivity {
    private static final String TAG = "PictureActivity";
    private static final int TAKE_IMAGE = 0;
    private static final int PICK_IMAGE = 1;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        ButterKnife.bind(this);

        int requestCode = (int) getIntent().getExtras().get("requestCode");
        int resultCode = (int) getIntent().getExtras().get("resultCode");

        switch (requestCode) {
            case TAKE_IMAGE:
                if (resultCode == RESULT_OK) {
                    Bitmap selectedImage = (Bitmap) getIntent().getExtras().get("data");
                    _inspectedPicture.setImageBitmap(selectedImage);
                }
                break;
        }

        _yes.setOnClickListener(v -> {
            approvePicture();
        });

        _no.setOnClickListener(v -> {
            declinePicture();
        });
    }

    /**
     * Picture is approved
     */
    private void approvePicture() {

    }

    /**
     * Picture is Declined
     */
    private void declinePicture() {

    }
}

