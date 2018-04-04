package br.com.promobeerapp.model

import java.io.Serializable

/**
 * Created by root on 14/03/18.
 */
data class ProductType(var id: Long,
                       var name: String,
                       var imagePath:String,
                       var selected:Boolean = false):Serializable