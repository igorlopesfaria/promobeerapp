package br.com.promobeerapp.model

import com.google.gson.annotations.SerializedName

/**
 * Created by root on 14/03/18.
 */
data class Product(var id: Long,
                   @SerializedName("brand")
                   var productBrand: ProductBrand,
                   @SerializedName("size")
                   var productSize: ProductSize,
                   @SerializedName("type")
                   var productType: ProductType,
                   var imagePath: String,
                   var checked:Boolean = false)