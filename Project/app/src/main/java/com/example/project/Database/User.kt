package com.example.project.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(@PrimaryKey(autoGenerate = true) val id: Int, val name:String, val email:String, val phone: String, val address: String) {
    override fun toString(): String {
        return "User(id=$id, name='$name', email='$email', phone='$phone', address='$address')"
    }
}