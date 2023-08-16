package com.webkingve.loginapp.io

import com.webkingve.loginapp.io.response.LoginResponse
import com.webkingve.loginapp.io.response.RegisterResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @POST("token/")
    @FormUrlEncoded
    fun postLogin(@Field(value = "username") username: String, @Field(value = "password") password: String):
            Call<LoginResponse>

    @POST("register/")
    @FormUrlEncoded
    fun postRegister(@Field(value = "username") username: String, @Field(value = "password") password: String, @Field(value = "password2") password2: String, @Field(value = "email") email: String, @Field(value = "first_name") first_name: String, @Field(value = "last_name") last_name: String):
            Call<RegisterResponse>

    companion object Factory{
        private const val BASE_URL = "http://10.0.2.2:8000/api/"
        fun create(): ApiService{
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(ApiService::class.java)
        }
    }
}