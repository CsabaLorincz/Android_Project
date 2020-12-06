package com.example.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.annotation.MainThread
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.API.ApiRepository
import com.example.project.API.ApiViewModel
import com.example.project.API.ApiViewModelFactory
import com.example.project.Database.User
import com.example.project.Database.UserFavourites
import com.example.project.Database.UserViewModel
import com.example.project.FirstFragment.FragmentComp.list
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import restaurant.Restaurant
import kotlin.coroutines.CoroutineContext

class ScrollingFragment : Fragment(),CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var columnCount = 1

    private lateinit var restaurantList: RecyclerView
    private lateinit var restaurantAdapter: ItemRecyclerViewAdapter
    private lateinit var restaurants: List<Restaurant>
    private var userFav: List<UserFavourites>? = null
    private var ready:Boolean =false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(MainActivity.ARG_COLUMN_COUNT)

        }
        //Log.d("Check", restaurants[0].toString())
        restaurants=list

        if(!this::restaurantAdapter.isInitialized) {
            Log.d("???", "oh")
            restaurantAdapter = ItemRecyclerViewAdapter()
            restaurantAdapter.setData(restaurants)

            val x=launch {

                userFav = activity?.viewModels<UserViewModel>()?.value?.userFav?.first()
                Log.d("zzz", userFav.toString())
                restaurantAdapter.setFavourite(userFav!!)
                Log.d("zzz", "2")
                restaurantAdapter.setActivity(requireActivity())
                Log.d("zzz", "3")


            }
            x.start()
            Log.d("zzz", "zzz")

            Log.d("zzz", "ready")
        }

    }
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        Log.d("zzz", "he")
        val view = inflater.inflate(R.layout.fragment_scrolling, container, false)
        val view2 = view.findViewById<RecyclerView>(R.id.recycler)
        if (view2 == null) {
            Log.d("EEE", "null")
        } else
            with(view2) {


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
            super.onViewCreated(view, savedInstanceState)

    }



}