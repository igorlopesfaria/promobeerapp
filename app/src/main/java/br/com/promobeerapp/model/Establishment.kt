package br.com.promobeerapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Establishment(var id: Long,
                         var latitude: String,
                         var longitude: String,
                         @SerializedName("gm_place_id")
                         var gmPlaceId: String,
                         var name:String,
                         var vicinity: String):Serializable {

}

