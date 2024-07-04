import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*
import com.google.android.gms.location.*
import com.google.gson.Gson
import com.weather.R
import com.weather.data.DataOrException
import com.weather.model.Weather
import com.weather.screens.main.MainViewModel
import com.weather.ui.theme.poppinsFamily
import com.weather.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

@ExperimentalPermissionsApi
@Composable
fun GetCurrentLocation(
    mainViewModel: MainViewModel,
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>,
) {
    LaunchedEffect(key1 = true) {
        // Log to verify default coordinates are being used
        Log.d("Default Location", "Latitude: ${latitude.value}, Longitude: ${longitude.value}")
    }

    // Directly call ShowData within the composable context
    ShowData(
        mainViewModel = mainViewModel,
        context = context,
        latitude = latitude.value,
        longitude = longitude.value
    )
}


@Composable
fun ShowData(
    mainViewModel: MainViewModel,
    context: Context,
    latitude: Double,
    longitude: Double
) {
    val gson = Gson()
    if (latitude != Constants.INVALID_LATITUDE && longitude != Constants.INVALID_LONGITUDE) {
        // Latitude and longitude are valid, so continue as normal
        val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            Log.d("Lat $ Lon", "$latitude and $longitude")
            value = mainViewModel.getWeatherData(latitude, longitude)
        }.value


        if (weatherData.loading == true) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(color = Color(0xFFd68118))
                Spacer(modifier = Modifier.height(10.dp))
                androidx.compose.material3.Text(
                    context.getString(R.string.loading_weather_information),
                    color = Color.White,
                    fontFamily = poppinsFamily
                )
            }
        } else if (weatherData.data != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                val icon = weatherData.data!!.current.weather[0].icon
                var image = R.drawable.sun_cloudy
                if (icon == "01d") {
                    image = R.drawable.sunny
                } else if (icon == "02d") {
                    image = R.drawable.sunny
                } else if (icon == "03d" || icon == "04d" || icon == "04n" || icon == "03n" || icon == "02n") {
                    image = R.drawable.cloudy
                } else if (icon == "09d" || icon == "10n" || icon == "09n") {
                    image = R.drawable.rainy
                } else if (icon == "10d") {
                    image = R.drawable.rainy_sunny
                } else if (icon == "11d" || icon == "11n") {
                    image = R.drawable.thunder_lightning
                } else if (icon == "13d" || icon == "13n") {
                    image = R.drawable.snow
                } else if (icon == "50d" || icon == "50n") {
                    image = R.drawable.fog
                } else if (icon == "01n") {
                    image = R.drawable.clear
                } else {
                    R.drawable.cloudy
                }
                Box(
                    modifier = Modifier.background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                colors.secondary,
                                colors.primary, Color.Transparent,
                            ), tileMode = TileMode.Clamp
                        ), alpha = 0.7F
                    )
                ) {
                    Image(
                        painter = painterResource(id = image),
                        contentDescription = "WeatherIcon",
                        modifier = Modifier
                            .scale(
                                1F
                            )
                    )

                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 5.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        weatherData.data!!.current.weather[0].description.split(' ')
                            .joinToString(separator = " ") { word -> word.replaceFirstChar { it.uppercase() } },
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = poppinsFamily
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = colors.onPrimary,
                                    fontSize = 50.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = poppinsFamily
                                )
                            ) {
                                append(weatherData.data!!.current.temp.toInt().toString())
                            }
                            withStyle(
                                style = SpanStyle(
                                    color = Color(0xFFd68118),
                                    fontSize = 45.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = poppinsFamily
                                )
                            ) {
                                append("°")
                            }
                        })
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.pressure),
                            contentDescription = "PressureIcon",
                            modifier = Modifier
                                .scale(
                                    1F
                                )
                                .size(80.dp)
                        )
                        Text(
                            weatherData.data!!.current.pressure.toString() + "hPa",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFamily
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("Pressure", fontSize = 12.sp, fontFamily = poppinsFamily)
                    }
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.wind),
                            contentDescription = "WindIcon",
                            modifier = Modifier
                                .scale(
                                    1F
                                )
                                .size(80.dp)

                        )
                        Text(
                            weatherData.data!!.current.wind_speed.toInt().toString() + "m/s",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFamily
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text("Wind", fontSize = 12.sp, fontFamily = poppinsFamily)
                    }
                    Column(
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.humidity),
                            contentDescription = "HumidityIcon",
                            modifier = Modifier
                                .scale(
                                    1F
                                )
                                .size(80.dp)
                        )
                        Text(
                            weatherData.data!!.current.humidity.toString() + "%",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            fontFamily = poppinsFamily
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(context.getString(R.string.humidity), fontSize = 12.sp, fontFamily = poppinsFamily)
                    }
                }
            }

        }
    }
}

suspend fun getLocationName(
    context: Context,
    latitude: MutableState<Double>,
    longitude: MutableState<Double>
): String {
    // To specify that the geocoding operation should be performed on the IO dispatcher
    return withContext(Dispatchers.IO) {
        /*
        withContext function will automatically suspend the current coroutine and resume it
        when the operation is complete, allowing other operations to be performed in the meantime
         */
        val geocoder = Geocoder(context, Locale.getDefault())
        val addresses = geocoder.getFromLocation(latitude.value, longitude.value, 1)
        var locationName = ""
        if (addresses != null && addresses.size > 0) {
            val address: Address = addresses[0]
            locationName = address.adminArea
        }
        locationName
    }
}