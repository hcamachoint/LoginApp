package com.webkingve.loginapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.webkingve.loginapp.R
import com.webkingve.loginapp.util.PreferenceHelper
import com.webkingve.loginapp.util.PreferenceHelper.set

class HomeActivity : AppCompatActivity() {

    private val preference by lazy { PreferenceHelper.defaultPrefs(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnLogout = findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            clearSessionPreference()
            goToLogin()
        }
    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun clearSessionPreference(){
        preference["token"] = ""
    }
}