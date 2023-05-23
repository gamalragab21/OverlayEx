package com.example.overlayex.network

import java.lang.reflect.Type

interface NetworkProvider {

    suspend fun <B, T> post(
        lazz: Type,
        url: String,
        headers: Map<String, String>,
        params: Map<String, String>,
        body: B? = null
    ): T

    suspend fun <T> get(
        lazz: Type,
        url: String,
        headers: Map<String, String>,
        params: Map<String, String>
    ): T

}
