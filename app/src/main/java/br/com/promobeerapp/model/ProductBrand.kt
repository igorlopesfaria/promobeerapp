package br.com.promobeerapp.model

/**
 * Created by root on 14/03/18.
 */
data class ProductBrand(var id: Long,
                        var name: String,
                        var imagePath:String,
                        var checked:Boolean = false)
