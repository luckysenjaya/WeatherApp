package com.julotest.weatherapp.view.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.julotest.weatherapp.data.Repository
import com.julotest.weatherapp.data.model.Result
import com.lukeone.mydiary.data.local.model.City
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    fun getWeather(city: String): MutableLiveData<Result> =
        Repository.getInstance().getWeatherDetail(city)

    fun getWeatherByCoordinate(lat: Double, lon: Double): MutableLiveData<Result> =
        Repository.getInstance().getWeatherDetailByCoordinate(lat, lon)

    fun getAllCity(context: Context): LiveData<List<City>> {
        return Repository.getInstance().getAllCity(context)
    }
}