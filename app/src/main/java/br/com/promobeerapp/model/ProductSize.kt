package br.com.promobeerapp.model

import java.io.Serializable

/**
 * Created by root on 14/03/18.
 */
data class ProductSize(var id: Long,
                       var value: String,
                       var material: String,
                       var imagePath:String):Serializable