package ru.gb.daytime_photo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.daytime_photo.BuildConfig
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PictureOfEarthPhotoViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfEarthData> = MutableLiveData()
) : ViewModel() {

    fun getData(): LiveData<PictureOfEarthData> {
        sendServerRequest()
        return liveDataForViewToObserve
    }

    private fun sendServerRequest() {

        liveDataForViewToObserve.value = PictureOfEarthData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        val date: LocalDate = LocalDate.now().minusYears(1)
        val dateSimple: String = date.format(DateTimeFormatter.ISO_LOCAL_DATE)


        if (apiKey.isBlank()) {
            PictureOfEarthData.Error(Throwable("You need API key"))
        } else {
//            retrofitImpl.getRetrofitImpl(PictureOfTheDayAPI::class.java).getEarthPhoto(apiKey, dateSimple).enqueue(object :
//                Callback<PODServerResponseEarthData> {
//                override fun onResponse(
//                    call: Call<PODServerResponseEarthData>,
//                    response: Response<PODServerResponseEarthData>
//                ) {
//                    if (response.isSuccessful && response.body() != null) {
//                        liveDataForViewToObserve.value =
//                            PictureOfEarthData.SuccessEarthPhoto(response.body()!!)
//                    } else {
//                        val message = response.message()
//                        if (message.isNullOrEmpty()) {
//                            val jObjError = JSONObject(response.errorBody()!!.string())
//                            val textError: String = try {
//                                jObjError.getString("msg")
//                            } catch (e: Exception) {
//                                "Unidentified error"
//                            }
//                            liveDataForViewToObserve.value =
//                                PictureOfEarthData.Error(Throwable(textError))
//                        } else {
//                            liveDataForViewToObserve.value =
//                                PictureOfEarthData.Error(Throwable(message))
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<PODServerResponseEarthData>, t: Throwable) {
//                    liveDataForViewToObserve.value = PictureOfEarthData.Error(t)
//                }
//            })
        }

    }


}