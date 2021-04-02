package com.ex.githubuser.data.network



import com.julotest.weatherapp.data.model.PlacesAutoCompleteResponse
import com.julotest.weatherapp.data.model.WeatherPredictionResponse
import com.julotest.weatherapp.data.model.WeatherResponse
import com.julotest.weatherapp.data.network.ConstantApi
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET(ConstantApi.GET_WEATHER)
    fun getWeatherByName(
        @Query("q") q: String,
        @Query("appid") appId: String = ConstantApi.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ) : Call<WeatherResponse>

    @GET(ConstantApi.GET_WEATHER)
    fun getWeatherByCoordinate(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appId: String = ConstantApi.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ) : Call<WeatherResponse>

    @GET(ConstantApi.GET_WEATHER_PREDICTION)
    fun getWeatherPrediction(
        @Query("q") q: String,
        @Query("cnt") count: Int,
        @Query("appid") appId: String = ConstantApi.WEATHER_API_KEY,
        @Query("units") units: String = "metric"
    ) : Call<WeatherPredictionResponse>

    @GET
    fun getPlacesPrediction(
        @Url url : String,
        @Query("input") input : String,
        @Query("types") types: String = "(cities)",
        @Query("key") key: String = ConstantApi.GOOGLE_API_KEY
    ) : Call<PlacesAutoCompleteResponse>




}