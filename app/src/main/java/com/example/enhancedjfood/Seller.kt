package com.example.enhancedjfood

class Seller (id: Int, name: String, email: String, phoneNumber: String, location: Location) {
    var sellerId = id
    var sellerName = name
    var sellerEmail= email
    var sellerPhoneNumber = phoneNumber
    var sellerLocation = location
    init{
        val display: String =   "Seller ${sellerName}  \n " +
                "ID             : " + sellerId        +" \n " +
                "e-mail         : " + sellerEmail     +" \n " +
                "Phone Number   : " + sellerPhoneNumber  +" \n" +
                "Location       : " + sellerLocation.locationCity  +" \n"
        println(display)
    }
    fun show(){
        val display: String =   "Seller ${sellerName}  \n " +
                "ID             : " + sellerId        +" \n " +
                "e-mail         : " + sellerEmail     +" \n " +
                "Phone Number   : " + sellerPhoneNumber  +" \n" +
                "Location       : " + sellerLocation.locationCity  +" \n"
        println(display)
    }
}