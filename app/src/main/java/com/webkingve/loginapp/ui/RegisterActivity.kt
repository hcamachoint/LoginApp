package com.webkingve.loginapp.ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.webkingve.loginapp.R
import com.webkingve.loginapp.io.ApiService
import com.webkingve.loginapp.io.response.LoginResponse
import com.webkingve.loginapp.io.response.RegisterResponse
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private var mProgressDialog: Dialog? = null
    private val apiService: ApiService by lazy{
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btRegister = findViewById<Button>(R.id.bt_register_send)
        btRegister.setOnClickListener {
            performRegister()
        }

        val tvGoLogin = findViewById<TextView>(R.id.tv_go_to_login)
        tvGoLogin.setOnClickListener{
            goToLogin()
        }
    }

    private fun goToLogin(){
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
    }

    private fun showCustomProgressDialog(){
        mProgressDialog = Dialog(this)
        mProgressDialog!!.setContentView(R.layout.dialog_custom_progress)
        mProgressDialog!!.show()
    }

    private fun hideCustomProgressDialog(){
        if(mProgressDialog != null){
            mProgressDialog!!.dismiss()
        }
    }

    private fun performRegister(){
        val etUsername = findViewById<EditText>(R.id.et_register_username).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_register_password).text.toString()
        val etPassword2 = findViewById<EditText>(R.id.et_register_password2).text.toString()
        val etEmail = findViewById<EditText>(R.id.et_register_email).text.toString()
        val etFirstName = findViewById<EditText>(R.id.et_register_first_name).text.toString()
        val etLastName = findViewById<EditText>(R.id.et_register_last_name).text.toString()

        if(etUsername.trim().isEmpty() || etPassword.trim().isEmpty() || etPassword2.trim().isEmpty() || etEmail.trim().isEmpty() || etFirstName.trim().isEmpty() || etLastName.trim().isEmpty()){
            Toast.makeText(applicationContext, "Please fill all inputs", Toast.LENGTH_SHORT).show()
            return
        }

        showCustomProgressDialog()

        val call = apiService.postRegister(etUsername, etPassword, etPassword2, etEmail, etFirstName, etLastName)
        call.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(call: retrofit2.Call<RegisterResponse>, response: Response<RegisterResponse>) {
                hideCustomProgressDialog()
                Log.i("Network failed", response.toString())
                if (response.isSuccessful){
                    val registerResponse = response.body()
                    if (registerResponse == null){
                        Toast.makeText(applicationContext, "An error occurred on the server", Toast.LENGTH_SHORT).show()
                        return
                    }else if(registerResponse.username != ""){
                        goToLogin()
                    }else{
                        Toast.makeText(applicationContext, "The credentials are wrong", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(applicationContext, "An error occurred on the server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<RegisterResponse>, t: Throwable) {
                hideCustomProgressDialog()
                Toast.makeText(applicationContext, "An error occurred on the server", Toast.LENGTH_SHORT).show()
                Log.i("Network failed", t.toString())
            }

        })
    }
}