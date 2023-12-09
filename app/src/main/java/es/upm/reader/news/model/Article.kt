package es.upm.reader.news.model

import com.google.gson.annotations.SerializedName

data class Article(
    val id: Int?,
    val title: String,
    val subtitle: String?,
    val category: Category?,
    val abstract: String,
    val body: String?,
    val username: String?,
    @SerializedName("update_date")
    val updateDate: String?,
    @SerializedName("image_data")
    val imageData: String?,
    @SerializedName("image_media_type")
    val imageMediaType: String?,
    @SerializedName("thumbnail_image")
    val thumbnailImage: String?,
    @SerializedName("thumbnail_media_type")
    val thumbnailMediaType: String?
)