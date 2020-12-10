package com.example.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.API.ApiRepository
import com.example.project.API.ApiViewModel
import com.example.project.API.ApiViewModelFactory
import com.example.project.Database.UserFavourites
import com.example.project.Database.UserViewModel
import com.example.project.FirstFragment.FragmentComp.list
import com.example.project.MainActivity.Companion.filterFlag
import com.example.project.MainActivity.Companion.logged_in
import kotlinx.android.synthetic.main.fragment_scrolling.view.*
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

        restaurantList=view.findViewById(R.id.recycler)
        var view2=view.findViewById<ConstraintLayout>(R.id.ScrollingConstraint)
        val back=view.findViewById<ImageButton>(R.id.scrolling_back)
        back.setOnClickListener{
            view.findNavController().navigate(R.id.scrollingFragment)
        }
            with(view) {
                val searchBar = view.findViewById<SearchView>(R.id.searchView)
                searchBar.setOnCloseListener {
                    view.findNavController().navigate(R.id.scrollingFragment)
                    true
                }
                searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return if(query.isNullOrEmpty()) {
                            false
                        } else {
                            filterFlag=1
                            restaurantAdapter.filter.filter(query.toString())
                            true
                        }
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        return if(newText.isNullOrEmpty()) {
                            false
                        } else {
                            filterFlag=1
                            restaurantAdapter.filter.filter(newText.toString())
                            true
                        }
                    }
                })
                val cityList = mutableListOf("SELECT CITY")
                val countryList = mutableListOf("SELECT COUNTRY")
                cityList += FirstFragment.cities
                countryList += FirstFragment.countries

                val citySpinnerAdapter= ArrayAdapter(requireContext(), R.layout.spinner_layout, cityList)
                val countrySpinnerAdapter = ArrayAdapter(requireContext(), R.layout.spinner_layout, countryList)

                val citySpinner=view.findViewById<Spinner>(R.id.citySpinner)
                val countrySpinner=view.findViewById<Spinner>(R.id.countrySpinner)

                citySpinner.adapter = citySpinnerAdapter
                countrySpinner.adapter = countrySpinnerAdapter

                citySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val city: String = parent?.getItemAtPosition(position).toString()
                        if(city=="SELECT CITY"){
                             filterFlag=3
                             restaurantAdapter.filter.filter("")
                        }
                        else{
                            filterFlag=3
                            restaurantAdapter.filter.filter(city)
                         }

                    }
                }

                countrySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onNothingSelected(parent: AdapterView<*>?) {
                    }

                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        val country: String = parent?.getItemAtPosition(position).toString()
                        if(country=="SELECT COUNTRY"){
                            filterFlag=2
                            restaurantAdapter.filter.filter("")
                        }
                        else{
                            filterFlag=2
                            restaurantAdapter.filter.filter(country)
                        }

                    }
                }

                val favbutt=view.findViewById<Button>(R.id.favourite_button_scrolling)
                favbutt.isEnabled= logged_in
                favbutt.setOnClickListener {
                    filterFlag=4
                    restaurantAdapter.filter.filter("a")
                }

                with(restaurantList) {
                        //restaurantList = view2.findViewById(R.id.recycler)
                        restaurantList.adapter = restaurantAdapter
                        restaurantList.layoutManager = LinearLayoutManager(activity)
                        restaurantList.setHasFixedSize(true)
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                    }

            }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

    }



}