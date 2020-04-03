package com.example.dermai20;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;


public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";

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
        TextView text = (TextView) findViewById(R.id.welcome_text);
        text.setText(greetings);

    }
}
