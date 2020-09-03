package com.example.enhancedjfood

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ExpandableListView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuFragment : Fragment(R.layout.fragment_menu) {
    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }
    val listSeller: ArrayList<Seller> = ArrayList<Seller>()
    val listFood: ArrayList<Food> = ArrayList<Food>()
    val childMapping: HashMap<Seller, ArrayList<Food>> = HashMap<Seller, ArrayList<Food>>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customerId: Int = arguments!!.getInt("customer")
        refreshList()
        lvExp.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val selected: Food = childMapping.get(listSeller.get(groupPosition))!!.get(childPosition)
            val bundle: Bundle = Bundle()
            bundle.putInt("customer", customerId)
            bundle.putInt("food", selected.foodId)
            val nextFrag: BuatPesananFragment = BuatPesananFragment()
            nextFrag.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, nextFrag).addToBackStack("login")
                commit()
            }
            false
        }
        pesanan.setOnClickListener {
            val bundle: Bundle = Bundle()
            bundle.putInt("customer", customerId)
            val nextFrag: PesananFragment = PesananFragment()
            nextFrag.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, nextFrag).addToBackStack("login")
                commit()
            }
        }
    }
    fun refreshList(){
        val call = retrofitInterface.getFoods()
        if (call != null) {
            val res = call.enqueue(object : Callback<ArrayList<Food>> {
                override fun onFailure(call: Call<ArrayList<Food>>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<ArrayList<Food>>, responses: Response<ArrayList<Food>>) {
                    if (responses.isSuccessful) {
                        Toast.makeText(context, "Please enter your order", Toast.LENGTH_LONG).show()
                        for (response in responses.body()!!){
                            val foodHolder: Food = Food(response.foodId,
                                response.foodName,
                                response.foodPrice,
                                response.foodCategory,
                                response.foodSeller)
                            val locationHolder: Location = Location(response.foodSeller.sellerLocation.locationProvince,
                                response.foodSeller.sellerLocation.locationDescription,
                                response.foodSeller.sellerLocation.locationCity)
                            val sellerHolder: Seller = Seller(  response.foodSeller.sellerId,
                                response.foodSeller.sellerName,
                                response.foodSeller.sellerEmail,
                                response.foodSeller.sellerPhoneNumber,
                                locationHolder)

                            var checker: Boolean = true
                            for (seller in listSeller){
                                if (sellerHolder.sellerName == seller.sellerName){
                                    checker = false
                                }
                            }
                            if (checker){
                                listSeller.add(sellerHolder)
                            }
                            listFood.add(foodHolder)
                        }
                        for (seller in listSeller) {
                            val temp: ArrayList<Food> = ArrayList<Food>()
                            for (food in listFood){
                                if (food.foodSeller.sellerName.equals(seller.sellerName)){
                                    temp.add(food)
                                }
                            }
                            childMapping.put(seller, temp)
                        }
                        val listAdapter: MainListAdapter = MainListAdapter(this@MenuFragment, listSeller, childMapping)
                        lvExp.setAdapter(listAdapter)
                    }
                }
            })
        }
    }
}