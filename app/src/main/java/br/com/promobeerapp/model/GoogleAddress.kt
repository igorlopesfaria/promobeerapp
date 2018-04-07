package br.com.promobeerapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GoogleAddress(var id: String,
                         @SerializedName("formatted_address")
                         var googleGeometry: GoogleGeometry,
                         var name:String,
                         var vicinity:String) :Serializable