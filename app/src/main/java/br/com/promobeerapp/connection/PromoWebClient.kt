package br.com.promobeerapp.connection

import android.util.Log
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.model.*
import okhttp3.MediaType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import okhttp3.RequestBody
import java.io.File
import android.R.attr.password
import android.content.Context
import br.com.promobeerapp.R
import retrofit2.http.Part


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
    fun insert(promo: Promo, callbackServiceResponse: CallbackServiceResponse<Promo>, context: Context) {

        val idProduct = RequestBody.create(MediaType.parse("text/plain"), promo.product.id.toString())
        val price = RequestBody.create(MediaType.parse("text/plain"), promo.price)
        val expiredDate = RequestBody.create(MediaType.parse("text/plain"), promo.expiredDate)
        val idPlace = RequestBody.create(MediaType.parse("text/plain"), promo.establishment.gmPlaceId)
        val description = RequestBody.create(MediaType.parse("text/plain"), promo.description)
        val file = RequestBody.create(MediaType.parse("image/jpeg"), promo.file)

        val call = RetrofitConfig().promoService().insert(idProduct, price,expiredDate,idPlace,description,file )

        call.enqueue(object : Callback<RestResponse<Promo>?> {
            override fun onResponse(call: Call<RestResponse<Promo>?>?, response: Response<RestResponse<Promo>?>?) {
                response?.body()?.let {
                    Log.d("","response.raw().request().url();"+response.raw()?.request()?.url())

                    val restResponse: RestResponse<Promo> = it
                    if (restResponse.data != null &&  restResponse.result!!) {
                        callbackServiceResponse.success(restResponse.data!!)
                    }else{

                        var msg = context.getString(R.string.promo_problem_registered)

                        if(restResponse.error?.code == 409) {
                            msg =  context.getString(R.string.promo_already_registered)
                        }

                        val t = Throwable(msg)
                        callbackServiceResponse.fail(t)

                    }

                }
            }

            override fun onFailure(call: Call<RestResponse<Promo>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

        })
    }
}



