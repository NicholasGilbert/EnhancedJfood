package com.example.enhancedjfood

class Promo (id: Int, code: String, discount: Int, minPrice: Int, active: Boolean) {
    var promoId = id
    var promoCode = code
    var promoDiscount = discount
    var promoMinPrice = minPrice
    var promoActive = active
    init{
        val display: String = "Promo ${promoCode} \n " +
                "ID             : " + promoId       +" \n " +
                "Promo Discount : " + promoDiscount +" \n " +
                "Minimum Price  : " + promoMinPrice +" \n " +
                "Active Status  : " + promoActive   +" \n "
        println(display)
    }
    fun show(){
        val display: String = "Promo ${promoCode} \n " +
                "ID             : " + promoId       +" \n " +
                "Promo Discount : " + promoDiscount +" \n " +
                "Minimum Price  : " + promoMinPrice +" \n " +
                "Active Status  : " + promoActive   +" \n "
        println(display)
    }
}