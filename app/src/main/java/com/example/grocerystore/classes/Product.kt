package com.example.grocerystore.classes

//@Entity
class Product(
    var id : String,
    var productName : String,
    var productPrice : String,
    var productQuantity : Long,
    var imageId : Long,
    var boughtQuantity : Long = 0,
    var bought : Boolean = false
)