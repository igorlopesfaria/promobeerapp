package br.com.promobeerapp.connection

class RestResponse<T> {
    val result:Boolean? = null
    var data:T? = null
    val error:Error? = null
}