package com.example.fanconic.dermAI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.fanconic.dermAI.retrofit.DBUserAdapter
import com.example.fanconic.dermAI.R
import kotlinx.android.synthetic.main.login.*


class RegisterActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val txtFirstName = findViewById<View>(R.id.txtFirstName) as EditText
        val txtSurname = findViewById<View>(R.id.txtSurname) as EditText
        val txtCountry = findViewById<View>(R.id.txtCountry) as EditText
        val txtUserName = findViewById<View>(R.id.txtUsername) as EditText
        val txtPassword = findViewById<View>(R.id.txtPassword) as EditText
        val txtPasswordConfirm = findViewById<View>(R.id.txtPasswordConfirm) as EditText
        val btnRegister2 = findViewById<View>(R.id.btnRegister2) as Button

        btnRegister2.setOnClickListener {
            val firstname = txtFirstName.text.toString()
            val surname = txtSurname.text.toString()
            val country = txtCountry.text.toString()
            val username = txtUserName.text.toString()
            val password = txtPassword.text.toString()
            val confirm = txtPasswordConfirm.text.toString()
            /*
            try {
                val notEmpty = username.isNotEmpty() && password.isNotEmpty() && firstname.isNotEmpty() && surname.isNotEmpty() && country.isNotEmpty() && confirm.isNotEmpty()
                val passwordCorrect = password == confirm
                if ( notEmpty && passwordCorrect) {

                    //TODO: Implement new User
                    val dbUser = DBUserAdapter(this@LoginActivity)
                    dbUser.open()

                    if (dbUser.Login(username, password)) {
                        Toast.makeText(this@LoginActivity, "Successfully Logged In", Toast.LENGTH_LONG).show()
                    }

                    else {
                        Toast.makeText(this@LoginActivity, "Invalid Username/Password", Toast.LENGTH_LONG).show()
                    }
                    dbUser.close()
                }

            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
            }
             */
        }


        btnRegister.setOnClickListener {
            val intent = Intent(this@RegisterActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}