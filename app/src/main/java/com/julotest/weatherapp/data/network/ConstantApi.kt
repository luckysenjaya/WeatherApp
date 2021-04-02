package com.julotest.weatherapp.data.network

class ConstantApi {
    companion object{
        const val WEATHER_API_KEY = "07e839e0f206d7c77ce6c128f387e9ae"
        const val WEATHER_BASE_URL = "http://api.openweathermap.org/"
        const val DATA_PATH = "data/2.5/"
        const val WEATHER_URL = WEATHER_BASE_URL+ DATA_PATH;
        const val GET_WEATHER = "weather"
        const val GET_WEATHER_PREDICTION = "forecast/daily"

        const val GOOGLE_API_KEY = "AIzaSyD9kRwGp967V2ycyjxP3pygp9N21Yxod6k";
        const val GOOGLE_API_URL = "https://maps.googleapis.com";
        const val GOOGLE_AUTOCOMPLETE_PATH = "/maps/api/place/autocomplete/json"
        const val GOOGLE_AUTOCOMPLETE_URL = GOOGLE_API_URL+ GOOGLE_AUTOCOMPLETE_PATH;
    }
}