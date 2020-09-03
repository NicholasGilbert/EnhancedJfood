package com.example.enhancedjfood

class CashlessInvoice : Invoice {
    var cashlessInvoicePromo: Promo? = null
    constructor(id: Int, food: Food, date: String, total: Int, customer: Customer, status: InvoiceStatus): super (id, food, date, total, customer, PaymentType.Cashless, status){
        setTotalPrice()
        showNoPromo()
    }
    constructor(id: Int, food: Food, date: String, total: Int, customer: Customer, status: InvoiceStatus, promo: Promo): super (id, food,  date, total, customer, PaymentType.Cashless, status){
        cashlessInvoicePromo = promo
        setTotalPrice()
        showPromo()
    }
    fun setTotalPrice(){
        if(cashlessInvoicePromo != null && cashlessInvoicePromo!!.promoActive){
            if (invoicefood.foodPrice > cashlessInvoicePromo!!.promoMinPrice){
                invoiceTotalPrice = invoicefood.foodPrice - cashlessInvoicePromo!!.promoDiscount
            }
            else{
                println("Minimum purchase not reached")
                invoiceTotalPrice = invoicefood.foodPrice
            }
        }
        else{
            println("No promo eligible")
            invoiceTotalPrice = invoicefood.foodPrice
        }
    }
    fun showPromo(){
        val display: String =   "Cashless Invoice ${invoiceId.toString()}  \n " +
                "ID         : " + invoiceId        +" \n " +
                "Food ID    : " + invoicefood.foodName     +" \n " +
                "Date       : " + invoiceDate +" \n " +
                "Price      : " + invoiceTotalPrice  +" \n" +
                "Customer   : " + invoiceCustomer.customerName  +" \n" +
                "Payment    : " + invoicePaymentType  +" \n" +
                "Status     : " + invoiceStatus  +" \n" +
                "Promo      : " + cashlessInvoicePromo!!.promoCode  +" \n"
        println(display)
    }

    fun showNoPromo(){
        val display: String =   "Cashless Invoice ${invoiceId.toString()}  \n " +
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