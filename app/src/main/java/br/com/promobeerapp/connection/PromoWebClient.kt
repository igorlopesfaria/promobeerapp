package br.com.promobeerapp.connection

import android.util.Log
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PromoWebClient {


    fun list( callbackServiceResponse: CallbackServiceResponse<List<Promo>>) {
        val call = RetrofitConfig().promoService().list(50, "created_at", "desc")
        call.enqueue(object : Callback<RestResponse<List<Promo>>?> {
            override fun onFailure(call: Call<RestResponse<List<Promo>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<Promo>>?>?, response: Response<RestResponse<List<Promo>>?>?) {
                response?.body()?.let {
                    val restResponse: RestResponse<List<Promo>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }
            }
        })
    }



}