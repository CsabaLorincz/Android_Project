package com.example.project.Database

import android.util.Log
import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class UserRepository(private val UserDao: UserDao) {

    val allUsers: Flow<List<User>> =getAll()
    private fun getAll():Flow<List<User>>{
        Log.d("!!!4", "asd")
        return UserDao.getUsers()
    }
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
    fun deleteALLFav(){
        UserDao.deleteAllFav()
    }
}