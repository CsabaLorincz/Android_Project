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

            restaurantViewModel.loadRestaurants("US")
            lateinit var list: List<Restaurant>
            restaurantViewModel.restaurants.observe(requireActivity(), Observer{ restaurants ->
                list = restaurants

                Log.d("APIDATA", restaurants[0].toString())
                saveToCompanion(list)
                findNavController().navigate(R.id.action_FirstFragment_to_scrollingFragment)

            })
            //val obs:Observer<List<Restaurant>> = Observer(restaurantViewModel.restaurants!!.value!!)
            /* restaurantViewModel.restaurants.observe(requireActivity(),
                 Observer{}
             )*/

            //Log.d("RESPONSEEEEEEEE", list.toString())
            super.onViewCreated(view, savedInstanceState)

            }
        }

    companion object FragmentComp{
        var list:List<Restaurant> =listOf()

        fun saveToCompanion(l: List<Restaurant>){
            list=l
        }
    }
    }

