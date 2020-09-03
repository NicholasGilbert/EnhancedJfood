package com.example.enhancedjfood

class CashInvoice : Invoice{
    var cashInvoiceDelivery: Int? = null

    constructor(id: Int, food: Food, date: String, total: Int, customer: Customer, status: InvoiceStatus): super (id, food, date, total, customer, PaymentType.Cash, status){
        setTotalPrice()
        showNoDelivery()
    }
    constructor(id: Int, food: Food, date: String, total: Int, customer: Customer, status: InvoiceStatus, delivery: Int): super (id, food , date, total, customer, PaymentType.Cash, status){
        cashInvoiceDelivery = delivery
        setTotalPrice()
        showDelivery()
    }
    fun setTotalPrice(){
        if (cashInvoiceDelivery == null){
            invoiceTotalPrice = invoicefood.foodPrice
        }
        else{
            invoiceTotalPrice = invoicefood.foodPrice + cashInvoiceDelivery!!
        }
    }
    fun showDelivery(){
        val display: String =   "Cash Invoice ${invoiceId.toString()}  \n " +
                "ID         : " + invoiceId        +" \n " +
                "Food ID    : " + invoicefood.foodName     +" \n " +
                "Date       : " + invoiceDate +" \n " +
                "Price      : " + invoiceTotalPrice  +" \n" +
                "Customer   : " + invoiceCustomer.customerName  +" \n" +
                "Payment    : " + invoicePaymentType  +" \n" +
                "Status     : " + invoiceStatus  +" \n" +
                "Delivery   : " + cashInvoiceDelivery  +" \n"
        println(display)
    }
    fun showNoDelivery(){
        val display: String =   "Cash Invoice ${invoiceId.toString()}  \n " +
                "ID         : " + invoiceId        +" \n " +
                "Food ID    : " + invoicefood.foodName     +" \n " +
                "Date       : " + invoiceDate +" \n " +
                "Price      : " + invoiceTotalPrice  +" \n" +
                "Customer   : " + invoiceCustomer.customerName  +" \n" +
                "Payment    : " + invoicePaymentType  +" \n" +
                "Status     : " + invoiceStatus  +" \n"
        println(display)
    }
}