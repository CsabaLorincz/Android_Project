package com.example.project

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.Database.ProjectDatabaseApp
import com.example.project.Database.UserFavourites
import com.example.project.Database.UserViewModel
import com.example.project.Database.UserViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import restaurant.Restaurant
import java.util.*
import kotlin.coroutines.CoroutineContext

class ItemRecyclerViewAdapter(


) : RecyclerView.Adapter<ItemRecyclerViewAdapter.RestaurantViewHolder>(), CoroutineScope, Filterable {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    lateinit var c: FragmentActivity
    var cisInit=false
    var restaurants: List<Restaurant> = listOf()
    var searchableRestaurants: MutableList<Restaurant> = mutableListOf()
    lateinit var view:View
    var favourites: MutableList<UserFavourites> = mutableListOf()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {

        if(!searchableRestaurants.isEmpty() && position<searchableRestaurants.size) {
            holder.fav.isEnabled = MainActivity.logged_in
            var rest: Restaurant
            Log.d("favo2?", position.toString())
            try{

                rest = searchableRestaurants[position]
                Log.d("favo23?", position.toString()+" "+itemCount +" " + searchableRestaurants.size)
                Log.d("RESTTTT", rest.toString())
                Glide.with(holder.itemView.context)
                        .load(rest.image_url)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(holder.img).view
                holder.title.text = rest.name
                holder.price.text = rest.price.toString()
                holder.address.text = rest.address
                if (cisInit) {
                    val userViewModel: UserViewModel by c.viewModels {
                        UserViewModelFactory((c.application as ProjectDatabaseApp).repository)
                    }
                    Log.d("zzz", "entered")
                    if (isFavouriteForLoggedIn(rest.id)) {
                        holder.fav.setImageResource(android.R.drawable.btn_star_big_on)

                    } else {
                        holder.fav.setImageResource(android.R.drawable.btn_star_big_off)

                    }
                    holder.fav.setOnClickListener {
                        if (isFavouriteForLoggedIn(rest.id)) {
                            holder.fav.setImageResource(android.R.drawable.btn_star_big_off)
                            
                            for(i in favourites){
                                if(i.restaurantId==rest.id && i.email==MainActivity.logged_in_id){
                                    favourites.remove(i)
                                    break
                                }
                            }
                            userViewModel.deleteFavourites(MainActivity.logged_in_id, rest.id)

                        } else {
                            holder.fav.setImageResource(android.R.drawable.btn_star_big_on)
                            favourites.add(UserFavourites(0, MainActivity.logged_in_id, rest.id))
                            userViewModel.insertFavourites(MainActivity.logged_in_id, rest.id)
                        }

                    }
                }


                holder.lay.setOnClickListener {
                    if (!searchableRestaurants.isEmpty()) {
                        val rest = searchableRestaurants[position]
                        val bundle = bundleOf(
                                "id" to rest.id,
                                "name" to rest.name,
                                "address" to rest.address,
                                "city" to rest.city,
                                "state" to rest.state,
                                "area" to rest.area,
                                "postal_code" to rest.postal_code,
                                "country" to rest.country,
                                "price" to rest.price,
                                "lat" to rest.lat,
                                "lng" to rest.lng,
                                "phone" to rest.phone,
                                "reserve_url" to rest.reserve_url,
                                "mobile_reserve_url" to rest.mobile_reserve_url,
                                "image_url" to rest.image_url

                        )
                        Log.d("asdFF", rest.image_url)
                        if (cisInit)
                            c.findNavController(R.id.nestedScroll).navigate(R.id.action_scrollingFragment_to_detailFragment, bundle)
                    }
                }
            }catch(e: Exception){
                Log.d("EXCEPTION", e.toString())
            }
        }
    }

    override fun getItemCount(): Int = searchableRestaurants.size

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val img: ImageView=view.findViewById(R.id.item_image)
        val title:TextView=view.findViewById(R.id.item_title)
        val address: TextView=view.findViewById(R.id.item_address)
        val price: TextView=view.findViewById(R.id.item_price)
        val fav: ImageButton=view.findViewById(R.id.item_favourite)
        val lay: ConstraintLayout =view.findViewById(R.id.item_layout)

        override fun toString(): String {
            return super.toString()
        }
    }

    fun setData(restaurants: List<Restaurant>) {
        var count=0
        var restaurants2= listOf<Restaurant>()
        Log.d("Vanish2", restaurants.size.toString())
        val start = (MainActivity.currentPage-1) * MainActivity.page_entries
        Log.d("vanish3", start.toString())
        for(i in ((MainActivity.currentPage-1)*MainActivity.page_entries) until (restaurants.size)){
            ++count
            Log.d("Vanish1", count.toString() + " " + i.toString() +
                    " "+((MainActivity.currentPage-1)*MainActivity.page_entries)+" "+restaurants.size)
            try {
                restaurants2 += restaurants[i]
            }catch(e:java.lang.Exception){
                Log.d("excp2", e.toString())
                break
            }
            if(count>=MainActivity.page_entries){
                break
            }
        }

        this.restaurants = FirstFragment.list
        this.searchableRestaurants = restaurants2.toMutableList()
        Log.d("Vanish?", searchableRestaurants.size.toString()+" "+MainActivity.currentPage)
        if(searchableRestaurants.size==0 || searchableRestaurants.isEmpty())
            searchableRestaurants=restaurants.toMutableList()
        notifyDataSetChanged()
    }

    fun setFavourite(fav: List<UserFavourites>){
        favourites=fav.toMutableList()
        Log.d("zzz", favourites.toString())
        notifyDataSetChanged()
    }

    fun isFavouriteForLoggedIn(id: Long):Boolean
    {
        if(!favourites.isEmpty())
        for(i in favourites){
            if(i.email==MainActivity.logged_in_id && i.restaurantId==id)
                return true
        }
        return false
    }

    fun setActivity(a: FragmentActivity){
        c=a
        cisInit=true
        Log.d("zzz", cisInit.toString())
        Log.d("zzz", c.toString())
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            private val filterResults = FilterResults()
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                searchableRestaurants.clear()

                //none selected
                if(MainActivity.filterFlag==0){
                    searchableRestaurants=restaurants.toMutableList()
                }
                ////searchBar
                Log.d("filter", MainActivity.filterFlag.toString())
                if(MainActivity.filterFlag==1) {
                    if (constraint.isNullOrBlank()) {
                        searchableRestaurants.addAll(restaurants)
                    } else {
                        val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                        for (item in 0..restaurants.size-1) {
                            if (restaurants[item].name.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                                searchableRestaurants.add(restaurants[item])
                            }
                        }
                    }

                }
                //country
                if(MainActivity.filterFlag==2){
                    if (constraint.isNullOrBlank()) {
                        searchableRestaurants.addAll(restaurants)
                    } else {
                        val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                        for (item in 0..restaurants.size-1) {
                            if (restaurants[item].country.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                                searchableRestaurants.add(restaurants[item])
                            }
                        }
                    }
                }
                //city
                if(MainActivity.filterFlag==3){
                    if (constraint.isNullOrBlank()) {
                        searchableRestaurants.addAll(restaurants)
                    } else {
                        val filterPattern = constraint.toString().toLowerCase(Locale.ROOT).trim { it <= ' ' }
                        for (item in 0..restaurants.size-1) {
                            if (restaurants[item].city.toLowerCase(Locale.ROOT).contains(filterPattern)) {
                                searchableRestaurants.add(restaurants[item])
                            }
                        }
                    }
                }
                //favourite
                if(MainActivity.filterFlag==4){
                    if (constraint.isNullOrBlank()) {
                        searchableRestaurants.addAll(restaurants)
                        Log.d("favo", "false")
                    } else {
                        Log.d("favo", "true")
                        for (item in 0..favourites.size-1) {

                            for(jtem in 0..restaurants.size-1) {

                                if (favourites[item].email == MainActivity.logged_in_id ) {
                                    Log.d("favo", "${favourites[item]}")
                                    Log.d("favo", restaurants[jtem].toString())
                                    if(favourites[item].restaurantId==restaurants[jtem].id) {
                                        Log.d("favo2", restaurants[jtem].toString())
                                        if(!searchableRestaurants.contains(restaurants[jtem])){
                                            Log.d("favo22", itemCount.toString())
                                            searchableRestaurants.add(restaurants[jtem])
                                            Log.d("favo23", itemCount.toString())
                                        }

                                    }
                                }
                            }
                        }
                    }
                }

                return filterResults.also {
                    it.values = searchableRestaurants
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if(searchableRestaurants.isNullOrEmpty()) {
                    setData(restaurants)
                    Log.d("Vanish<", restaurants.size.toString())
                }
                else {
                    Log.d("Vanish..", searchableRestaurants.size.toString())
                    setData(searchableRestaurants)

                }
                notifyDataSetChanged()
            }
        }
    }
}