package com.example.enhancedjfood

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.fragment_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuByCategoryFragment : Fragment(R.layout.fragment_menu_by_category) {
    private val retrofitInterface by lazy{
        RetrofitInterface.create()
    }
    val listCategory: ArrayList<String> = ArrayList<String>()
    val listFood: ArrayList<Food> = ArrayList<Food>()
    var childMapping: HashMap<String, ArrayList<Food>> = HashMap<String, ArrayList<Food>>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onViewCreated(view, savedInstanceState)
        childMapping.clear()
        Toast.makeText(context, "Please enter your order", Toast.LENGTH_LONG).show()
        val customerId: Int = arguments!!.getInt("customer")
        val handler = Handler(Looper.getMainLooper())
        handler.post(object : Runnable {
            override fun run() {
                refreshList()
                handler.postDelayed(this, 30000)
            }
        })
        lvExp.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val selected: Food = childMapping.get(listCategory.get(groupPosition))!!.get(childPosition)
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

    fun createNotificationChannel(context: Context, importance: Int, showBadge: Boolean, name: String, description: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "${context.packageName}-$name"
            val channel = NotificationChannel(channelId, name, importance)
            channel.description = description
            channel.setShowBadge(showBadge)
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
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
                        val childMappingHolder: HashMap<String, ArrayList<Food>> = HashMap<String, ArrayList<Food>>()
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

                            var sellerCheck: Boolean = true
                            for (category in listCategory){
                                if (foodHolder.foodCategory == category){
                                    sellerCheck = false
                                }
                            }
                            if (sellerCheck){
                                listCategory.add(foodHolder.foodCategory)
                            }
                            var foodCheck: Boolean = true
                            for (food in listFood){
                                if (foodHolder.foodName == food.foodName){
                                    foodCheck = false
                                }
                            }
                            if (foodCheck){
                                listFood.add(foodHolder)
                            }
                        }
                        for (category in listCategory) {
                            val temp: ArrayList<Food> = ArrayList<Food>()
                            for (food in listFood){
                                if (food.foodCategory.equals(category)){
                                    temp.add(food)
                                }
                            }
                            childMappingHolder.put(category, temp)
                        }
                        if (childMappingHolder != childMapping){
//                            val diff = childMappingHolder
//                            var text: String = ""
//                            diff.keys.removeAll(childMapping.keys)
//                            diff.values.removeAll(childMapping.values)
//                            for (i in diff.keys){
//                                for (j in diff[i]!!){
//                                    text = text + j.foodName + ", "
//                                }
//                                text = text + "added by " + i.sellerName + ". "
//                            }
                            createNotificationChannel(  context!!,
                                NotificationManagerCompat.IMPORTANCE_HIGH,
                                false,
                                getString(R.string.app_name),
                                "App notification channel.")
                            val channelId = "${context!!.packageName}-${context!!.getString(R.string.app_name)}"
                            val builder = NotificationCompat.Builder(context!!, channelId).apply {
                                setSmallIcon(R.drawable.ic_launcher_foreground)
                                setContentTitle("New Food Added")
                                setContentText("There's a new food!")
                                setDefaults(NotificationCompat.DEFAULT_ALL)
                                priority = NotificationCompat.PRIORITY_HIGH
                            }
                            with(NotificationManagerCompat.from(context!!)) {
                                notify(1001, builder.build())
                            }
                            childMapping = childMappingHolder
                            val listAdapter: MainListAdapter = MainListAdapter(this@MenuByCategoryFragment, listCategory, childMapping)
                            lvExp.setAdapter(listAdapter)
                            for (sellerCount in listCategory.indices){
                                lvExp.expandGroup(sellerCount)
                            }
                        }
                    }
                }
            })
        }
    }
}