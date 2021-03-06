package com.example.dermai20;

import android.app.ProgressDialog;
import android.content.Intent;
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

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.dermai20.R.style.ThemeOverlay_AppCompat_Dark;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    String BACKEND_URL = "";

    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;

    /**
     * Execute when the Activity is created.
     *
     * @param savedInstanceState
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        BACKEND_URL = getResources().getString(R.string.db_app) + getResources().getString(R.string.db_login);
        _loginButton.setOnClickListener(v -> {
            try {
                login();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        _signupLink.setOnClickListener(v -> {
            // Start the Sign up activity
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivityForResult(intent, REQUEST_SIGNUP);
        });
    }

    /**
     * Login action, which creates the JSON packet and executes the POST request to the backend.
     * Furthermore, processes the response of the server.
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void login() throws JSONException {
        Log.d(TAG, getResources().getString(R.string.login));

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                ThemeOverlay_AppCompat_Dark);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage(getResources().getString(R.string.authenticating));
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = Utils.encrypt_password(_passwordText.getText().toString());

        // Create JSON Object
        JSONObject json = new JSONObject();
        try {
            json.put("email", email);
            json.put("pwd", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Execute POST request
        JSONObject response_json = HttpConnector.post(BACKEND_URL, json.toString());
        String response_code = response_json.get("status").toString();

        new android.os.Handler().postDelayed(
                () -> {
                    // If response if OK
                    if (response_code.equals(getResources().getString(R.string.successfull_code))) {
                        String fname = null;
                        try {
                            fname = response_json.get("fname").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        onLoginSuccess(fname);
                        progressDialog.dismiss();
                    } else {
                        onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 2000);
    }

    /**
     * Process successful login.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                onLoginSuccess(data.getStringExtra("fname"));
                this.finish();
            }
        }
    }

    /**
     * disable going back to the MainActivity.
     */
    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    /**
     * Action upon successful login, go to UserActivity
     *
     * @param fname : first name of user for greeting.
     */
    public void onLoginSuccess(String fname) {
        Toast.makeText(getBaseContext(), R.string.login_successfull, Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        intent.putExtra("fname", fname);
        startActivity(intent);
        this.finish();
    }

    /**
     * Action upon failed login.
     */
    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), getResources().getString(R.string.login_failed), Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    /**
     * Validate if the inputs are valid.
     *
     * @return boolean valid: true if all the inputs are valid.
     */
    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

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

        return valid;
    }
}