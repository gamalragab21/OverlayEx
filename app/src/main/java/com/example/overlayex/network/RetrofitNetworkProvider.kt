package com.example.overlayex.network

import com.example.overlayex.toLog
import com.google.gson.Gson
import java.lang.reflect.Type

class RetrofitNetworkProvider constructor(val apiService: ApiService) : NetworkProvider {

    override suspend fun <B, T> post(
        lazz: Type,
        url: String,
        headers: Map<String, String>,
        params: Map<String, String>,
        body: B?
    ): T = try {

        val response = apiService.post(url, headers, body, params)
        response.body()!!.string().getModelFromString(lazz)!!
    } catch (e: Exception) {
        e.printStackTrace()
        throw java.lang.Exception("Failed to perform POSt request: ${e.message}")
    }

    override suspend fun <T> get(
        lazz: Type,
        url: String,
        headers: Map<String, String>,
        params: Map<String, String>
    ): T = try {
        val response = apiService.get(url, headers)
        response.body()!!.string().getModelFromString(lazz)!!

    } catch (e: Exception) {
        e.printStackTrace()
        throw java.lang.Exception("Failed to perform GET request: ${e.message}")
    }

    private fun <M> String.getModelFromString(clazz: Class<M>): M? {
        this.toLog()
        return Gson().fromJson(this, clazz)
    }

    private fun <M> String.getModelFromString(clazz: Type): M? {
        this.toLog()
        return Gson().fromJson(this, clazz)
    }
}
