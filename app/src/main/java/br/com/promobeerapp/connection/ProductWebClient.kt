package br.com.promobeerapp.connection

import android.util.Log
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.model.ProductBrand
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductWebClient {

    fun listBrands(callbackServiceResponse: CallbackServiceResponse<List<ProductBrand>>) {

        val call = RetrofitConfig().productBrandService().listBrands()

        call.enqueue(object : Callback<RestResponse<List<ProductBrand>>?> {

            override fun onFailure(call: Call<RestResponse<List<ProductBrand>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<ProductBrand>>?>?,
                                    response: Response<RestResponse<List<ProductBrand>>?>?) {
                response?.body()?.let {
                    val restResponse: RestResponse<List<ProductBrand>> = it
                    if(restResponse.data!=null)
                        callbackServiceResponse.success(restResponse.data!!)
                }

            }

        })
    }
}