package com.example.project

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ItemRecyclerViewAdapter(
        //TODO data constr

) : RecyclerView.Adapter<ItemRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        /*val food = foodList[position]
        holder.idView.text = food.id.toString()
        holder.contentView.text = food.name
        holder.titleView.text = food.type.toString()*/
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
    }

    override fun getItemCount(): Int = 20 //TODO

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //val idView: TextView = view.findViewById(R.id.item_number)
        //val contentView: TextView = view.findViewById(R.id.content)
        //val titleView: TextView = view.findViewById(R.id.titles)
        val fav: ImageButton=view.findViewById(R.id.item_favourite)
        var inact=true
        override fun toString(): String {
            return super.toString()
        }
    }
}