package com.example.enhancedjfood

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_detail_pesanan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPesananFragment : Fragment(R.layout.fragment_detail_pesanan) {
    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customerId: Int = arguments!!.getInt("customer")
        val invoiceId: Int = arguments!!.getInt("invoice")
        val invoiceType: String = arguments!!.getString("type")!!

        if (invoiceType == "Cash"){
            val callCashInvoice = retrofitInterface.getCashInvoiceById(invoiceId)
            val resInv = callCashInvoice!!.enqueue(object : Callback<CashInvoice> {
                override fun onFailure(call: Call<CashInvoice>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<CashInvoice>, response: Response<CashInvoice>) {
                    invID.text              = invoiceId.toString()
                    invDate.text            = response.body()!!.invoiceDate.substring(0,10)
                    invFoodName.text        = response.body()!!.invoicefood.foodName
                    invFoodPrice.text       = response.body()!!.invoicefood.foodPrice.toString()
                    invSeller.text          = response.body()!!.invoicefood.foodSeller.sellerName
                    invPrice.text           = response.body()!!.invoiceTotalPrice.toString()
                    invCustName.text        = response.body()!!.invoiceCustomer.customerName
                    invPayType.text         = response.body()!!.invoicePaymentType.toString()
                    staticPromoCode.visibility   = View.INVISIBLE
                    staticDis.visibility    = View.INVISIBLE
                    invPromoCode.visibility = View.INVISIBLE
                    invDis.visibility       = View.INVISIBLE
                    if (response.body()!!.cashInvoiceDelivery != null){
                        staticDel.visibility    = View.VISIBLE
                        invDel.visibility       = View.VISIBLE
                        invDel.text             = response.body()!!.cashInvoiceDelivery.toString()
                    }
                    else{
                        staticDel.visibility    = View.INVISIBLE
                        invDel.visibility       = View.INVISIBLE
                    }
                    invStat.text = response.body()!!.invoiceStatus.toString()
                    if (response.body()!!.invoiceStatus.toString() == "Finished" || response.body()!!.invoiceStatus.toString() == "Cancelled"){
                        cancel.visibility    = View.INVISIBLE
                        finish.visibility  = View.INVISIBLE
                    }
                }

            })
        }
        else{
            val callCashlessInvoice = retrofitInterface.getCashlessInvoiceById(invoiceId)
            val resInv = callCashlessInvoice!!.enqueue(object : Callback<CashlessInvoice> {
                override fun onFailure(call: Call<CashlessInvoice>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<CashlessInvoice>, response: Response<CashlessInvoice>) {
                    invID.text              = invoiceId.toString()
                    invDate.text            = response.body()!!.invoiceDate.substring(0,10)
                    invFoodName.text        = response.body()!!.invoicefood.foodName
                    invFoodPrice.text       = response.body()!!.invoicefood.foodPrice.toString()
                    invSeller.text          = response.body()!!.invoicefood.foodSeller.sellerName
                    invPrice.text           = response.body()!!.invoiceTotalPrice.toString()
                    invCustName.text        = response.body()!!.invoiceCustomer.customerName
                    invPayType.text         = response.body()!!.invoicePaymentType.toString()
                    staticDel.visibility    = View.INVISIBLE
                    invDel.visibility       = View.INVISIBLE
                    if (response.body()!!.cashlessInvoicePromo != null){
                        staticPromoCode.visibility   = View.VISIBLE
                        staticDis.visibility    = View.VISIBLE
                        invPromoCode.visibility = View.VISIBLE
                        invDis.visibility       = View.VISIBLE
                        invPromoCode.text       = response.body()!!.cashlessInvoicePromo!!.promoCode
                        invDis.text             = response.body()!!.cashlessInvoicePromo!!.promoDiscount.toString()
                    }
                    else{
                        staticPromoCode.visibility   = View.INVISIBLE
                        staticDis.visibility    = View.INVISIBLE
                        invPromoCode.visibility = View.INVISIBLE
                        invDis.visibility       = View.INVISIBLE
                    }
                    invStat.text = response.body()!!.invoiceStatus.toString()
                    if (response.body()!!.invoiceStatus.toString() == "Finished" || response.body()!!.invoiceStatus.toString() == "Cancelled"){
                        cancel.visibility    = View.INVISIBLE
                        finish.visibility  = View.INVISIBLE
                    }
                }
            })
        }

        cancel.setOnClickListener {
            val callChangeCancel = retrofitInterface.changeStatus(invoiceId, "Cancelled")
            val resInv = callChangeCancel!!.enqueue(object : Callback<Invoice> {
                override fun onFailure(call: Call<Invoice>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Invoice>, response: Response<Invoice>) {
                    val bundle: Bundle = Bundle()
                    bundle.putInt("customer", customerId)
                    bundle.putInt("invoice", invoiceId)
                    bundle.putString("type", invoiceType)
                    val nextFrag: DetailPesananFragment = DetailPesananFragment()
                    nextFrag.arguments = bundle
                    activity!!.supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, nextFrag)
                        commit()
                    }
                }
            })
        }

        finish.setOnClickListener {
            val callChangeFinished = retrofitInterface.changeStatus(invoiceId, "Finished")
            val resInv = callChangeFinished!!.enqueue(object : Callback<Invoice> {
                override fun onFailure(call: Call<Invoice>, t: Throwable) {
                    t.printStackTrace()
                }

                override fun onResponse(call: Call<Invoice>, response: Response<Invoice>) {
                    val bundle: Bundle = Bundle()
                    bundle.putInt("customer", customerId)
                    bundle.putInt("invoice", invoiceId)
                    bundle.putString("type", invoiceType)
                    val nextFrag: DetailPesananFragment = DetailPesananFragment()
                    nextFrag.arguments = bundle
                    activity!!.supportFragmentManager.beginTransaction().apply {
                        replace(R.id.flFragment, nextFrag)
                        commit()
                    }
                }
            })
        }
    }
}