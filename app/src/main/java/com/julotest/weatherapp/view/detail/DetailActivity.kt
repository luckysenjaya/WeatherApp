package com.julotest.weatherapp.view.detail

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.julotest.weatherapp.R
import com.julotest.weatherapp.data.model.WeatherPredictionResponse
import com.julotest.weatherapp.data.model.WeatherResponse
import com.julotest.weatherapp.data.model.prediction.DataList
import com.julotest.weatherapp.view.Util
import com.lukeone.mydiary.data.local.model.City
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_general.*
import kotlinx.android.synthetic.main.card_general.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class DetailActivity : AppCompatActivity() {
    lateinit var viewModel: DetailViewModel
    var isSaved = false
    lateinit var menu: Menu
    lateinit var city: String
    lateinit var country: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            DetailViewModel::class.java
        )


        val data = intent.getParcelableExtra<WeatherResponse>("weatherdata")
        city = intent.getStringExtra("city")!!
        country = intent.getStringExtra("country")!!




        if (data == null) {
            getCurrentWeatherByCityName(city.toString())
        } else {
            setCurrentData(data)
        }
        getPredictionData(city.toString())

    }

    fun getCurrentWeatherByCityName(city: String) {
        viewModel.getWeather(city).observe(this, {
            val data = it.data as WeatherResponse
            tvTemperature.text = data.main.temp.toString() + "°C"
            tvLocation.text = data.name + " " + data.sys.country
            tvWeather.text = data.weather[0].main

            val date = Date(data.dt * 1000)
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm") // the format of your date
            val timezoneValue = data.timezone / 3600
            var timezone = "GMT"
            if (timezoneValue > 0) timezone += "+" + timezoneValue
            else timezone += timezoneValue
            sdf.timeZone = TimeZone.getTimeZone(timezone)

            tvDatetime.text = sdf.format(date)
            tvHumidity.text = "Humidity ${data.main.humidity}"
            tvWindSpeed.text = "Wind Speed ${data.wind.speed}"

            val sdfHour = SimpleDateFormat("HH")
            sdfHour.timeZone = TimeZone.getTimeZone(timezone)
            val hour = sdfHour.format(date).toInt()
            Log.d("hour", hour.toString())
            val isNight = hour < 6 || hour >= 18;
            if (isNight) {
                cardLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_card_night)
                ivWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_night))
            } else {
                cardLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_card_day)
                ivWeather.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_sun))
            }
        })
    }

    fun getPredictionData(city: String) {
        viewModel.getWeatherPrediction(city).observe(this, {
            val modifiedList = ArrayList<DataList>()
            var ct = 0
            for (data in (it.data as WeatherPredictionResponse).list) {
                if (ct > 0) modifiedList.add(data)
                ct++
            }
            val adapter = Adapter(modifiedList, city, this)
            rvOtherCity.layoutManager = LinearLayoutManager(this)
            rvOtherCity.adapter = adapter
            adapter.notifyDataSetChanged()
        })
    }

    fun setCurrentData(data: WeatherResponse) {
        tvTemperature.text = data.main.temp.toString() + "°C"
        tvLocation.text = data.name + " " + data.sys.country
        tvWeather.text = data.weather[0].main

        val date = Date(data.dt * 1000)
        val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm") // the format of your date
        sdf.timeZone = TimeZone.getTimeZone("GMT+7")

        tvDatetime.text = sdf.format(date)
        tvHumidity.text = "Humidity ${data.main.humidity}"
        tvWindSpeed.text = "Wind Speed ${data.wind.speed}"

        val sdfHour = SimpleDateFormat("HH")
        sdfHour.timeZone = TimeZone.getTimeZone("GMT+7")
        val hour = sdfHour.format(date).toInt()
        val isNight = hour < 6 || hour >= 18;
        if (isNight) {
            cardLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_card_night)
            ivWeather.setImageDrawable(Util.returnImage(isNight, data.weather[0].main, this))

        } else {
            cardLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_card_day)
            ivWeather.setImageDrawable(Util.returnImage(isNight, data.weather[0].main, this))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        this.menu = menu
        viewModel.getCity(city, country, this).observe(this, {
            if (it != null) {
                isSaved = true
                menu.getItem(0).icon =
                    ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_baseline_star_24)
            } else {
                isSaved = false
                menu.getItem(0).icon = ContextCompat.getDrawable(
                    this@DetailActivity,
                    R.drawable.ic_outline_star_border_24
                )
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isSaved) {
            viewModel.deleteCity(City(0, city, country), this)
            isSaved = false
            menu.getItem(0).icon =
                ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_outline_star_border_24)

        } else {
            viewModel.insertCity(City(0, city, country), this)
            isSaved = true
            menu.getItem(0).icon =
                ContextCompat.getDrawable(this@DetailActivity, R.drawable.ic_baseline_star_24)


        }
        return super.onOptionsItemSelected(item)
    }
}