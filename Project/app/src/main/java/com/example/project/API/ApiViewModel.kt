package com.example.project.API

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.project.MainActivity
import restaurant.CityBy
import restaurant.CountryBy
import restaurant.Restaurant
import restaurant.RestaurantBy

class ApiViewModel(private val repository: ApiRepository):ViewModel() {
    val restaurants: MutableLiveData<List<Restaurant>> = MutableLiveData()
    val cities: MutableLiveData<List<String>> = MutableLiveData()
    val countries: MutableLiveData<List<String>> = MutableLiveData()
    var pageNum:Int =0
    suspend fun getRestaurantByCountry(country: String, page:Int):RestaurantBy{
       val res=repository.getRestaurantsByCountry(country, page)
       pageNum=res.total_entries/res.per_page
       if(pageNum*res.per_page!=res.total_entries){
           ++pageNum
       }
        Log.d("restaurantSize00", pageNum.toString() + " "+res.total_entries.toString()+" "+res.per_page.toString())
       return res
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
        var i=1
        var ress:List<Restaurant> = listOf()
        do{
            val res=getRestaurantByCountry(country, i)
            ++i
            Log.d("restaurantSize0", res.restaurants.size.toString()+" "+i.toString()+" "+pageNum.toString())
            ress += restaurantsByCountryConverter(res)
        }while(i<=pageNum)

        restaurants.value = ress
        MainActivity.setToPageNum(pageNum)
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