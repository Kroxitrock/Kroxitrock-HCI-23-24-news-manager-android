package es.upm.reader.news.model

import com.google.gson.annotations.SerializedName

class User(
    val username: String,
    @SerializedName("apikey")
    val apiKey: String
)
