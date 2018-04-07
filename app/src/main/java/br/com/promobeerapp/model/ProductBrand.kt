package br.com.promobeerapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Created by root on 14/03/18.
 */
data class ProductBrand(var id: Long,
                        var name: String,
                        @SerializedName("image_path")
                        var imagePath:String,
                        var selected:Boolean = false):Serializable
