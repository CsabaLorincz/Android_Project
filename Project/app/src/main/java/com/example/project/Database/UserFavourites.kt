package com.example.project.Database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "user_favourites")
data class UserFavourites(@PrimaryKey(autoGenerate = true) val id:Int, val email:String,  val restaurantId: Long) {
    override fun toString(): String {
        return "UserFavourites(id=$id, email='$email', restaurantId=$restaurantId)"
    }
}