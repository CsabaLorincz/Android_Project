package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.project.API.ApiRepository
import com.example.project.API.ApiViewModel
import com.example.project.API.ApiViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import restaurant.Restaurant
import kotlin.coroutines.CoroutineContext


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment(), CoroutineScope {

    private lateinit var restaurantViewModel: ApiViewModel
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        val view=inflater!!.inflate(R.layout.fragment_first, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        launch {
            val repository = ApiRepository()
            val factory = ApiViewModelFactory(repository)
            restaurantViewModel = ViewModelProvider(requireActivity(), factory).get(ApiViewModel::class.java)

            restaurantViewModel.loadCities()
            lateinit var cityList: List<String>
            restaurantViewModel.cities.observe(requireActivity(), Observer { cities->
                cityList=cities
                Log.d("APIDATA_CITY", cities[0])
                Log.d("APIDATA_CITY_LENGTH", cityList.size.toString())
                setToCities(cityList)
            })

            restaurantViewModel.loadCountries()
            lateinit var countryList: List<String>
            restaurantViewModel.countries.observe(requireActivity(), Observer { countries->
                countryList=countries
                Log.d("APIDATA_COUNTRY", countryList[0])
                Log.d("APIDATA_COUNTRY_LENGTH", countryList.size.toString())
                setToCountries(countryList)
                for(i in countryList){
                    restaurantLoading(i)

                }
            })

            lateinit var list: List<Restaurant>
            restaurantViewModel.restaurants.observe(requireActivity(), Observer{ restaurants ->
                list = restaurants

                Log.d("APIDATA", restaurants[0].toString())
                Log.d("APIDATA_LENGTH", FragmentComp.list.size.toString())
                saveToCompanion(list)
                if(countLoading== countries.size)
                    findNavController().navigate(R.id.action_FirstFragment_to_scrollingFragment)

            })
            
            super.onViewCreated(view, savedInstanceState)

            }
        }
    fun restaurantLoading(country: String){
        launch{
            Log.d("...alma", country)
            restaurantViewModel.loadRestaurants(country)
        }
    }
    companion object FragmentComp{
        var list:List<Restaurant> =listOf()
        var countries:List<String> = listOf()
        var cities: List<String> = listOf()
        var countLoading=0
        fun saveToCompanion(l: List<Restaurant>){
            list+=l
            ++countLoading
        }
        fun setToCountries(c: List<String>){
            countries=c
        }
        fun setToCities(c: List<String>){
            cities=c
        }

    }
    }

