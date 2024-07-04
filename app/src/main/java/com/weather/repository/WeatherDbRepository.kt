package com.weather.repository

import com.weather.data.WeatherDao
import com.weather.model.CurrentWeatherObject
import com.weather.model.Favourite
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao) {

    fun getFavourites(): Flow<List<Favourite>> = weatherDao.getFavourites()
    suspend fun insertFavourite(favourite: Favourite) = weatherDao.insertFavourite(favourite)
    suspend fun updateFavourite(favourite: Favourite) = weatherDao.updateFavourite(favourite)
    suspend fun deleteAllFavourite() = weatherDao.deleteAllFavourites()
    suspend fun deleteFavourite(favourite: Favourite) = weatherDao.deleteFavourite(favourite)
    suspend fun getFavById(city: String): Favourite = weatherDao.getFavById(city)

    // Current weather table
    fun getWeatherObjects(): Flow<List<CurrentWeatherObject>> = weatherDao.getWeatherObjects()
    suspend fun getWeatherById(id: Int): CurrentWeatherObject = weatherDao.getWeatherById(id)
    suspend fun insertCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = weatherDao.insertCurrentWeatherObject(currentWeatherObject)
    suspend fun updateCurrentWeatherObject(currentWeatherObject: CurrentWeatherObject) = weatherDao.updateCurrentWeatherObject(currentWeatherObject)
}