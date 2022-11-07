package ru.gb.daytime_photo.model.retrofits.nasa_apod

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.gb.daytime_photo.BuildConfig

interface PictureOfTheDayAPI {

    @GET("planetary/apod?api_key=${BuildConfig.NASA_API_KEY}")
    fun getPictureOfTheDay(
        @Query("date") date: String
    ): Call<PODNasaAPOD>

    //https://api.nasa.gov/EPIC/api/natural?api_key=DEMO_KEY
    @GET("EPIC/api/natural?api_key=${BuildConfig.NASA_API_KEY}")
    fun getLastPhotoMeta(): Call<PODEpicMetadata>

}
