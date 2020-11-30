package com.example.project.API

import com.example.project.MainActivity
import restaurant.RestaurantBy

class ApiRepository {
    suspend fun getRestaurantsByCountry(country:String):RestaurantBy{
        return MainActivity.RestaurantApi.retrofitService.getRestaurantsByCountry(country)
    }
}