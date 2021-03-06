package com.julotest.weatherapp.view.main

import android.content.Context
import android.transition.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.julotest.weatherapp.R
import com.julotest.weatherapp.data.model.WeatherPredictionResponse
import com.julotest.weatherapp.data.model.WeatherResponse
import com.julotest.weatherapp.data.model.prediction.DataList
import com.julotest.weatherapp.view.Util
import kotlinx.android.synthetic.main.card_general.*
import kotlinx.android.synthetic.main.card_general.view.*
import java.text.SimpleDateFormat
import java.util.*


class MainAdapter(var data: List<WeatherResponse>, var context: Context) :
    RecyclerView.Adapter<MainAdapter.ItemVH>() {
    lateinit var listener: WeatherListener

    fun setWeatherListener(listener: WeatherListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            LayoutInflater.from(parent.context).inflate(R.layout.card_general, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItem(datas: List<WeatherResponse>) {
        data = datas
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ItemVH, position: Int) {

        holder.bindView(data[position])

    }

    inner class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: WeatherResponse) {
            itemView.setOnClickListener { listener.onWeatherListener(data) }
            itemView.tvTemperature.text = "${data.main.temp}°C"
            itemView.tvWeather.text = data.weather[0].main
            itemView.tvLocation.text = data.name
            val date = Date(data.dt * 1000)
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy")
            val timezoneValue = data.timezone / 3600
            var timezone = "GMT"
            if (timezoneValue > 0) timezone += "+" + timezoneValue
            else timezone += timezoneValue
            sdf.timeZone = TimeZone.getTimeZone(timezone)

            itemView.tvDatetime.text = sdf.format(date)
            itemView.tvHumidity.text = "Humidity ${data.main.humidity}"
            itemView.tvWindSpeed.text = "Wind Speed ${data.wind.speed}"


            val sdfHour = SimpleDateFormat("HH")
            sdfHour.timeZone = TimeZone.getTimeZone(timezone)
            val hour = sdfHour.format(date).toInt()
            Log.d("hour", hour.toString())
            val isNight = hour < 6 || hour >= 18;
            if (isNight) {
                itemView.background = ContextCompat.getDrawable(context, R.drawable.ic_card_night)
                itemView.ivWeather.setImageDrawable(
                    Util.returnImage(
                        isNight,
                        data.weather[0].main,
                        context
                    )
                )
            } else {
                itemView.background = ContextCompat.getDrawable(context, R.drawable.ic_card_day)
                itemView.ivWeather.setImageDrawable(
                    Util.returnImage(
                        isNight,
                        data.weather[0].main,
                        context
                    )
                )

            }
        }
    }

    interface WeatherListener {
        fun onWeatherListener(data: WeatherResponse)
    }
}