package com.example.enhancedjfood

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface RetrofitInterface {
    companion object{
        fun create(): RetrofitInterface{
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .baseUrl("http://192.168.0.9:8080/")
//                .baseUrl("http://192.168.43.99:8080/")
                .build()

            return retrofit.create(RetrofitInterface::class.java)
        }
    }

    @POST("customer/register")
    fun regis(@Query("name")        name    : String,
              @Query("email")       email   : String,
              @Query("password")    pass    : String): Call<Customer>?

    @POST("customer/login")
    fun login(@Query("email")       email   : String,
              @Query("password")    pass    : String): Call<Customer>?

    @GET("food/getfoods")
    fun getFoods(): Call<ArrayList<Food>>?

    @GET("food/{id}")
    fun getFoodById(@Path("id")     id      : Int): Call<Food>?

    @GET("customer/{id}")
    fun getCustomerById(@Path("id") id      : Int): Call<Customer>?

    @GET("invoice/{id}")
    fun getInvoiceById(@Path("id") id      : Int): Call<Invoice>?

    @PUT("invoice/{id}")
    fun changeStatus(@Path("id")         id      : Int,
                     @Query("status")    status  : String): Call<Invoice>?

    @GET("invoice/{id}")
    fun getCashInvoiceById(@Path("id") id      : Int): Call<CashInvoice>?

    @GET("invoice/{id}")
    fun getCashlessInvoiceById(@Path("id") id      : Int): Call<CashlessInvoice>?

    @GET("promo/{code}")
    fun getPromoByCode(@Path("code")code    : String): Call<Promo>?

    @POST("invoice/cashorder")
    fun addCashInvoice(@Query("food")      food    : Int,
                       @Query("customer")  customer: Int,
                       @Query("delivery")  delivery: Int = 0): Call<CashInvoice>

    @POST("invoice/cashlessorder")
    fun addCashlessInvoice(@Query("food")      food    : Int,
                           @Query("customer")  customer: Int,
                           @Query("promo")     promo   : String = ""): Call<CashlessInvoice>

    @GET("invoice/customer/{id}")
    fun getCustomerInvoice(@Path("id")     id      : Int): Call<ArrayList<Invoice>>?
}