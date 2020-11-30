package com.example.project

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        /*try {
            //MainActivity.RestaurantApi.retrofitService.getProperties()
            getRestaurantProperties("US")

            Log.d("RESPONSE", _response.value.toString())
        }catch(E: Exception){
           Log.d("RESPONSEERR", E.toString())
        }*/
        val view=inflater!!.inflate(R.layout.fragment_first, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

            super.onViewCreated(view, savedInstanceState)
        }


        //findNavController().navigate(R.id.action_FirstFragment_to_scrollingFragment)
    }
    /*private val _response = MutableLiveData<String>()

    private fun getRestaurantProperties(country: String) {

        lifecycleScope.launch {
            try {
                val listResult = MainActivity.RestaurantApi.retrofitService.getRestaurantsByCountry(country)
                _response.value =
                    "Success: ${listResult} Restaurants retrieved"
                Log.d("RESPONSE1", _response.value.toString())
            } catch (e: Exception) {
                _response.value = "Failure: ${e.message}"
                Log.d("RESPONSE2", _response.value.toString())

            }
        }
    }*/
