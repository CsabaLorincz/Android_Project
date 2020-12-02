package com.example.project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.*
import restaurant.Restaurant

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


/**
 * A simple [Fragment] subclass.
 * Use the [DetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var id:Long?=null
    private var name: String?=null
    private var address: String?=null
    private var city: String?=null
    private var state: String?=null
    private var area: String?=null
    private var postal_code:Int?=null
    private var country: String?=null
    private var phone: String?=null
    private var lat:Double?=null
    private var lng: Double?=null
    private var price: Double?=null
    private var reserve_url:String?=null
    private var mobile_reserve_url:String?=null
    private var image_url:String?=null

    private var restaurant:Restaurant?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        id= arguments?.getLong("id")
        name=arguments?.getString("name")
        address=arguments?.getString("address")
        city=arguments?.getString("city")
        state=arguments?.getString("state")
        area=arguments?.getString("area")
        postal_code=arguments?.getInt("postal_code")
        country=arguments?.getString("country")
        phone=arguments?.getString("phone")
        lat=arguments?.getDouble("lat")
        lng=arguments?.getDouble("lng")
        price=arguments?.getDouble("price")
        reserve_url=arguments?.getString("reserve_url")
        mobile_reserve_url=arguments?.getString("mobile_reserve_url")
        image_url=arguments?.getString("image_url")
        Log.d("asd1", id.toString())
        Log.d("asd2", name.toString())
        Log.d("asd3", address.toString())
        Log.d("asd4", city.toString())
        Log.d("asd5", state.toString())
        Log.d("asd6", postal_code.toString())
        Log.d("asd7", country.toString())
        Log.d("asd8", phone.toString())
        Log.d("asd9", lat.toString())
        Log.d("asd10", lng.toString())
        Log.d("asd11", price.toString())
        Log.d("asd12", reserve_url.toString())
        Log.d("asd13", mobile_reserve_url.toString())
        Log.d("asd14", image_url.toString())


        if(id!=null && name!=null && address!=null && city!=null && state!=null && area!=null && postal_code!=null && country!=null && phone!=null && lat!=null && lng!=null && price!=null && reserve_url!=null && mobile_reserve_url!=null && image_url!=null) {
            restaurant = Restaurant(id!!, name!!, address!!, city!!, state!!, area!!, postal_code!!, country!!, phone!!, lat!!, lng!!, price!!, reserve_url!!, mobile_reserve_url!!, image_url!!)
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(restaurant!=null) {
            val call = view.findViewById<FloatingActionButton>(R.id.detail_call)
            val gps = view.findViewById<FloatingActionButton>(R.id.detail_gps)
            val address = view.findViewById<TextView>(R.id.detail_address)
            val area = view.findViewById<TextView>(R.id.detail_area)
            val city = view.findViewById<TextView>(R.id.detail_city)
            val country = view.findViewById<TextView>(R.id.detail_country)
            val name = view.findViewById<TextView>(R.id.detail_name)
            val price = view.findViewById<TextView>(R.id.detail_price)
            val state = view.findViewById<TextView>(R.id.detail_state)
            val img=view.findViewById<ImageView>(R.id.detail_image)
            address.text = restaurant?.address
            area.text = restaurant?.area
            city.text = restaurant?.city
            country.text = restaurant?.country
            name.text = restaurant?.name
            price.text = restaurant?.price.toString()
            state.text = restaurant?.state
            Glide.with(img.context)
                    .load(restaurant?.image_url)
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(img).view
            call.setOnClickListener {
                val callIntent = Intent()
                callIntent.action = Intent.ACTION_DIAL
                callIntent.data = Uri.parse("tel:$phone")
                startActivity(callIntent)
            }
            gps.setOnClickListener {
                val gmmIntentUri = Uri.parse("geo:${lat},${lng}")
                val gpsIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                gpsIntent.setPackage("com.google.android.apps.maps")
                startActivity(gpsIntent)
            }
        }
    }

    /*companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        /*@JvmStatic
        fun newInstance(param1: Restaurant) =
                DetailFragment().apply {
                    arguments = Bundle().apply {
                        put("restaurant", param1)
                    }
                }*/
    }*/
}