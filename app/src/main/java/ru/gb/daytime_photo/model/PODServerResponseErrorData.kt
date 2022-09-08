package ru.gb.daytime_photo.model

import com.google.gson.annotations.SerializedName

data class PODServerResponseErrorData(
    @field:SerializedName("code") val code: Int?,
    @field:SerializedName("msg") val msg: String?,
    @field:SerializedName("service_version") val serviceVersion: String?
)
