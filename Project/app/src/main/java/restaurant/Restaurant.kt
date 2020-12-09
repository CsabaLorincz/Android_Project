package restaurant

class Restaurant(val id:Long, val name: String, val address: String, val city: String, val state: String?,
    val area: String, val postal_code:String, val country: String, val phone: String, val lat:Double,
        val lng: Double, val price: Double, val reserve_url:String,val mobile_reserve_url:String, val image_url:String) {


    override fun toString(): String {
        return "Restaurant(id=$id, name='$name', address='$address', city='$city', state='$state', area='$area', postal_code=$postal_code, country='$country', phone='$phone', lat=$lat, lng=$lng, price=$price, reserve_url='$reserve_url', mobile_reserve_url='$mobile_reserve_url', image_url='$image_url')"
    }

}