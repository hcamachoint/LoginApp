package com.webkingve.loginapp.io

import com.webkingve.loginapp.io.response.LoginResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("token/")
    @FormUrlEncoded
    fun postLogin(@Field(value = "username") username: String, @Field(value = "password") password: String):
            Call<LoginResponse>

    companion object Factory{
        private const val BASE_URL = "http://10.0.2.2:8000/api/"
        fun create(): ApiService{
            val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()
            return retrofit.create(ApiService::class.java)
        }
    }
}