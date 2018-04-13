package br.com.promobeerapp.model.dao

import android.content.Context
import android.content.SharedPreferences
import br.com.promobeerapp.model.ProductBrand
import br.com.promobeerapp.model.ProductType
import com.google.gson.Gson
import br.com.promobeerapp.model.ProductSize


class Preffs (context: Context) {

    val PREFS_FILENAME = "br.com.promobeerapp.prefs"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    val PREFS_FILTER_BRAND = "br.com.promobeerapp.filter.brand.prefs"
    val PREFS_FILTER_TYPE = "br.com.promobeerapp.filter.type.prefs"
    val PREFS_FILTER_SIZE = "br.com.promobeerapp.filter.size.prefs"


    fun getBrandFilter():ProductBrand?{
        val json = prefs.getString(PREFS_FILTER_BRAND, "")
        if(!json.equals("\"\""))
            return Gson().fromJson<ProductBrand>(json, ProductBrand::class.java)
        else
            return null
        
    }

    fun setBrandFilter(productBrand:ProductBrand?){
        val prefsEditor = prefs.edit()
        if(productBrand != null)
            prefsEditor.putString(PREFS_FILTER_BRAND,  Gson().toJson(productBrand))
        else
            prefsEditor.putString(PREFS_FILTER_BRAND,  Gson().toJson(""))

        prefsEditor.apply()
    }

    fun getTypeFilter():ProductType?{
        val json = prefs.getString(PREFS_FILTER_TYPE, "")
        if(!json.equals("\"\""))
            return Gson().fromJson<ProductType>(json, ProductType::class.java)
        else
            return null

    }

    fun setTypeFilter(productType:ProductType?){
        val prefsEditor = prefs.edit()
        if(productType != null)
            prefsEditor.putString(PREFS_FILTER_TYPE,  Gson().toJson(productType))
        else
            prefsEditor.putString(PREFS_FILTER_TYPE,  Gson().toJson(""))

        prefsEditor.apply()
    }


    fun getSizeFilter():ProductSize?{
        val json = prefs.getString(PREFS_FILTER_SIZE, "")
        if(!json.equals("\"\""))
            return Gson().fromJson<ProductSize>(json, ProductSize::class.java)
        else
            return null

    }

    fun setSizeFilter(productSize: ProductSize?){
        val prefsEditor = prefs.edit()
        if(productSize != null)
            prefsEditor.putString(PREFS_FILTER_SIZE,  Gson().toJson(productSize))
        else
            prefsEditor.putString(PREFS_FILTER_SIZE,  Gson().toJson(""))

        prefsEditor.apply()
    }
}