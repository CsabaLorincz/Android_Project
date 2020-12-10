package com.example.project.Database

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUsers: Flow<List<User>> = getAll()
    private fun getAll():Flow<List<User>>{
        Log.d("!!!3", "he<")
        return repository.allUsers
    }
    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(word: User) = viewModelScope.launch {
        repository.insert(word)
    }

    fun deleteAll()=viewModelScope.launch { repository.deleteAll() }
    //favourites
    fun getFavourites():Flow<List<UserFavourites>>{
        return repository.getFavourites()
    }
    fun deleteFavourites(userId:String, restaurantId:Long)=viewModelScope.launch {
        repository.deleteFavourite(userId, restaurantId)
    }
    val userFav:Flow<List<UserFavourites>> = getFavourites()
    fun insertFavourites(userId:String, restaurantId: Long)=viewModelScope.launch {
        repository.insertFavourites(userId, restaurantId)
    }

    fun deleteAllFav(){
        repository.deleteALLFav()
    }
}

class UserViewModelFactory(private val repository: UserRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}