package com.example.fanconic.dermAI

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.example.fanconic.dermAI.R


class SplashActivity : Activity() {

    internal lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashfile)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 3000)

    }
}