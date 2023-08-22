package com.webkingve.loginapp.ui

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.webkingve.loginapp.R
import com.webkingve.loginapp.io.ApiService
import com.webkingve.loginapp.io.response.LoginResponse
import com.webkingve.loginapp.io.response.RegisterResponse
import org.json.JSONObject
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
        val etUsername = findViewById<EditText>(R.id.et_register_username)
        val tvUsernameMessage = findViewById<TextView>(R.id.tv_username_message)

        val etPassword = findViewById<EditText>(R.id.et_register_password)
        val tvPasswordMessage = findViewById<TextView>(R.id.tv_password_message)

        val etPassword2 = findViewById<EditText>(R.id.et_register_password2)
        val tvPassword2Message = findViewById<TextView>(R.id.tv_password2_message)

        val etEmail = findViewById<EditText>(R.id.et_register_email)
        val tvEmailMessage = findViewById<TextView>(R.id.tv_email_message)

        val etFirstName = findViewById<EditText>(R.id.et_register_first_name)
        val tvFirstNameMessage = findViewById<TextView>(R.id.tv_first_name_message)

        val etLastName = findViewById<EditText>(R.id.et_register_last_name)
        val tvLastNameMessage = findViewById<TextView>(R.id.tv_last_name_message)

        if(etUsername.text.toString().trim().isEmpty() || etPassword.text.toString().trim().isEmpty() || etPassword2.text.toString().trim().isEmpty() || etEmail.text.toString().trim().isEmpty() || etFirstName.text.toString().trim().isEmpty() || etLastName.text.toString().trim().isEmpty()){
            Toast.makeText(applicationContext, "Please fill all inputs", Toast.LENGTH_SHORT).show()
            return
        }

        showCustomProgressDialog()

        val call = apiService.postRegister(etUsername.text.toString(), etPassword.text.toString(), etPassword2.text.toString(), etEmail.text.toString(), etFirstName.text.toString(), etLastName.text.toString())
        call.enqueue(object: Callback<RegisterResponse> {
            override fun onResponse(call: retrofit2.Call<RegisterResponse>, response: Response<RegisterResponse>) {
                hideCustomProgressDialog()
                if (response.isSuccessful){
                    etUsername.text.clear();
                    etPassword.text.clear();
                    etPassword2.text.clear();
                    etEmail.text.clear();
                    etFirstName.text.clear();
                    etLastName.text.clear();
                    goToLogin()
                }else{
                    val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())

                    if(jsonObj.has("username")){
                        tvUsernameMessage.text = jsonObj.getJSONArray("username")[0].toString()
                        tvUsernameMessage.visibility = View.VISIBLE
                    }else{tvUsernameMessage.visibility = View.GONE}

                    if(jsonObj.has("email")){
                        tvEmailMessage.text = jsonObj.getJSONArray("email")[0].toString()
                        tvEmailMessage.visibility = View.VISIBLE
                    }else{tvEmailMessage.visibility = View.GONE}

                    if(jsonObj.has("password")){
                        tvPasswordMessage.text = jsonObj.getJSONArray("password")[0].toString()
                        tvPasswordMessage.visibility = View.VISIBLE
                    }else{tvPasswordMessage.visibility = View.GONE}

                    if(jsonObj.has("password2")){
                        tvPassword2Message.text = jsonObj.getJSONArray("password")[0].toString()
                        tvPassword2Message.visibility = View.VISIBLE
                    }else{tvPassword2Message.visibility = View.GONE}

                    Toast.makeText(applicationContext, "An error occurred on the server", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: retrofit2.Call<RegisterResponse>, t: Throwable) {
                hideCustomProgressDialog()
                //Toast.makeText(applicationContext, "An error occurred on the server", Toast.LENGTH_SHORT).show()
                Log.i("Response API", t.toString())
            }

        })
    }
}