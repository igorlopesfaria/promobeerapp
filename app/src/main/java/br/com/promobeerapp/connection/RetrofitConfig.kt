package br.com.promobeerapp.connection

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitConfig {

    private val retrofit = Retrofit.Builder()
            .baseUrl("http://promobeer.pazzaro.com.br/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    fun productService(): ProductService  = retrofit.create(ProductService::class.java)

}