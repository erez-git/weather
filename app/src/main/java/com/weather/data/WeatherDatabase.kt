package com.weather.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.weather.model.CurrentWeatherObject
import com.weather.model.Favourite

@Database(entities = [Favourite::class, CurrentWeatherObject::class], version = 2, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}