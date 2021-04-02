package com.julotest.weatherapp.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.julotest.weatherapp.data.model.Result
import com.julotest.weatherapp.data.model.WeatherPredictionResponse
import com.julotest.weatherapp.data.model.WeatherResponse
import com.julotest.weatherapp.data.network.NetworkUtil
import com.lukeone.mydiary.data.local.CityDb
import com.lukeone.mydiary.data.local.model.City
import retrofit2.Call
import retrofit2.Response

//import retrofit2.Call
//import retrofit2.Response
import javax.security.auth.callback.Callback

class Repository {
    companion object{
        private var instance: Repository? = null

        fun getInstance() : Repository =
            instance ?: synchronized(this){
                instance ?: Repository()
            }
    }

    lateinit var data : MutableLiveData<Result>
    fun getWeatherDetail(query:String): MutableLiveData<Result>{
        data = MutableLiveData()
        NetworkUtil().api().getWeatherByName(query).enqueue(object : Callback, retrofit2.Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if(response.isSuccessful)
                    data.postValue(response.body().let { Result(true, response.message(), it) })
                else
                    data.postValue(response.body().let { Result(false, response.message(), null) })

            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                data.postValue(Result(false, t.message.toString(), null));
            }

        })

        return data
    }

    fun getWeatherDetailByCoordinate(lat: Double, lon: Double): MutableLiveData<Result>{
        data = MutableLiveData()
        NetworkUtil().api().getWeatherByCoordinate(lat,lon).enqueue(object : Callback, retrofit2.Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if(response.isSuccessful)
                    data.postValue(response.body().let { Result(true, response.message(), it) })
                else
                    data.postValue(response.body().let { Result(false, response.message(), null) })

            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                data.postValue(Result(false, t.message.toString(), null));
            }

        })

        return data
    }

    fun getWeatherPrediction(city: String): MutableLiveData<Result>{
        data = MutableLiveData()
        NetworkUtil().api().getWeatherPrediction(city, 4).enqueue(object : Callback, retrofit2.Callback<WeatherPredictionResponse>{
            override fun onResponse(
                call: Call<WeatherPredictionResponse>,
                response: Response<WeatherPredictionResponse>
            ) {
                if(response.isSuccessful)
                    data.postValue(response.body().let { Result(true, response.message(), it) })
                else
                    data.postValue(response.body().let { Result(false, response.message(), null) })

            }

            override fun onFailure(call: Call<WeatherPredictionResponse>, t: Throwable) {
                data.postValue(Result(false, t.message.toString(), null));
            }

        })

        return data
    }

    suspend fun insertCity(city: City, context: Context) : Long{
       return CityDb.getInstance(context).getCityDao().insertCity(city)
    }

    fun getCity(cityName: String, country:String, context: Context) = CityDb.getInstance(context).getCityDao().getCityByNameAndCountry(cityName, country)

    suspend fun deleteCity(city: City, context: Context){
        CityDb.getInstance(context).getCityDao().deleteCity(city.cityName.toString(), city.country.toString())
    }

    fun getAllCity(context: Context) : LiveData<List<City>> = CityDb.getInstance(context).getCityDao().getAllCity()
}