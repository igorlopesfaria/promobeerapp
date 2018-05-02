package br.com.promobeerapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class GoogleAddress(var id: String,
                         @SerializedName("geometry")
                         var googleGeometry: GoogleGeometry,
                         var name:String,
                         @SerializedName("place_id")
                         var placeId:String,
                         var vicinity:String) :Serializable