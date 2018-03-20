package br.com.promobeerapp.model

/**
 * Created by root on 14/03/18.
 */
data class ProductSize(var id: Long,
                       var value: String,
                       var material: String,
                       var imagePath:String,
                       var checked:Boolean = false)