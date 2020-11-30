package com.example.project

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.project.API.ApiViewModel
import org.w3c.dom.Text
import restaurant.Restaurant

class ItemRecyclerViewAdapter(
        //TODO data constr

) : RecyclerView.Adapter<ItemRecyclerViewAdapter.RestaurantViewHolder>() {
    var restaurants: List<Restaurant> = listOf()
    lateinit var view:View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return RestaurantViewHolder(view)
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        //TODO set data
        if(!restaurants.isEmpty()) {
            val rest = restaurants[position]
            Log.d("RESTTTT", rest.toString())
            Glide.with(holder.itemView.context)
                .load(rest.image_url)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(holder.img).view
            holder.title.text=rest.name
            holder.price.text=rest.price.toString()
            holder.address.text=rest.address
        }
        holder.fav.setOnClickListener{
            if(holder.inact==true) {
                holder.fav.setImageResource(android.R.drawable.btn_star_big_on)
                holder.inact = false
            }
            else{
                holder.fav.setImageResource(android.R.drawable.btn_star_big_off)
                holder.inact = true
            }
        }
        holder.lay.setOnClickListener {
            if (!restaurants.isEmpty()) {
                val rest = restaurants[position]
                val bundle = bundleOf(
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
                    "fav" to holder.inact
                )
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
        val lay:LinearLayout=view.findViewById(R.id.item_layout)
        var inact=true
        override fun toString(): String {
            return super.toString()
        }
    }

    fun setData(restaurants: List<Restaurant>) {
        this.restaurants = restaurants
        notifyDataSetChanged()
    }
}