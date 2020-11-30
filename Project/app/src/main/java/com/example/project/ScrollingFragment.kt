package com.example.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.API.ApiRepository
import com.example.project.API.ApiViewModel
import com.example.project.API.ApiViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import restaurant.Restaurant
import kotlin.coroutines.CoroutineContext

class ScrollingFragment : Fragment(),CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var columnCount = 1
    private lateinit var restaurantViewModel: ApiViewModel
    private lateinit var restaurantList: RecyclerView
    private lateinit var restaurantAdapter: ItemRecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(MainActivity.ARG_COLUMN_COUNT)

        }


    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_scrolling, container, false)
        val view2 = view.findViewById<RecyclerView>(R.id.recycler)
        if (view2 == null) {
            Log.d("EEE", "null")
        } else
            with(view2) {
                restaurantAdapter = ItemRecyclerViewAdapter()
                restaurantList = view2.findViewById(R.id.recycler)
                restaurantList.adapter = restaurantAdapter
                restaurantList.layoutManager = LinearLayoutManager(activity)
                restaurantList.setHasFixedSize(true)
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

            }

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
               restaurantAdapter.setData(list)
               Log.d("APIDATA", restaurants[0].toString())
           })
            //val obs:Observer<List<Restaurant>> = Observer(restaurantViewModel.restaurants!!.value!!)
            /* restaurantViewModel.restaurants.observe(requireActivity(),
                 Observer{}
             )*/

            //Log.d("RESPONSEEEEEEEE", list.toString())
            super.onViewCreated(view, savedInstanceState)
        }
    }

}