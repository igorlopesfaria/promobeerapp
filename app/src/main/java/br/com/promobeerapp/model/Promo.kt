package br.com.promobeerapp.model


/**
 * Created by root on 14/03/18.
 */
data class Promo(var id: Long,
                 var product: Product,
                 var address: Address,
                 var description: String,
                 var price: String,
                 var expiredDate: String,
                 var publishedDate: String
                 )
