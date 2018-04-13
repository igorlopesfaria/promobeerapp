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
                    Log.d("","response.raw().request().url();"+response?.raw()?.request()?.url())

                    val restResponse: RestResponse<List<Promo>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }
            }
        })
    }
    var productBrandFiltered: ProductBrand? = null
    var productTypeFiltered: ProductType? = null
    var productSizeFiltered: ProductSize? = null
    fun listByFilter(productBrand: ProductBrand? ,productType: ProductType?, productSize: ProductSize?, callbackServiceResponse: CallbackServiceResponse<List<Promo>>) {

        if(productBrand==null) {
            productBrandFiltered = ProductBrand(0, "", "", false)
        }else {
            productBrandFiltered = productBrand
        }

        if(productType==null) {
            productTypeFiltered = ProductType(0, "", "", false)
        }else {
            productTypeFiltered = productType
        }

        if(productSize==null) {
            productSizeFiltered = ProductSize(0, "", "", "")
        }else{
            productSizeFiltered = productSize

        }

        val call = RetrofitConfig().promoService().listByFilter(50, "created_at", "desc", productBrandFiltered!!.id, productTypeFiltered!!.id, productSizeFiltered!!.id)
        call.enqueue(object : Callback<RestResponse<List<Promo>>?> {
            override fun onFailure(call: Call<RestResponse<List<Promo>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<Promo>>?>?, response: Response<RestResponse<List<Promo>>?>?) {
                response?.body()?.let {
                    Log.d("","response.raw().request().url();"+response?.raw()?.request()?.url())

                    val restResponse: RestResponse<List<Promo>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }
            }
        })
    }

}