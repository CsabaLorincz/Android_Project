package com.example.project.API

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import restaurant.Restaurant
import restaurant.RestaurantBy

class ApiViewModel(private val repository: ApiRepository):ViewModel() {
    val restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    suspend fun getRestaurantByCountry(country: String){
        val res=repository.getRestaurantsByCountry(country)
        restaurants.value=restaurantsByCountryConverter(res)
    }
    private fun restaurantsByCountryConverter(restaurantBy: RestaurantBy): List<Restaurant>{
        val list= mutableListOf<Restaurant>()
        for(i in restaurantBy.restaurants){
            val restaurant=Restaurant(i.id, i.name, i.address, i.city, i.state, i.area, i.postal_code, i.country,
            i.phone, i.lat, i.lng, i.price, i.reserve_url, i.mobile_reserve_url, i.image_url)
            list.add(restaurant)
        }
        return list

    }
    suspend fun loadRestaurants(country: String){
        getRestaurantByCountry(country)
    }
}