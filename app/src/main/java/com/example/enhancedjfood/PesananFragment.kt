package com.example.enhancedjfood

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import kotlinx.android.synthetic.main.fragment_pesanan.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PesananFragment : Fragment(R.layout.fragment_pesanan) {
    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }
    val listDate: ArrayList<String> = ArrayList<String>()
    val listInvoice: ArrayList<Invoice> = ArrayList<Invoice>()
    val childMapping: HashMap<String, ArrayList<Invoice>> = HashMap<String, ArrayList<Invoice>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val customerId: Int = arguments!!.getInt("customer")
        refreshList(customerId)
        explv.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val selected: Invoice = childMapping.get(listDate.get(groupPosition))!!.get(childPosition)
            val bundle: Bundle = Bundle()
            bundle.putInt("customer", customerId)
            bundle.putInt("invoice", selected.invoiceId)
            bundle.putString("type", selected.invoicePaymentType.toString())
            val nextFrag: DetailPesananFragment = DetailPesananFragment()
            nextFrag.arguments = bundle
            activity!!.supportFragmentManager.beginTransaction().apply {
                replace(R.id.flFragment, nextFrag).addToBackStack("login")
                commit()
            }
            false
        }
    }
    fun refreshList(id: Int){
        val call = retrofitInterface.getCustomerInvoice(id)
        val res = call!!.enqueue(object : Callback<ArrayList<Invoice>> {
            override fun onFailure(call: Call<ArrayList<Invoice>>, t: Throwable) {
                t.printStackTrace()
            }

            override fun onResponse(call: Call<ArrayList<Invoice>>, responses: Response<ArrayList<Invoice>>) {
                if(responses.isSuccessful){
                    for (response in responses.body()!!){
//                        if (response.invoicePaymentType.toString() == "Cash"){
                        val invoiceHolder: Invoice = Invoice(response.invoiceId,
                            response.invoicefood,
                            response.invoiceDate,
                            response.invoiceTotalPrice,
                            response.invoiceCustomer,
                            response.invoicePaymentType,
                            response.invoiceStatus)
                        var checker: Boolean = true
                        for (date in listDate){
                            if (date == invoiceHolder.invoiceDate){
                                checker = false
                            }
                        }
                        if (checker){
                            listDate.add(invoiceHolder.invoiceDate)
                        }
                        listInvoice.add(invoiceHolder)
//                        }
                    }
                    for (date in listDate) {
                        val temp: ArrayList<Invoice> = ArrayList<Invoice>()
                        for (invoice in listInvoice){
                            if (invoice.invoiceDate.equals(date)){
                                temp.add(invoice)
                            }
                        }
                        childMapping.put(date, temp)
                    }
                    val listAdapter: ListPesananAdapter = ListPesananAdapter(this@PesananFragment, listDate, childMapping)
                    explv.setAdapter(listAdapter)
                }
            }
        })
    }
}