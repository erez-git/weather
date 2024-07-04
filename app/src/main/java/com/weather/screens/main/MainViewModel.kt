package com.weather.screens.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.weather.data.DataOrException
import com.weather.model.Weather
import com.weather.repository.WeatherRepository
import com.weather.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {
    private var latitude = mutableStateOf(Constants.INVALID_LATITUDE)
    private var longitude = mutableStateOf(Constants.INVALID_LONGITUDE)
    private var _city = mutableStateOf(Constants.DEFAULT_CITY)
    val city: State<String> = _city

    fun setCity(cityName: String) {
        _city.value = cityName
    }

    suspend fun getWeatherData(
        lat: Double,
        lon: Double
    ): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeather(latQuery = lat, lonQuery = lon)
    }
}