package br.com.promobeerapp.connection

import android.util.Log
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.model.Product
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductSize
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


    fun listTypeByFilter(productBrand: ProductBrand, callbackServiceResponse: CallbackServiceResponse<List<ProductType>>) {
        val call = RetrofitConfig().productService().listTypeByFilter(productBrand.id)

        call.enqueue(object : Callback<RestResponse<List<ProductType>>?> {

            override fun onFailure(call: Call<RestResponse<List<ProductType>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<ProductType>>?>?,
                                    response: Response<RestResponse<List<ProductType>>?>?) {
                response?.body()?.let {
                    Log.d("","response.raw().request().url();"+response?.raw()?.request()?.url())
                    val restResponse: RestResponse<List<ProductType>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }

            }

        })

    }

    fun listType( callbackServiceResponse: CallbackServiceResponse<List<ProductType>>) {
        val call = RetrofitConfig().productService().listType()

        call.enqueue(object : Callback<RestResponse<List<ProductType>>?> {

            override fun onFailure(call: Call<RestResponse<List<ProductType>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<ProductType>>?>?,
                                    response: Response<RestResponse<List<ProductType>>?>?) {
                response?.body()?.let {
                    Log.d("","response.raw().request().url();"+response?.raw()?.request()?.url())
                    val restResponse: RestResponse<List<ProductType>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }

            }

        })

    }
    fun listSizeByFilter(productBrand: ProductBrand, productType: ProductType, callbackServiceResponse: CallbackServiceResponse<List<ProductSize>>) {
        val call = RetrofitConfig().productService().listSizeByFilter(productBrand.id, productType.id)

        call.enqueue(object : Callback<RestResponse<List<ProductSize>>?> {

            override fun onFailure(call: Call<RestResponse<List<ProductSize>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<ProductSize>>?>?,
                                    response: Response<RestResponse<List<ProductSize>>?>?) {
                response?.body()?.let {
                    val restResponse: RestResponse<List<ProductSize>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }

            }

        })

    }

    fun listSize( callbackServiceResponse: CallbackServiceResponse<List<ProductSize>>) {
        val call = RetrofitConfig().productService().listSize()

        call.enqueue(object : Callback<RestResponse<List<ProductSize>>?> {

            override fun onFailure(call: Call<RestResponse<List<ProductSize>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<ProductSize>>?>?,
                                    response: Response<RestResponse<List<ProductSize>>?>?) {
                response?.body()?.let {
                    val restResponse: RestResponse<List<ProductSize>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }

            }

        })

    }

    fun listByFilter(productBrand: ProductBrand, productType: ProductType, productSize: ProductSize, callbackServiceResponse: CallbackServiceResponse<List<Product>>) {
        val call = RetrofitConfig().productService().listByFilter(productBrand.id, productType.id, productSize.id)

        call.enqueue(object : Callback<RestResponse<List<Product>>?> {
            override fun onFailure(call: Call<RestResponse<List<Product>>?>?, t: Throwable?) {
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestResponse<List<Product>>?>?, response: Response<RestResponse<List<Product>>?>?) {
                response?.body()?.let {
                    val restResponse: RestResponse<List<Product>> = it
                    if (restResponse.data != null)
                        callbackServiceResponse.success(restResponse.data!!)
                }
            }
        })
    }
}