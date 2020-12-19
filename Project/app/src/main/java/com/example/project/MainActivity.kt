package com.example.project

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.project.Database.ProjectDatabaseApp
import com.example.project.Database.UserViewModel
import com.example.project.Database.UserViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView
import retrofit2.Retrofit
import kotlinx.coroutines.*
import restaurant.CityBy
import restaurant.CountryBy
import restaurant.RestaurantBy
import retrofit2.converter.gson.GsonConverterFactory
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
        suspend fun getRestaurantsByCountry(@Query("country")country:String, @Query("page")page: Int):
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
        }
    }
    @InternalCoroutinesApi

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

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
        var currentPage=1
        var pageNum=0
        fun setToPageNum(Num: Int){
            pageNum+=Num
        }
        fun increaseCurrentPage(){
            if(currentPage< pageNum)
                ++currentPage
            Log.d("pages: ", currentPage.toString() + "/"+ pageNum.toString())
        }
        fun reduceCurrentPage(){
            if(currentPage>1)
                --currentPage
            Log.d("pages: ", currentPage.toString() + "/"+ pageNum.toString())
        }
        val page_entries=25
        fun setCurrPageToNumber(pg: Int){
            if(pg>0 && pg<=pageNum)
                currentPage=pg
        }
    }


}

