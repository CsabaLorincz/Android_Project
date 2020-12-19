package com.example.project.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface UserDao {
    @Query("SELECT * FROM user_table ORDER BY name ASC")
    fun getUsers(): Flow<List<User>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(word: User)


    @Query("DELETE FROM user_table")
    fun deleteAll()

    //userFavourites
    @Query("SELECT * FROM user_favourites ORDER BY restaurantId ASC")
    fun getFavouritesForAll(): Flow<List<UserFavourites>>

    @Query("DELETE FROM user_favourites where email=:userId AND restaurantId=:restaurantID")
    fun deleteFavourite(userId:String, restaurantID:Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavourite(userFavourites: UserFavourites)

    @Query("DELETE FROM user_favourites")
    fun deleteAllFav()
}