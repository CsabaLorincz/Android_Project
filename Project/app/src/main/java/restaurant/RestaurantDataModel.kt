package restaurant

import androidx.lifecycle.ViewModel
import kotlin.collections.ArrayList

class RestaurantDataModel: ViewModel() {
    private val restaurantList: ArrayList<Restaurant> = arrayListOf()

    fun addRestaurant(restaurant: Restaurant) {
        restaurantList.add(restaurant)
    }

    fun getRestaurant(id: Int) = restaurantList.getOrNull(id)

    fun getAllRestaurants() = restaurantList

    fun removeRestaurant(pos: Int) = restaurantList.removeAt(pos)
}