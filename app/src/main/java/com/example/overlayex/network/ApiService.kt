package com.example.overlayex.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface ApiService {

    @GET("{url}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun get(
        @Path(value = "url", encoded = true) url: String,
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = hashMapOf()
    ): Response<ResponseBody>

    @POST("{url}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun post(
        @Path(value = "url", encoded = true) url: String,
        @HeaderMap headerMap: Map<String, String>,
        @Body body: Any? = null,
        @QueryMap params: Map<String, String> = hashMapOf()
    ): Response<ResponseBody>


    @GET("{url}")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun <T> get2(
        @Path(value = "url", encoded = true) url: String,
        @HeaderMap headerMap: Map<String, String>,
        @QueryMap params: Map<String, String> = hashMapOf()
    ): T


}