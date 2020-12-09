package com.example.project.API

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import restaurant.CityBy
import restaurant.CountryBy
import restaurant.Restaurant
import restaurant.RestaurantBy

class ApiViewModel(private val repository: ApiRepository):ViewModel() {
    val restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val cities: MutableLiveData<List<String>> = MutableLiveData()
    val countries: MutableLiveData<List<String>> = MutableLiveData()
    suspend fun getRestaurantByCountry(country: String){
        val res=repository.getRestaurantsByCountry(country)
        restaurants.value=restaurantsByCountryConverter(res)
    }

    suspend fun getCities(){
        val res=repository.getCities()
        cities.value=cityConverter(res)
    }
    private fun cityConverter(cityBy: CityBy): List<String>{
        val list= mutableListOf<String>()

        for(i in cityBy.cities){
            val city=i
            list.add(city)
        }
        return list
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

    suspend fun loadCities(){
        getCities()
    }
    suspend fun getCountries(){
        val res=repository.getCountries()
        countries.value=countryConverter(res)
    }

    private fun countryConverter(countryBy: CountryBy): List<String>{
        val list= mutableListOf<String>()
        for(i in countryBy.countries){
            val country=i
            list.add(country)
        }
        return list

    }

    suspend fun loadCountries()
    {
        getCountries()
    }
}