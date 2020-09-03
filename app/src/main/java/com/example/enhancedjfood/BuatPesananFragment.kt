package com.example.enhancedjfood

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_buat_pesanan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BuatPesananFragment : Fragment(R.layout.fragment_buat_pesanan) {
    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customerId: Int = arguments!!.getInt("customer")
        val foodId: Int = arguments!!.getInt("food")
        var foodHolder: Food? = null

        val callFood = retrofitInterface.getFoodById(foodId)
        if (callFood != null) {
            val res = callFood.enqueue(object : Callback<Food> {
                override fun onFailure(call: Call<Food>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Food>, response: Response<Food>) {
                    if (response.isSuccessful) {
                        foodHolder = Food(  response.body()!!.foodId,
                            response.body()!!.foodName,
                            response.body()!!.foodPrice,
                            response.body()!!.foodCategory,
                            response.body()!!.foodSeller)

                        food_name.text = foodHolder!!.foodName
                        food_category.text = foodHolder!!.foodCategory
                        food_price.text = foodHolder!!.foodPrice.toString()
                    }
                }
            })
        }
        
        radioGroup.setOnCheckedChangeListener { radioGroup, checkedId ->
            if (checkedId == R.id.cash){
                hitung.visibility                                   = View.INVISIBLE
                textCode.visibility                                 = View.INVISIBLE
                promo_code.visibility                               = View.INVISIBLE

                total_price.text = foodHolder!!.foodPrice.toString()
                pesan.visibility                                    = View.VISIBLE
            }
            else{
                total_price.text = "0"
                textCode.visibility                                 = View.VISIBLE
                promo_code.visibility                               = View.VISIBLE
                promo_code.isEnabled                                = true
                promo_code.text.clear()
                hitung.visibility                                   = View.VISIBLE
                pesan.visibility                                    = View.INVISIBLE
            }
        }

        hitung.setOnClickListener {
            if (promo_code.text.toString() == ""){
                total_price.text = food_price.text
            }
            else if (promo_code.text.toString() != ""){
                val inCode: String  = promo_code.text.toString()
                var promoHolder: Promo?    = null
                val callPromo = retrofitInterface.getPromoByCode(promo_code.text.toString())
                if (callPromo != null) {
                    val res = callPromo.enqueue(object : Callback<Promo> {
                        override fun onFailure(call: Call<Promo>, t: Throwable) {
                            t.printStackTrace()
                            Toast.makeText(context, "Promo not found", Toast.LENGTH_LONG).show()
                            promo_code.text.clear()
                            total_price.setText(foodHolder!!.foodPrice.toString())
                        }

                        override fun onResponse(call: Call<Promo>, response: Response<Promo>) {
                            if(response.isSuccessful && response.body() != null){
                                promoHolder = Promo(response.body()!!.promoId,
                                    response.body()!!.promoCode,
                                    response.body()!!.promoDiscount,
                                    response.body()!!.promoMinPrice,
                                    response.body()!!.promoActive)

                                if (promoHolder!!.promoActive && promoHolder!!.promoMinPrice <= foodHolder!!.foodPrice){
                                    val total: Int = foodHolder!!.foodPrice - promoHolder!!.promoDiscount
                                    Toast.makeText(context, "Promo applied", Toast.LENGTH_LONG).show()
                                    total_price.text = total.toString()
                                }
                                else if (!promoHolder!!.promoActive){
                                    Toast.makeText(context, "Promo not active", Toast.LENGTH_LONG).show()
                                    promo_code.text.clear()
                                    total_price.setText(foodHolder!!.foodPrice.toString())
                                }
                                else if (promoHolder!!.promoMinPrice > foodHolder!!.foodPrice){
                                    Toast.makeText(context, "Minimum order not reached", Toast.LENGTH_LONG).show()
                                    promo_code.text.clear()
                                    total_price.setText(foodHolder!!.foodPrice.toString())
                                }
                                else if (!promoHolder!!.promoActive && promoHolder!!.promoMinPrice > foodHolder!!.foodPrice){
                                    Toast.makeText(context, "Promo not active and minimum order not reached", Toast.LENGTH_LONG).show()
                                    promo_code.text.clear()
                                    total_price.setText(foodHolder!!.foodPrice.toString())
                                }
                            }
                        }
                    })
                }
            }
            promo_code.isEnabled = false
            hitung.visibility = View.INVISIBLE
            pesan.visibility = View.VISIBLE
        }

        pesan.setOnClickListener {
            if (cash.isChecked){
                val callCashInvoice = retrofitInterface.addCashInvoice(foodId, customerId)
                val res = callCashInvoice.enqueue(object : Callback<CashInvoice>{
                    override fun onFailure(call: Call<CashInvoice>, t: Throwable) {
                        t.printStackTrace()
                    }

                    override fun onResponse(call: Call<CashInvoice>, response: Response<CashInvoice>) {
                        if (response.isSuccessful) {
                            Toast.makeText(context, "Invoice number :" + response.body()!!.invoiceId.toString() + " created", Toast.LENGTH_LONG).show()
                            val bundle: Bundle = Bundle()
                            bundle.putInt("customer", customerId)
                            val nextFrag: MenuFragment = MenuFragment()
                            nextFrag.arguments = bundle
                            activity!!.supportFragmentManager.beginTransaction().apply {
                                replace(R.id.flFragment, nextFrag)
                                commit()
                            }
                        }
                        else if (response.code() == 500){
                            Toast.makeText(context, "Ongoing Invoice Exist, Complete or Cancel Your Order First", Toast.LENGTH_LONG).show()
                        }
                    }
                })
            }
            else if (cashless.isChecked){
                if (promo_code.text.toString() == ""){
                    val callCashlessInvoiceNoPromo = retrofitInterface.addCashlessInvoice(foodId, customerId)
                    val res = callCashlessInvoiceNoPromo.enqueue(object : Callback<CashlessInvoice>{
                        override fun onFailure(call: Call<CashlessInvoice>, t: Throwable) {
                            t.printStackTrace()
                        }

                        override fun onResponse(call: Call<CashlessInvoice>, response: Response<CashlessInvoice>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Invoice number :" + response.body()!!.invoiceId.toString() + " created", Toast.LENGTH_LONG).show()
                                val bundle: Bundle = Bundle()
                                bundle.putInt("customer", customerId)
                                val nextFrag: MenuFragment = MenuFragment()
                                nextFrag.arguments = bundle
                                activity!!.supportFragmentManager.beginTransaction().apply {
                                    replace(R.id.flFragment, nextFrag)
                                    commit()
                                }
                            }
                            else if (response.code() == 500){
                                Toast.makeText(context, "Ongoing Invoice Exist, Complete or Cancel Your Order First", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
                else if (promo_code.text.toString() != ""){
                    val callCashlessInvoiceNoPromo = retrofitInterface.addCashlessInvoice(foodId, customerId, promo_code.text.toString())
                    val res = callCashlessInvoiceNoPromo.enqueue(object : Callback<CashlessInvoice>{
                        override fun onFailure(call: Call<CashlessInvoice>, t: Throwable) {
                            t.printStackTrace()
                        }

                        override fun onResponse(call: Call<CashlessInvoice>, response: Response<CashlessInvoice>) {
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Invoice number :" + response.body()!!.invoiceId.toString() + " created", Toast.LENGTH_LONG).show()
                            }
                            else if (response.code() == 500){
                                Toast.makeText(context, "Ongoing Invoice Exist, Complete or Cancel Your Order First", Toast.LENGTH_LONG).show()
                            }
                        }
                    })
                }
            }
        }
    }
}