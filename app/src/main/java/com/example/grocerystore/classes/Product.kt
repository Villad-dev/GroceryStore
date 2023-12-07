package com.example.grocerystore.classes

data class Product(
    var id: String = "",
    var productName: String = "",
    var productPrice: String = "",
    var productQuantity: Long = 0,
    var boughtQuantity: Long = 0,
    var imageId: Long,
    var bought: Boolean = false
) {
    constructor() : this("", "", "", 0, 0, 0, false)
}