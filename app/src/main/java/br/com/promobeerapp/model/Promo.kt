package br.com.promobeerapp.model

import android.annotation.SuppressLint
import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.File
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by root on 14/03/18.
 */
data class Promo(var id: Long?,
                 var product: Product,
                 var description: String,
                 var price: String,
                 @SerializedName("product_valid_until")
                 var expiredDate: String,
                 @SerializedName("published_at")
                 var publishedDate: String?,
                 var establishment: Establishment,
                 var file: File): Serializable {

    @SuppressLint("SimpleDateFormat")
    fun getExpiredDateFormatted():String{
        val sdfSource = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        val sdfDestination = SimpleDateFormat("dd/MM/yyyy");

        // parse the date into another format
        return sdfDestination.format(sdfSource.parse(expiredDate));
    }

    @SuppressLint("SimpleDateFormat")
    fun getPublishedDateFormatted():String{
        val sdfSource = SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        val sdfDestination = SimpleDateFormat("dd/MM/yyyy hh:mm");

        // parse the date into another format
        return sdfDestination.format(sdfSource.parse(publishedDate));
    }

}
