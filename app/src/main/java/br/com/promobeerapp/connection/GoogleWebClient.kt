package br.com.promobeerapp.connection

import android.util.Log
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.model.GoogleAddress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GoogleWebClient {


    fun searchByDistance(location:String, rankby:String, types:String, key:String,
                   callbackServiceResponse: CallbackServiceResponse<List<GoogleAddress>>) {

        val call = RetrofitGoogleConfig().googleService().searchByDistance(location,rankby,types,key)

        call.enqueue(object : Callback<RestGoogleResponse<List<GoogleAddress>>?> {

            override fun onFailure(call: Call<RestGoogleResponse<List<GoogleAddress>>?>?, t: Throwable?) {

                Log.e("",t.toString())
                callbackServiceResponse.fail(t!!)
            }

            override fun onResponse(call: Call<RestGoogleResponse<List<GoogleAddress>>?>?,
                                    response: Response<RestGoogleResponse<List<GoogleAddress>>?>?) {

                Log.d("","response.raw().request().url();"+response?.raw()?.request()?.url())
                response?.body()?.let {
                    val restResponse: RestGoogleResponse<List<GoogleAddress>> = it
                    if (restResponse.results != null)
                        callbackServiceResponse.success(restResponse.results!!)
                }

            }

        })
    }



}