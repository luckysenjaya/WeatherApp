package com.julotest.weatherapp.view.detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julotest.weatherapp.data.Repository
import com.julotest.weatherapp.data.model.Result
import com.lukeone.mydiary.data.local.CityDb
import com.lukeone.mydiary.data.local.model.City
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel : ViewModel() {
    fun getWeather(city: String) : MutableLiveData<Result> = Repository.getInstance().getWeatherDetail(city)

    fun getWeatherPrediction(city: String) : MutableLiveData<Result> = Repository.getInstance().getWeatherPrediction(city)

    fun insertCity(city: City, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val id = Repository.getInstance().insertCity(city, context)
        }
    }

    fun deleteCity(city: City, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            Repository.getInstance().deleteCity(city, context)
        }
    }

    fun getCity(cityName: String, country:String, context: Context): LiveData<City> {
        return Repository.getInstance().getCity(cityName, country, context)
    }

}