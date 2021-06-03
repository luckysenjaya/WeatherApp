package com.julotest.weatherapp.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.core.content.ContextCompat
import com.julotest.weatherapp.R

class Util {
    companion object{
        fun returnImage(isNight: Boolean, weather:String, context: Context) : Drawable {
            if(weather.equals("Rain")) return ContextCompat.getDrawable(context, R.drawable.ic_rain)!!
            else{
				Log.d("TES","TES")
				Log.d("TES2", "TES2")
				Log.d("TESFEATURE", "TESFEATURE")
                if(isNight) {
                    return ContextCompat.getDrawable(context, R.drawable.ic_night)!!
                }
                else {
                    return ContextCompat.getDrawable(context, R.drawable.ic_sun)!!

                }

            }
        }
    }
}
