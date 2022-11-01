package ru.gb.daytime_photo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.daytime_photo.BuildConfig
import ru.gb.daytime_photo.model.retrofits.nasa_apod.PODEpicMetadata
import ru.gb.daytime_photo.model.retrofits.nasa_apod.PODEpicMetadataItem
import ru.gb.daytime_photo.model.retrofits.nasa_apod.PODRetrofitImpl
import java.text.SimpleDateFormat
import java.util.*


class PictureOfTheEarthViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfEarthData> = MutableLiveData(),
) : ViewModel() {

    private val resourceUrl =
        "EPIC/archive/natural/{year}/{month}/{day}/jpg/{image}.jpg?api_key={api}"

    fun getData(): LiveData<PictureOfEarthData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {

        val apiKey: String = BuildConfig.NASA_API_KEY

        liveDataForViewToObserve.value = PictureOfEarthData.Loading(null)

        if (apiKey.isNotBlank()) {
            PODRetrofitImpl.getAPI().getLastPhotoMeta().enqueue(object : Callback<PODEpicMetadata> {
                override fun onResponse(
                    call: Call<PODEpicMetadata>,
                    response: Response<PODEpicMetadata>
                ) {
                    liveDataForViewToObserve.postValue(
                        if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                            PictureOfEarthData.SuccessEarthPhoto(getPhotoUrl(response.body()!![0]))
                        } else {
                            PictureOfEarthData.Error(Throwable("Ошибка"))
                        }
                    )
                }

                override fun onFailure(call: Call<PODEpicMetadata>, t: Throwable) {
                    liveDataForViewToObserve.postValue(PictureOfEarthData.Error(t))
                }
            }
            )
        }
    }

    private fun getPhotoUrl(MetadataItem: PODEpicMetadataItem): String {

        var url = ""
        val simpleDateString = MetadataItem.date
        val image = MetadataItem.image

        val dateLastPhoto: Date =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault(Locale.Category.FORMAT)
            ).parse(simpleDateString) as Date

        GregorianCalendar().apply {
            time = dateLastPhoto
            url = resourceUrl.replace("{year}", this[Calendar.YEAR].toString())
            url = url.replace("{month}", (this[Calendar.MONTH] + 1).toString())
            url = url.replace("{day}", (this[Calendar.DAY_OF_MONTH]).toString())
            url = url.replace("{image}", image)
            url = url.replace("{api}", BuildConfig.NASA_API_KEY)
        }

        url = PODRetrofitImpl.BASEURL + url

        return url
    }
}



