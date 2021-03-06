package com.example.project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.Database.ProjectDatabaseApp
import com.example.project.Database.UserFavourites
import com.example.project.Database.UserViewModel
import com.example.project.Database.UserViewModelFactory
import com.example.project.FirstFragment.FragmentComp.list
import com.example.project.MainActivity.Companion.filterFlag
import com.example.project.MainActivity.Companion.logged_in
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
    val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as ProjectDatabaseApp).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(MainActivity.ARG_COLUMN_COUNT)

        }
        restaurants=list

        if(!this::restaurantAdapter.isInitialized) {
            Log.d("???", "oh")
            restaurantAdapter = ItemRecyclerViewAdapter()
            restaurantAdapter.setData(restaurants)
            Log.d("Vanish", restaurants.size.toString())
            val x=launch {

                userFav=userViewModel.userFav.first()
                Log.d("zzz", userFav.toString())
                restaurantAdapter.setFavourite(userFav!!)
                Log.d("zzz", "2")
                restaurantAdapter.setActivity(requireActivity())
                Log.d("zzz", "3")


            }
            x.start()
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
        val back=view.findViewById<ImageButton>(R.id.scrolling_back)
        back.setOnClickListener{
            filterFlag=0
            view.findNavController().navigate(R.id.scrollingFragment)
        }
            with(view) {
                val searchBar = view.findViewById<SearchView>(R.id.searchView)
                searchBar.setOnCloseListener {
                    filterFlag=0
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
                        restaurantList.adapter = restaurantAdapter
                        restaurantList.layoutManager = LinearLayoutManager(activity)
                        restaurantList.setHasFixedSize(true)
                        layoutManager = when {
                            columnCount <= 1 -> LinearLayoutManager(context)
                            else -> GridLayoutManager(context, columnCount)
                        }
                    }

            }
        val shpg=view.findViewById<TextView>(R.id.showPage)
        val pgDown=view.findViewById<ImageButton>(R.id.PageDown)
        val pgUp=view.findViewById<ImageButton>(R.id.PageUp)
        val gotoPg=view.findViewById<EditText>(R.id.gotoPage)
        shpg.text=MainActivity.currentPage.toString()
        pgDown.setOnClickListener {
            MainActivity.reduceCurrentPage()
            filterFlag=0
            view.findNavController().navigate(R.id.scrollingFragment)
        }
        pgUp.setOnClickListener {
            MainActivity.increaseCurrentPage()
            filterFlag=0
            view.findNavController().navigate(R.id.scrollingFragment)
        }
        val letsgo=view.findViewById<Button>(R.id.letsGoPage)
        letsgo.isEnabled=false
        gotoPg.addTextChangedListener{
            if(gotoPg.text.isNotBlank())
                letsgo.isEnabled=true
        }
        letsgo.setOnClickListener {
            val pgg = gotoPg.text.toString() to Int
            MainActivity.setCurrPageToNumber(pgg.first.toInt())
            filterFlag=0
            view.findNavController().navigate(R.id.scrollingFragment)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)

    }



}