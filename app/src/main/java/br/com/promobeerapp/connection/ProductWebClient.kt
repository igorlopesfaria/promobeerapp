package br.com.promobeerapp.connection

import android.util.Log
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductType
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductWebClient {

    fun listBrands(callbackServiceResponse: CallbackServiceResponse<List<ProductBrand>>) {

        val call = RetrofitConfig().productService().listBrands()

        call.enqueue(object : Callback<RestResponse<List<ProductBrand>>?> {

            override fun onFailure(call: Call<RestResponse<List<ProductBrand>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<ProductBrand>>?>?,
                                    response: Response<RestResponse<List<ProductBrand>>?>?) {
                response?.body()?.let {
                    val restResponse: RestResponse<List<ProductBrand>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }

            }

        })
    }


    fun listTypeByBrandId(productBrand: ProductBrand, callbackServiceResponse: CallbackServiceResponse<List<ProductType>>) {
        val call = RetrofitConfig().productService().listTypeByBrandId(productBrand.id)

        call.enqueue(object : Callback<RestResponse<List<ProductType>>?> {

            override fun onFailure(call: Call<RestResponse<List<ProductType>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<ProductType>>?>?,
                                    response: Response<RestResponse<List<ProductType>>?>?) {
                response?.body()?.let {
                    val restResponse: RestResponse<List<ProductType>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }

            }

        })

    }
}