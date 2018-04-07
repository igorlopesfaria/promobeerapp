package br.com.promobeerapp.connection

import br.com.promobeerapp.model.GoogleAddress
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductSize
import br.com.promobeerapp.model.ProductType
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleService {

    @GET("maps/api/place/nearbysearch/json")
    fun searchByDistance(@Query("location") location:String,
                          @Query("rankby") rankby:String,
                         @Query("types") types:String,
                         @Query("key") key:String) : Call<RestGoogleResponse<List<GoogleAddress>>>

}

