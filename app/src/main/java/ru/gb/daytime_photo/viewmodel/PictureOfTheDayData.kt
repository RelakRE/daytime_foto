package ru.gb.daytime_photo.viewmodel

import ru.gb.daytime_photo.model.retrofits.nasa_apod.PODNasaAPOD

sealed class PictureOfTheDayData {
    data class SuccessDayPhoto(val serverResponseData: PODNasaAPOD) :
        PictureOfTheDayData()

    data class Error(val error: Throwable) : PictureOfTheDayData()
    data class Loading(val progress: Int?) : PictureOfTheDayData()
}

sealed class PictureOfEarthData {
    data class SuccessEarthPhoto(val url: String) :
        PictureOfEarthData()

    data class Error(val error: Throwable) : PictureOfEarthData()
    data class Loading(val progress: Int?) : PictureOfEarthData()
}