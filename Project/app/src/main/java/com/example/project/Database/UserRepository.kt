package com.example.project.Database

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class UserRepository(private val UserDao: UserDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allUsers: Flow<List<User>> =getAll()
    private fun getAll():Flow<List<User>>{
        Log.d("!!!4", "asd")
        return UserDao.getUsers()
    }
    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(user: User) {
        UserDao.insert(user)
    }

    suspend fun deleteAll(){
        UserDao.deleteAll()
    }
    //favourites
    fun getFavourites():Flow<List<UserFavourites>>{
        return UserDao.getFavouritesForAll()
    }
    suspend fun deleteFavourite(userId:String, restaurantID:Long){
        UserDao.deleteFavourite(userId, restaurantID)
    }
    fun insertFavourites(userId:String, restaurantID:Long){
        UserDao.insertFavourite(UserFavourites(0, userId, restaurantID))
    }
}