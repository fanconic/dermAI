package com.example.dermai20;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.dermai20.R.style.ThemeOverlay_AppCompat_Dark;

public class SignUpActivity extends AppCompatActivity {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private static final String TAG = "SignUpActivity";
    String BACKEND_URL = "";
    TextView txtString;

    @BindView(R.id.input_fname)
    EditText _fnameText;
    @BindView(R.id.input_lname)
    EditText _lnameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.input_age)
    EditText _ageNumber;
    @BindView(R.id.input_country)
    EditText _countryText;
    @BindView(R.id.input_city)
    EditText _cityText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        BACKEND_URL = getResources().getString(R.string.backend_url);
        _signupButton.setOnClickListener(v -> signup());

        _loginLink.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            finish();
        });
    }

    void post(String url, String json_string) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json_string);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TAG",response.body().string());
            }
        });
    }

    public void signup() {
        Log.d(TAG, getResources().getString(R.string.sign_up));

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this, ThemeOverlay_AppCompat_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.creating_account));
        progressDialog.show();

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        int age = Integer.parseInt(_ageNumber.getText().toString());
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();

        MessageDigest digest = null;
        String password_encrypted = "";
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            password_encrypted = Base64.getEncoder().encodeToString(hash);
        }

        JSONObject json = new JSONObject();
        try {
            json.put("fname", fname);
            json.put("lname", lname);
            json.put("email", email);
            json.put("pwd", password_encrypted);
            json.put("age",age);
            json.put("country", country);
            json.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            post(BACKEND_URL, json.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        new android.os.Handler().postDelayed(
                () -> {
                    // On complete call either onSignupSuccess or onSignupFailed
                    // depending on success
                    onSignupSuccess();
                    // onSignupFailed();
                    progressDialog.dismiss();
                }, 3000);
    }

    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String fname = _fnameText.getText().toString();
        String lname = _lnameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        int age = Integer.parseInt(_ageNumber.getText().toString());
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();

        if (fname.isEmpty() || fname.length() < 3) {
            _fnameText.setError(getString(R.string.at_least_3_char));
            valid = false;
        } else {
            _lnameText.setError(null);
        }

        if (lname.isEmpty() || lname.length() < 2) {
            _lnameText.setError(getResources().getString(R.string.at_least_3_char));
            valid = false;
        } else {
            _lnameText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError(getResources().getString(R.string.valid_email));
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 10) {
            _passwordText.setError(getResources().getString(R.string.valid_pwd));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (age < 0 || age >= 130) {
            _ageNumber.setError(getResources().getString(R.string.invalid_age));
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        List<String> countries = Arrays.asList(getResources().getStringArray(R.array.countries_array));
        if (country.isEmpty() || !countries.contains(country)) {
            _countryText.setError(getResources().getString(R.string.not_country));
            valid = false;
        } else {
            _countryText.setError(null);
        }

        if (city.isEmpty() || city.length() < 2) {
            _cityText.setError(getResources().getString(R.string.at_least_3_char));
            valid = false;
        } else {
            _cityText.setError(null);
        }
        return valid;
    }
}