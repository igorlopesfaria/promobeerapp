package br.com.promobeerapp.connection

import br.com.promobeerapp.model.*
import retrofit2.Call
import okhttp3.RequestBody
import retrofit2.http.*


interface PromoService {

    @GET("promotions")
    fun list(@Query("limit") limit:Int,
             @Query("order") order:String,
             @Query("orderDirection") orderDirection:String
             ) : Call<RestResponse<List<Promo>>>

    @GET("promotions")
    fun listByFilter(@Query("limit") limit:Int,
                     @Query("order") order:String,
                     @Query("orderDirection") orderDirection:String,
                     @Query("brand") idBrand:Long,
                     @Query("type") idType:Long,
                     @Query("size") idSize:Long
                     ) : Call<RestResponse<List<Promo>>>

    @Multipart
    @POST("promotions")
    fun insert(@Part("product_id") idProduct: RequestBody,@Part("price") price: RequestBody,@Part("product_valid_until") expiredDate: RequestBody, @Part("place_id") idPlace: RequestBody, @Part("description") description: RequestBody, @Part("image\"; filename=\"photo_promo.jpg\" ") file: RequestBody): Call<RestResponse<Promo>>
}