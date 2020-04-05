package com.example.dermai20;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import butterknife.ButterKnife;

public class ResultActivity extends AppCompatActivity {
    private static final String TAG = "ResultActivity";

    /**
     * Execute when the Activity is created.
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        ButterKnife.bind(this);

        String prediction = getIntent().getStringExtra("prediction");
        TextView text_prediction = findViewById(R.id.prediction_results_nr);
        text_prediction.setText(prediction);

        float probability = getIntent().getFloatExtra("probability", 0);
        TextView text_probability = findViewById(R.id.probability_results_nr);
        text_probability.setText(Float.toString(probability));
    }
}
