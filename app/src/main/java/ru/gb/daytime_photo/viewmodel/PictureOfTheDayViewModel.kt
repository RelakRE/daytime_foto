package ru.gb.daytime_photo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.gb.daytime_photo.BuildConfig
import ru.gb.daytime_photo.model.PODRetrofitImpl
import ru.gb.daytime_photo.model.PODServerResponseData
import ru.gb.daytime_photo.model.PODServerResponseErrorData
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class PictureOfTheDayViewModel(
    private val liveDataForViewToObserve: MutableLiveData<PictureOfTheDayData> = MutableLiveData(),
    private val retrofitImpl: PODRetrofitImpl = PODRetrofitImpl()
) : ViewModel() {

    var dateOnDateChips: LocalDate? = null
    val TODAY_DATE = 0
    val YESTERDAY_DATE = -1
    val TOMORROW_DATE = 1

    private var selectedDay: LocalDate = LocalDate.now()

    fun getData(): LiveData<PictureOfTheDayData> {
        sendServerRequest(getRequestDate())
        return liveDataForViewToObserve
    }

    fun setDay(date: LocalDate){
        selectedDay = date
    }

    fun setDay(day: Int) {
        selectedDay = when(day){
            TODAY_DATE -> LocalDate.now()
            YESTERDAY_DATE -> LocalDate.now().minusDays(1)
            TOMORROW_DATE -> LocalDate.now().plusDays(1)
            else -> LocalDate.now()
        }
    }

    private fun getRequestDate(): LocalDate {
        return selectedDay
    }

    private fun sendServerRequest(date: LocalDate = LocalDate.now()) {
        liveDataForViewToObserve.value = PictureOfTheDayData.Loading(null)
        val apiKey: String = BuildConfig.NASA_API_KEY
        val dateSimple: String = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        if (apiKey.isBlank()) {
            PictureOfTheDayData.Error(Throwable("You need API key"))
        } else {
            retrofitImpl.getRetrofitImpl().getPictureOfTheDay(apiKey, dateSimple).enqueue(object :
                Callback<PODServerResponseData> {
                override fun onResponse(
                    call: Call<PODServerResponseData>,
                    response: Response<PODServerResponseData>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        liveDataForViewToObserve.value =
                            PictureOfTheDayData.Success(response.body()!!)
                    } else {

                        val message = response.message()
                        if (message.isNullOrEmpty()) {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            val textError: String = try {
                                jObjError.getString("msg")
                            } catch (e: Exception) {
                                "Unidentified error"
                            }
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(textError))
                        } else {
                            liveDataForViewToObserve.value =
                                PictureOfTheDayData.Error(Throwable(message))
                        }
                    }
                }

                override fun onFailure(call: Call<PODServerResponseData>, t: Throwable) {
                    liveDataForViewToObserve.value = PictureOfTheDayData.Error(t)
                }
            })
        }
    }

}