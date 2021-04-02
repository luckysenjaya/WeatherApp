package com.lukeone.mydiary.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.lukeone.mydiary.data.local.dao.CityDao
import com.lukeone.mydiary.data.local.model.City

@Database(entities = [City::class],
        version = 1,
        exportSchema = false)
abstract class CityDb : RoomDatabase() {
    abstract fun getCityDao(): CityDao

    companion object {

        @Volatile
        private var INSTANCE: CityDb? = null

        fun getInstance(context: Context): CityDb =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: Room.databaseBuilder(context.applicationContext,
                            CityDb::class.java,
                            "City.db").fallbackToDestructiveMigration().build()
                }
    }
}