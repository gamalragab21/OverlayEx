package com.example.overlayex.network.data

import com.google.gson.annotations.SerializedName

data class AppVersionDto(
    @SerializedName("userId") var userId: Int,
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("completed") var completed: Boolean,
)