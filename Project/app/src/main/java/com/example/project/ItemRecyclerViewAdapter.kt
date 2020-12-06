package com.example.project

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.room.CoroutinesRoom
import androidx.room.Entity
import com.bumptech.glide.Glide
import com.example.project.API.ApiViewModel
import com.example.project.Database.UserFavourites
import com.example.project.Database.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import restaurant.Restaurant
import kotlin.coroutines.CoroutineContext

class ItemRecyclerViewAdapter(
        //TODO data constr

) : RecyclerView.Adapter<ItemRecyclerViewAdapter.RestaurantViewHolder>(), CoroutineScope{

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    lateinit var c: FragmentActivity
    var cisInit=false
    var restaurants: List<Restaurant> = listOf()
    lateinit var view:View
    var favourites: MutableList<UserFavourites> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {

        if(!restaurants.isEmpty()) {
            holder.fav.isEnabled = MainActivity.logged_in
            val rest = restaurants[position]
            Log.d("RESTTTT", rest.toString())
            Glide.with(holder.itemView.context)
                .load(rest.image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.img).view
            holder.title.text = rest.name
            holder.price.text = rest.price.toString()
            holder.address.text = rest.address
            if (cisInit) {
                Log.d("zzz", "entered")
                if (isFavouriteForLoggedIn(rest.id)) {
                    holder.fav.setImageResource(android.R.drawable.btn_star_big_on)

                } else {
                    holder.fav.setImageResource(android.R.drawable.btn_star_big_off)

                }
                holder.fav.setOnClickListener {
                    if (isFavouriteForLoggedIn(rest.id)) {
                        holder.fav.setImageResource(android.R.drawable.btn_star_big_off)
                        favourites.remove(UserFavourites(0, MainActivity.logged_in_id, rest.id))

                            c.viewModels<UserViewModel>().value.deleteFavourites(MainActivity.logged_in_id, rest.id)


                    } else {
                        holder.fav.setImageResource(android.R.drawable.btn_star_big_on)
                        favourites.add(UserFavourites(0, MainActivity.logged_in_id, rest.id))

                            c.viewModels<UserViewModel>().value.insertFavourites(MainActivity.logged_in_id, rest.id)

                    }

                }
            }
        }

        holder.lay.setOnClickListener {
            if (!restaurants.isEmpty()) {
                val rest = restaurants[position]
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
                view.findNavController().navigate(R.id.action_scrollingFragment_to_detailFragment, bundle)
            }
        }
    }

    override fun getItemCount(): Int = restaurants.size //TODO

    inner class RestaurantViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //TODO
        val img: ImageView=view.findViewById(R.id.item_image)
        val title:TextView=view.findViewById(R.id.item_title)
        val address: TextView=view.findViewById(R.id.item_address)
        val price: TextView=view.findViewById(R.id.item_price)
        val fav: ImageButton=view.findViewById(R.id.item_favourite)
        val lay: ConstraintLayout =view.findViewById(R.id.item_layout)
        //var inact=true
        override fun toString(): String {
            return super.toString()
        }
    }

    fun setData(restaurants: List<Restaurant>) {
        this.restaurants = restaurants
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
}