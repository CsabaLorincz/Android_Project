package com.example.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.project.Database.*
import com.example.project.MainActivity.Companion.logged_in
import com.example.project.MainActivity.Companion.setLoggedId
import com.example.project.MainActivity.Companion.setLogin
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.lang.Integer.parseInt
import kotlin.coroutines.CoroutineContext


class SecondFragment : Fragment(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()
    val vm: UserViewModel by viewModels {
        UserViewModelFactory((activity?.application as ProjectDatabaseApp).repository)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        if(!logged_in)
            return inflater.inflate(R.layout.fragment_second, container, false)
        return inflater.inflate(R.layout.fragment_second_display, container, false)
    }
    var list:List<User>? = listOf()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(!logged_in) {
            launch{
                if(list==null ||list?.size==0) {
                    list=vm.allUsers.first()
                }
            }
            val login=view.findViewById<Button>(R.id.button_login)
            val register=view.findViewById<Button>(R.id.button_register)
            register.isEnabled=false

            view.findViewById<ImageButton>(R.id.login_back).setOnClickListener {
                findNavController().navigate(R.id.action_SecondFragment_to_scrollingFragment)
            }
            val email=view.findViewById<EditText>(R.id.login_email)
            val name=view.findViewById<EditText>(R.id.login_name)
            val phone=view.findViewById<EditText>(R.id.login_phone)
            val address=view.findViewById<EditText>(R.id.login_address)
            login.setOnClickListener{
                    if (list != null) {
                        for(i in list!!){
                            Log.d("!!!3", i.toString())
                            if(email.text.toString()==i.email){
                                setLogin(true)
                                setLoggedId(email.text.toString())
                                val bundle= bundleOf("email" to i.email,
                                        "name" to i.name,
                                        "address" to i.address,
                                        "phone" to i.phone)
                                findNavController().navigate(R.id.action_SecondFragment_self, bundle)
                            }
                        }
                    } else {
                        Log.d("!!!3", "sad")
                    }
            }

            email.addTextChangedListener {
                register.isEnabled=isValidRegisterData(email.text.toString(), name.text.toString(), phone.text.toString(), address.text.toString())
            }
            name.addTextChangedListener {
                register.isEnabled=isValidRegisterData(email.text.toString(), name.text.toString(), phone.text.toString(), address.text.toString())
            }
            phone.addTextChangedListener {
                register.isEnabled=isValidRegisterData(email.text.toString(), name.text.toString(), phone.text.toString(), address.text.toString())
            }
            address.addTextChangedListener {
                register.isEnabled=isValidRegisterData(email.text.toString(), name.text.toString(), phone.text.toString(), address.text.toString())
            }
            register.setOnClickListener{

                   val posUser = User(0, name.text.toString(), email.text.toString(), phone.text.toString(), address.text.toString())

                   if (list != null) {

                       var bool=true
                       for(i in list!!){
                           Log.d("!!!2", i.toString())
                            if(posUser.email==i.email || posUser.name==i.name || posUser.phone==i.phone){
                               bool=false
                            }
                       }
                       if(bool) {
                           vm.insert(posUser)
                           val bundle = bundleOf(
                               "email" to email.text.toString(),
                               "name" to name.text.toString(),
                               "address" to address.text.toString(),
                               "phone" to phone.text.toString()
                           )
                           setLogin(true)
                           setLoggedId(email.text.toString())
                           findNavController().navigate(R.id.action_SecondFragment_self, bundle)
                       }
                   } else {
                       Log.d("!!!2", "sad")
                   }
            }
        }
        else{
            view.findViewById<Button>(R.id.button_logout).setOnClickListener{
                setLogin(false)
                setLoggedId("-1")
                findNavController().navigate(R.id.action_SecondFragment_self)
            }
            Log.d("...", logged_in.toString())
            view.findViewById<ImageButton>(R.id.user_back).setOnClickListener {
                view.findNavController().navigate(R.id.action_SecondFragment_to_scrollingFragment)
            }
            val email=arguments?.getString("email")
            val name=arguments?.getString("name")
            val phone=arguments?.getString("phone")
            val address=arguments?.getString("address")
            if(email.isNullOrEmpty())
            {
                launch {
                    Log.d("email?", "email?")
                    if (list == null || list?.size == 0) {
                        list=vm.allUsers.first()
                    }
                    if (list != null) {
                        for (i in list!!) {
                            if (i.email == MainActivity.logged_in_id) {
                                view.findViewById<TextView>(R.id.user_address).text = i.address
                                view.findViewById<TextView>(R.id.user_name).text = i.name
                                view.findViewById<TextView>(R.id.user_phone).text = i.phone
                                view.findViewById<TextView>(R.id.user_email).text = i.email
                            }
                        }
                    }
                }
            }
            else{
                view.findViewById<TextView>(R.id.user_address).text=address
                view.findViewById<TextView>(R.id.user_name).text=name
                view.findViewById<TextView>(R.id.user_phone).text=phone
                view.findViewById<TextView>(R.id.user_email).text=email
            }
        }
    }

    private fun isValidRegisterData(email: String, name:String, phone: String, address:String):Boolean {
            if (!isEmailValid(email)) {
                return false
            }
            if (!isNameValid(name)) {
                return  false
            }
            if (!isPhoneValid(phone)) {
                return false
            }
            if (!isAddressValid(address)) {
               return false
            }
        return true
    }
    private fun isEmailValid(email:String):Boolean{
        val reg=Regex("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
        return reg.matches(email)
    }
    private fun isNameValid(name:String):Boolean{
        if(name.isBlank())
            return false
        return true
    }
    private fun isPhoneValid(phone:String):Boolean{
        try {
            parseInt(phone)
            return true
        } catch (e: NumberFormatException) {

        }
        return false
    }
    private fun isAddressValid(address:String):Boolean{
        val reg=Regex("^[0-9]+ [\\w ]+\$")
        return reg.matches(address)
    }
}