package com.example.enhancedjfood

open class Invoice (id: Int, food: Food, date: String, total: Int, customer: Customer, paymentType: PaymentType, status: InvoiceStatus) {
    var invoiceId = id
    var invoicefood = food
    var invoiceDate= date
    var invoiceTotalPrice = total
    var invoiceCustomer = customer
    var invoicePaymentType = paymentType
    var invoiceStatus = status
    fun show(){
        val display: String =   "Invoice ${invoiceId.toString()}  \n " +
                "ID         : " + invoiceId        +" \n " +
                "Food ID    : " + invoicefood.foodName+" \n " +
                "Date       : " + invoiceDate +" \n " +
                "Price      : " + invoiceTotalPrice  +" \n" +
                "Customer   : " + invoiceCustomer.customerName  +" \n" +
                "Payment    : " + invoicePaymentType  +" \n" +
                "Status     : " + invoiceStatus  +" \n"
        println(display)
    }
}