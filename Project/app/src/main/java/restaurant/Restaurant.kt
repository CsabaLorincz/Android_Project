package restaurant

class Restaurant(val id:Long, val name: String, val address: String, val city: String, val state: String,
    val area: String, val postal_code:Int, val country: String, val phone: Int, val lat:Double,
        val lng: Double, val price: Double, val reserve_url:String, val image_url:String) {
}