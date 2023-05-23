package com.example.overlayex.network.data


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RequestPost(
    @SerializedName("body")
    @Expose
    var body: String,

    @SerializedName("id")
    @Expose
    var id: Int?=null,

    @Expose
    @SerializedName("title")
    var title: String,

    @Expose
    @SerializedName("userId")
    var userId: Int
){
    constructor(body: String,userId: Int,title: String):this(body,null,title,userId)
}