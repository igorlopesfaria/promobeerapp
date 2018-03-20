package br.com.promobeerapp.model

/**
 * Created by root on 14/03/18.
 */
data class Product(var id: Long,
                   var productBrand: ProductBrand,
                   var productSize: ProductSize,
                   var productType: ProductType,
                   var imagePath: String,
                   var checked:Boolean = false)