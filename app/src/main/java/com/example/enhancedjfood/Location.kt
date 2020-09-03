package com.example.enhancedjfood

class Location (province: String, description: String, city: String){
    var locationProvince = province
    var locationDescription= description
    var locationCity = city
    init{
        val display: String =   "Province of ${locationProvince}  \n " +
                "Description: " + locationDescription        +" \n " +
                "City       : " + locationCity  +" \n"
        println(display)
    }
    fun show(){
        val display: String =   "Province of ${locationProvince}  \n " +
                "Description: " + locationDescription        +" \n " +
                "City         " + locationCity  +" \n"
        println(display)
    }
}