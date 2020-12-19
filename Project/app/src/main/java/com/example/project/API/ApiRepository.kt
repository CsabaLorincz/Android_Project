package com.example.project.API

import com.example.project.MainActivity
import restaurant.CityBy
import restaurant.CountryBy
import restaurant.RestaurantBy

class ApiRepository {
    suspend fun getRestaurantsByCountry(country:String, page: Int):RestaurantBy{
        return MainActivity.RestaurantApi.retrofitService.getRestaurantsByCountry(country, page)
    }

    suspend fun getCountries(): CountryBy {
        return MainActivity.RestaurantApi.retrofitService.getCountries()
    }
    suspend fun getCities(): CityBy {
        return MainActivity.RestaurantApi.retrofitService.getCities()
    }
}