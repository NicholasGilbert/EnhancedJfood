package com.example.enhancedjfood

class Customer (id: Int, name: String, email: String, passsword: String, date: String){
    var customerId = id
    var customerName = name
    var customerEmail= email
    var customerPassword = passsword
    var customerJoindate = date
    init{
        val display: String =   "Customer ${customerName}  \n " +
                "ID         : " + customerId        +" \n " +
                "e-mail     : " + customerEmail     +" \n " +
                "Password   : " + customerPassword +" \n " +
                "Joined on  : " + customerJoindate  +" \n"
        println(display)
    }
    fun show(){
        val display: String =   "Customer ${customerName}  \n " +
                "ID         : " + customerId        +" \n " +
                "e-mail     : " + customerEmail     +" \n " +
                "Password   : " + customerPassword +" \n " +
                "Joined on  : " + customerJoindate  +" \n"
        println(display)
    }
}