package com.example.dermai20;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

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
        this.finish();
    }
}

