package com.example.project

import android.os.Bundle
import android.provider.SyncStateContract
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.project.Database.ProjectDatabaseApp
import com.example.project.Database.User
import com.example.project.Database.UserViewModel
import com.example.project.Database.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Call
import retrofit2.Retrofit
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import restaurant.CityBy
import restaurant.CountryBy
import restaurant.Restaurant
import restaurant.RestaurantBy
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory((application as ProjectDatabaseApp).repository)
    }


    interface RestaurantApiService{
        @GET("countries")
        suspend fun getCountries():
                CountryBy
        @GET("cities")
        suspend fun getCities():
                CityBy
        @GET("restaurants")
        suspend fun getRestaurantsByCountry(@Query("country")country:String):
                RestaurantBy

    }
    object RestaurantApi {
        //private const val BASE_URL = "https://opentable.herokuapp.com/api/"
        private const val BASE_URL= "https://ratpark-api.imok.space/"
        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val retrofitService : RestaurantApiService by lazy {
            retrofit.create(RestaurantApiService::class.java) }
    }
    lateinit var navView: BottomNavigationView

    @InternalCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        launch {

            setContentView(R.layout.activity_main)
            setSupportActionBar(findViewById(R.id.toolbar))

            findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }
            navView = findViewById(R.id.bottomnav)

            val navController = findNavController(R.id.nav_host_fragment)

            val appBarConfiguration =
                AppBarConfiguration(setOf(R.id.scrollingFragment, R.id.SecondFragment))
            setupActionBarWithNavController(navController, appBarConfiguration)
            navView.setupWithNavController(navController)

            //userViewModel.deleteAllFav()
            //userViewModel.deleteAll()
            userViewModel.insert(User(0, "1", "asd@asd.asd", "123", "1 12"))

        }
    }
    @InternalCoroutinesApi

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        //menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }

    }

    companion object Companion{
        const val ARG_COLUMN_COUNT = "column-count"
        var logged_in=false
        fun setLogin(b: Boolean){
            logged_in=b
        }
        var logged_in_id:String = ""
        fun setLoggedId(id:String){
            logged_in_id=id
        }

        var filterFlag=0
    }


}

