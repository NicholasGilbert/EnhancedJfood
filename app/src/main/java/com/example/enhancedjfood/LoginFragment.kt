package com.example.enhancedjfood

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnlogin.setOnClickListener {
            val inEmail:    String = etemail.text.toString()
            val inPass:     String = etpassword.text.toString()
            login(inEmail, inPass)
        }
    }

    fun login(paramEmail: String, paramPass: String){
        val call = retrofitInterface.login(paramEmail, paramPass)
        val res = call!!.enqueue(object : Callback<Customer> {
            override fun onFailure(call: Call<Customer>, t: Throwable) {
                Toast.makeText(context, "Login Failed", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Customer>, response: Response<Customer>) {
                if (response.isSuccessful){
                    Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                    val bundle: Bundle = Bundle()
                    bundle.putInt("customer", response.body()!!.customerId)
                    val nextFrag: MenuFragment = MenuFragment()
                    nextFrag.arguments = bundle
                    activity!!.supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, nextFrag).addToBackStack("login")
                        commit()
                    }
                }
                else{
                    Toast.makeText(context, "Login Failed", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }
}