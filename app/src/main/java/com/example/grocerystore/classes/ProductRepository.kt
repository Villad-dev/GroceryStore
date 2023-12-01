package com.example.grocerystore.classes

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class ProductRepository(private val firebaseDatabase: FirebaseDatabase) {

    //val allProducts = productDao.getProducts()
    val allProducts = MutableStateFlow(HashMap<String, Product>())
    private val products: String = "products"

    init {
        firebaseDatabase.getReference().addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val product = Product(
                    id = snapshot.ref.key as String,
                    productName = snapshot.child("productName").value as String,
                    productPrice = snapshot.child("productPrice").value as Double,
                    productQuantity = snapshot.child("productQuantity").value as Int,
                    imageId = snapshot.child("imageId").value as Int,
                    boughtQuantity = snapshot.child("boughtQuantity").value as Int,
                    isBought = snapshot.child("isBought").value as Boolean
                )
                allProducts.value[product.id] = product
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val product = Product(
                    id = snapshot.ref.key as String,
                    productName = snapshot.child("productName").value as String,
                    productPrice = snapshot.child("productPrice").value as Double,
                    productQuantity = snapshot.child("productQuantity").value as Int,
                    imageId = snapshot.child("imageId").value as Int,
                    boughtQuantity = snapshot.child("boughtQuantity").value as Int,
                    isBought = snapshot.child("isBought").value as Boolean
                )
                allProducts.value[product.id] = product
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                allProducts.value = allProducts.value.toMutableMap().apply {
                    remove(snapshot.ref.key as String)
                } as HashMap<String, Product>
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("error", error.message)
            }

        })

    }

    suspend fun insert(product: Product) = firebaseDatabase.getReference(products).push().also {
        product.id = it.ref.key.toString()
        it.setValue(product)
    }

    suspend fun update(product: Map.Entry<String, Product>) {
        firebaseDatabase.getReference(products+"/${product.value.id}").also {
            it.child("productPrice").setValue(product.value.productPrice)
            it.child("productQuantity").setValue(product.value.productQuantity)
            it.child("boughtQuantity").setValue(product.value.boughtQuantity)
            it.child("productName").setValue(product.value.productName)
            it.child("isBought").setValue(product.value.isBought)
            it.child("imageId").setValue(product.value.imageId)
        }
    }

    suspend fun delete(productId: String) {
        firebaseDatabase.getReference(products+"/${productId}").removeValue()
    }
    /*  suspend fun insert(product: Product) = productDao.insertProduct(product)
      suspend fun update(product: Product) = productDao.updateProduct(product)
      suspend fun delete(product: Product) = productDao.deleteProduct(product)*/

}