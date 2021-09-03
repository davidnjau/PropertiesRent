package com.dave.properties.network

import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface Interface {

    @POST("/api/v1/properties/add_property")
    fun addProperty(@Body property: Properties): Call<Any>

    @PUT("/api/v1/properties/update_property")
    fun updateProperty(@Body property: Properties): Call<Any>

    @GET("/api/v1/receipts/update_receipts")
    fun get(@Body property: Properties): Call<Any>


}