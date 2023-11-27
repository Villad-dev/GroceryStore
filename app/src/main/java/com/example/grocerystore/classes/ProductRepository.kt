package com.example.grocerystore.classes

class ProductRepository (private val productDao: ProductDao ){

    val allProducts = productDao.getProducts()

    suspend fun insert(product: Product) = productDao.insertProduct(product)
    suspend fun update(product: Product) = productDao.updateProduct(product)
    suspend fun delete(product: Product) = productDao.deleteProduct(product)

}