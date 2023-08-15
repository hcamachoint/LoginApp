package com.webkingve.loginapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.webkingve.loginapp.R
import com.webkingve.loginapp.io.ApiService
import com.webkingve.loginapp.io.response.LoginResponse
import com.webkingve.loginapp.util.PreferenceHelper
import com.webkingve.loginapp.util.PreferenceHelper.get
import com.webkingve.loginapp.util.PreferenceHelper.set
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if (preferences["token", ""].contains(".")){
            goToMenu()
        }

        val tvGoRegister = findViewById<TextView>(R.id.tv_go_to_register)
        tvGoRegister.setOnClickListener{
            goToRegister()
        }

        val btnGoMenu = findViewById<Button>(R.id.btn_go_to_menu)
        btnGoMenu.setOnClickListener {
            performLogin()
        }
    }

   private fun goToRegister(){
       val i = Intent(this, RegisterActivity::class.java)
       startActivity(i)
   }

    private fun goToMenu(){
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun createSessionPreference(token: String){
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["token"] = token
    }

    private fun performLogin(){
        val etUsuario = findViewById<EditText>(R.id.et_usuario).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

        if(etUsuario.trim().isEmpty() || etPassword.trim().isEmpty()){
            Toast.makeText(applicationContext, "Ingresa un nombre de usuario y contrasena", Toast.LENGTH_SHORT).show()
            return
        }

        val call = apiService.postLogin(etUsuario, etPassword)
        call.enqueue(object: Callback<LoginResponse> {
            override fun onResponse(call: retrofit2.Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful){
                    val loginResponse = response.body()
                    if (loginResponse == null){
                        Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }else if(loginResponse.access != ""){
                        createSessionPreference(loginResponse.access)
                        goToMenu()
                    }else{
                        Toast.makeText(applicationContext, "Las credenciales son incorrectas", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                Log.i("Network failed", t.toString())
            }

        })
    }
}