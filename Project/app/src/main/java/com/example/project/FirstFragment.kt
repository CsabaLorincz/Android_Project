package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.awaitAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.Exception
import java.net.URL

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            //MainActivity.RestaurantApi.retrofitService.getProperties()
            getRestaurantProperties()

            Log.d("RESPONSE", _response.value.toString())
        }catch(E: Exception){
            Log.d("RESPONSEERR", E.toString())
        }

        //findNavController().navigate(R.id.action_FirstFragment_to_scrollingFragment)
    }
    private val _response = MutableLiveData<String>()

    private fun getRestaurantProperties() {

        MainActivity.RestaurantApi.retrofitService.getProperties().enqueue(
                object: Callback<String> {
                    override fun onFailure(call: Call<String>, t: Throwable) {
                        _response.value = "Failure: " + t.message
                    }

                    override fun onResponse(call: Call<String>, response: Response<String>) {
                        _response.value = response.body().toString()
                        Log.d("RESPONSE2", _response.value.toString())

                        }
                })
    }
}