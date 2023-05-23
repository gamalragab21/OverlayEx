package com.example.overlayex.network.utils

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException


// Custom TypeAdapterFactory to handle generics
class GenericTypeAdapterFactory : TypeAdapterFactory {
    @Throws(IOException::class)
    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val rawType: Class<in T> = type.rawType as Class<in T>

        // Only apply custom handling for classes that are parameterized
        if (rawType.typeParameters.isNotEmpty()) {
            val typeAdapter = object : TypeAdapter<T>() {
                @Throws(IOException::class)
                override fun read(reader: JsonReader): T? {
                    if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull()
                        return null
                    }

                    val jsonObject =
                        gson.fromJson<LinkedTreeMap<String, Any>>(reader, LinkedTreeMap::class.java)
                    val json = gson.toJson(jsonObject)
                    return gson.fromJson(json, type.type)
                }

                @Throws(IOException::class)
                override fun write(writer: JsonWriter, value: T?) {
                    gson.toJson(value, type.type, writer)
                }
            }
            return typeAdapter.nullSafe()
        }

        return null
    }
}