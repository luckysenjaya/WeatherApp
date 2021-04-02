package com.lukeone.mydiary.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.lukeone.mydiary.data.local.model.City

@Dao
interface CityDao {



    @Transaction
    @Query("SELECT * FROM cities WHERE cityName=:cityName AND country=:country")
    fun getCityByNameAndCountry(cityName:String, country:String): LiveData<City>

    @Transaction
    @Query("SELECT * FROM cities")
    fun getAllCity() : LiveData<List<City>>


    @Insert
    suspend fun insertCity(city: City) : Long

    @Update
    suspend fun updateCity(city: City)

    @Transaction
    @Query("DELETE FROM cities WHERE cityName=:cityName AND country=:country")
    suspend fun deleteCity(cityName:String, country:String)

    @Delete
    suspend fun deleteCity(city: City)
}