package ru.gb.daytime_photo.model.retrofits.nasa_apod


import com.google.gson.annotations.SerializedName

data class PODEpicMetadataItem(
    @SerializedName("date")
    val date: String,
    @SerializedName("identifier")
    val identifier: String,
    @SerializedName("image")
    val image: String,
)