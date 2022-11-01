package ru.gb.daytime_photo.model.retrofits.nasa_apod

import ru.gb.daytime_photo.model.retrofits.utils.RetrofitsUtils


object PODRetrofitImpl {

    const val BASEURL = "https://api.nasa.gov/"

    private var pictureOfTheDayImpl: PictureOfTheDayAPI? = null

    fun getAPI(): PictureOfTheDayAPI {
        return if (pictureOfTheDayImpl == null) {
            createPictureOfTheDayAPI()
        } else pictureOfTheDayImpl!!
    }

    private fun createPictureOfTheDayAPI(): PictureOfTheDayAPI {
        return RetrofitsUtils.buildRetrofitImpl(BASEURL, PictureOfTheDayAPI::class.java)
    }

}
