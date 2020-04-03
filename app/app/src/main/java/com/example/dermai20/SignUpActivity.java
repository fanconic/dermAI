package com.example.dermai20;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dermai20.HttpConnector.*;
import static com.example.dermai20.R.style.ThemeOverlay_AppCompat_Dark;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    String BACKEND_URL = "";

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

    /**
     * Execute when this activity is being created.
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
        BACKEND_URL = getResources().getString(R.string.db_app) + getResources().getString(R.string.db_new_user);
        _signupButton.setOnClickListener(v -> signup());

        _loginLink.setOnClickListener(v -> {
            // Finish the registration screen and return to the Login activity
            finish();
        });
    }


    /**
     * Store the text informations into a JSON string and post the HTML request.
     * Furthermore, process the response.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
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
        String password = Utils.encrypt_password(_passwordText.getText().toString());
        int age = Integer.parseInt(_ageNumber.getText().toString());
        String country = _countryText.getText().toString();
        String city = _cityText.getText().toString();

        // Create JSON Object
        JSONObject json = new JSONObject();
        try {
            json.put("fname", fname);
            json.put("lname", lname);
            json.put("email", email);
            json.put("pwd", password);
            json.put("age",age);
            json.put("country", country);
            json.put("city", city);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Execute POST request
        String response_code = post(BACKEND_URL, json.toString());

        new android.os.Handler().postDelayed(
                () -> {
                    if(response_code.equals(getResources().getString(R.string.successfull_code))){
                        onSignupSuccess();
                        progressDialog.dismiss();
                    } else if(response_code.equals(getString(R.string.already_exists_code))) {
                        onAlreadyExists();
                        progressDialog.dismiss();
                    } else {
                        onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    /**
     * Action upon successful sign up.
     */
    public void onSignupSuccess() {
        setResult(RESULT_OK, null);
        finish();
    }

    /**
     * Action upon successful sign up.
     */
    public void onAlreadyExists() {
        Toast.makeText(getBaseContext(), R.string.account_exists_msg, Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    /**
     * Action upon failed sign up.
     */
    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    /**
     * Validate if the inputs of the signup form are valid.
     *
     * @return valid, True if all the inputs are valid
     */
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