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

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val txtUserName = findViewById<View>(R.id.txtUsername) as EditText
        val txtPassword = findViewById<View>(R.id.txtPassword) as EditText
        val btnLogin = findViewById<View>(R.id.btnLogin) as Button
        val btnRegister = findViewById<View>(R.id.btnRegister) as Button

        btnLogin.setOnClickListener {
            val username = txtUserName.text.toString()
            val password = txtPassword.text.toString()
            try {

                if (username.isNotEmpty() && password.isNotEmpty()) {
                    val dbUser = DBUserAdapter(this@LoginActivity)
                    dbUser.open()

                    if (dbUser.Login(username, password)) {
                        Toast.makeText(this@LoginActivity, "Successfully Logged In", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    }

                    else {
                        Toast.makeText(this@LoginActivity, "Invalid Username/Password", Toast.LENGTH_LONG).show()
                    }
                    dbUser.close()
                }

            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_LONG).show()
            }
        }

        btnRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}