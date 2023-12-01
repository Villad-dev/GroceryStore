package com.example.grocerystore.classes

import androidx.room.Entity
import androidx.room.PrimaryKey

//@Entity
class Product(

   // @PrimaryKey(autoGenerate = true)
    var id : String,
    var productName : String,
    var productPrice : Double,
    var productQuantity : Int,
    var imageId : Int,
    var boughtQuantity : Int = 0,
    var isBought : Boolean = false
){
}