package com.julotest.weatherapp.view.main

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.location.Criteria
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.julotest.weatherapp.R
import com.julotest.weatherapp.data.model.WeatherResponse
import com.julotest.weatherapp.data.network.ConstantApi
import com.julotest.weatherapp.view.Util
import com.julotest.weatherapp.view.detail.DetailActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card_general.*
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    lateinit var mLocationManager: LocationManager
    var savedList = ArrayList<WeatherResponse>()
    val TAG = "MainActivityLog"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), ConstantApi.GOOGLE_API_KEY);
        }
        Places.createClient(this)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )



        mLocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager



        val autocompleteFragment = supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
        autocompleteFragment.setPlaceFields(
            Arrays.asList(
                Place.Field.NAME,
                Place.Field.ADDRESS_COMPONENTS
            )
        )
        autocompleteFragment.setTypeFilter(TypeFilter.CITIES)
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                var countryName = ""
                var name = place.addressComponents?.asList()?.get(0)?.name
                for (address in place.addressComponents?.asList()!!) {
                    if (address.getTypes().get(0).equals("country")) {
                        countryName = address.shortName.toString()
                    }

                }
                Log.i("Place", name + " " + countryName + " " + place.addressComponents.toString())
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("city", name)
                intent.putExtra("country", countryName)
                startActivity(intent)
            }

            override fun onError(status: Status) {
                Log.i(TAG, "An error occurred: $status")
            }
        })
    }

    fun getCurrentWeatherByCityName(city: String) {
        viewModel.getWeather(city).observe(this, {
            var data = it.data as WeatherResponse
            tvTemperature.text = data.main.temp.toString()
            tvLocation.text = city

            val date = Calendar.getInstance().time
            val formatter = SimpleDateFormat.getDateTimeInstance() //or use getDateInstance()
            val formatedDate = formatter.format(date)
            tvDatetime.text = formatedDate
        })
    }

    override fun onResume() {
        getSavedCity()
        getCurrentWeather()
        super.onResume()
    }

    fun getSavedCity(){
        savedList.clear()
        val adapter = MainAdapter(savedList, this)
        adapter.setWeatherListener(object: MainAdapter.WeatherListener {
            override fun onWeatherListener(data: WeatherResponse) {
                Log.d("testtt", "testtt "+ data.name)
                val intent = Intent(this@MainActivity, DetailActivity::class.java)
                intent.putExtra("city", data.name)
                intent.putExtra("country", data.sys.country)
                startActivity(intent)
            }
        })
        rvOtherCity.layoutManager = LinearLayoutManager(this)
        rvOtherCity.adapter = adapter
        viewModel.getAllCity(this).observe(this, {
            for (city in it){
                viewModel.getWeather(city.cityName.toString()).observe(this,{
                    savedList.add(it.data as WeatherResponse)

                    adapter.notifyDataSetChanged()
                })
            }
        })
    }

    fun getCurrentWeather() {
        val criteria = Criteria()
        val provider = mLocationManager.getBestProvider(criteria, false)

        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 1)
            return
        }

        val location: Location = mLocationManager.getLastKnownLocation(provider.toString())!!

        viewModel.getWeatherByCoordinate(location.latitude, location.longitude).observe(this, {
            val data = it.data as WeatherResponse
            tvTemperature.text = data.main.temp.toString() + "°C"
            tvLocation.text = data.name + " " + data.sys.country
            tvWeather.text = data.weather[0].main

            val date = Date(data.dt*1000)
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy HH:mm") // the format of your date
            sdf.timeZone = TimeZone.getTimeZone("GMT+7")

            tvDatetime.text = sdf.format(date)
            tvHumidity.text = "Humidity ${data.main.humidity}"
            tvWindSpeed.text = "Wind Speed ${data.wind.speed}"

            val sdfHour = SimpleDateFormat("HH")
            sdfHour.timeZone = TimeZone.getTimeZone("GMT+7")
            val hour = sdfHour.format(date).toInt()
            Log.d("hour",hour.toString())
            val isNight = hour < 6 || hour >= 18;
            if(isNight) {
                cardLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_card_night)
                ivWeather.setImageDrawable(Util.returnImage(isNight, data.weather[0].main, this))
            }
            else {
                cardLayout.background = ContextCompat.getDrawable(this, R.drawable.ic_card_day)
                ivWeather.setImageDrawable(Util.returnImage(isNight, data.weather[0].main, this))

            }


            cardLayout.setOnClickListener {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra("weatherdata", data)
                intent.putExtra("city", data.name)
                intent.putExtra("country", data.sys.country)
                startActivity(intent)
            }
        })
    }




}