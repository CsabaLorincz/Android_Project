package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text
import restaurant.Restaurant

class ItemRecyclerViewAdapter(
        //TODO data constr
        val restaurants: ArrayList<Restaurant>
) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {
    lateinit var view:View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //TODO set data
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
        holder.img.setOnClickListener{
            view.findNavController().navigate(R.id.action_scrollingFragment_to_detailFragment)
        }
        holder.title.setOnClickListener{
            view.findNavController().navigate(R.id.action_scrollingFragment_to_detailFragment)
        }
        holder.address.setOnClickListener{
            view.findNavController().navigate(R.id.action_scrollingFragment_to_detailFragment)
        }
        holder.price.setOnClickListener{
            view.findNavController().navigate(R.id.action_scrollingFragment_to_detailFragment)
        }
    }

    override fun getItemCount(): Int = 20 //TODO

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //TODO
        val img: ImageView=view.findViewById(R.id.item_image)
        val title:TextView=view.findViewById(R.id.item_title)
        val address: TextView=view.findViewById(R.id.item_address)
        val price: TextView=view.findViewById(R.id.item_price)
        val fav: ImageButton=view.findViewById(R.id.item_favourite)

        var inact=true
        override fun toString(): String {
            return super.toString()
        }
    }
}