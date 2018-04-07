package br.com.promobeerapp.connection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitGoogleConfig {

    private val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun googleService(): GoogleService  = retrofit.create(GoogleService::class.java)

}