package br.com.promobeerapp.connection

import com.google.gson.annotations.SerializedName

class RestGoogleResponse<T> {
    var results:T? = null
    @SerializedName("next_page_token")
    val nextPageToken:String = ""
}