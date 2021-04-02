package com.julotest.weatherapp.view.detail

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
import com.julotest.weatherapp.data.model.prediction.DataList
import com.julotest.weatherapp.view.Util
import kotlinx.android.synthetic.main.card_general.*
import kotlinx.android.synthetic.main.card_general.view.*
import java.text.SimpleDateFormat
import java.util.*


class Adapter(var data: List<DataList>, var city: String, val context: Context) :
    RecyclerView.Adapter<Adapter.ItemVH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemVH {
        return ItemVH(
            LayoutInflater.from(parent.context).inflate(R.layout.card_general, parent, false)
        )

    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun setItem(datas: List<DataList>) {
        data = datas
        notifyDataSetChanged()
    }


    override fun onBindViewHolder(holder: ItemVH, position: Int) {

        holder.bindView(data[position])

    }

    inner class ItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindView(data: DataList) {

            itemView.tvTemperature.text = "${data.temp.min}°C - ${data.temp.max}°C"
            itemView.tvWeather.text = data.weather[0].main
            itemView.tvLocation.text = city
            val date = Date(data.dt * 1000)
            val sdf = SimpleDateFormat("EEE, dd MMM yyyy") // the format of your date


            sdf.timeZone = TimeZone.getTimeZone("GMT+7")

            itemView.tvDatetime.text = sdf.format(date)
            itemView.tvHumidity.text = "Humidity ${data.humidity}"
            itemView.tvWindSpeed.text = "Wind Speed ${data.speed}"

            val sdfHour = SimpleDateFormat("HH")
            sdfHour.timeZone = TimeZone.getTimeZone("GMT+7")
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
}