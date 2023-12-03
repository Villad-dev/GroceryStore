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
        firebaseDatabase.getReference(products).addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                val product = Product(
                    id = snapshot.ref.key as String,
                    productName = snapshot.child("productName").value as String,
                    productPrice = snapshot.child("productPrice").value as String,
                    productQuantity = snapshot.child("productQuantity").value as Long,
                    imageId = snapshot.child("imageId").value as Long,
                    boughtQuantity = snapshot.child("boughtQuantity").value as Long,
                    bought = snapshot.child("bought").value as Boolean
                )
                allProducts.value = allProducts.value.toMutableMap().apply {
                    put(product.id, product)
                } as HashMap<String, Product>
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val product = Product(
                    id = snapshot.ref.key as String,
                    productName = snapshot.child("productName").value as String,
                    productPrice = snapshot.child("productPrice").value as String,
                    productQuantity = snapshot.child("productQuantity").value as Long,
                    imageId = snapshot.child("imageId").value as Long,
                    boughtQuantity = snapshot.child("boughtQuantity").value as Long,
                    bought = snapshot.child("bought").value as Boolean
                )
                allProducts.value = allProducts.value.toMutableMap().apply {
                    put(product.id, product)
                } as HashMap<String, Product>
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

    fun insert(product: Product){
        firebaseDatabase.getReference(products).push().also { reference ->
            Log.i("msg1", firebaseDatabase.getReference(products).toString() + "\n ${reference.ref.key} ")
            product.id = reference.ref.key.toString()
            reference.setValue(product).addOnSuccessListener { msg ->
                    Log.i("msg2", "Success ${msg.toString()}")
                }
                .addOnFailureListener { msg ->
                    Log.i("msg3", "Failure ${msg.message}")
                }
        }
    }

    fun update(product: Product) {
        firebaseDatabase.getReference(products+"/${product.id}").also {
            it.child("productPrice").setValue(product.productPrice)
            it.child("productQuantity").setValue(product.productQuantity)
            it.child("boughtQuantity").setValue(product.boughtQuantity)
            it.child("productName").setValue(product.productName)
            it.child("bought").setValue(product.bought)
            it.child("imageId").setValue(product.imageId)
        }
    }

    fun delete(productId: String) {
        firebaseDatabase.getReference(products+"/${productId}").removeValue()
    }
    /*  suspend fun insert(product: Product) = productDao.insertProduct(product)
      suspend fun update(product: Product) = productDao.updateProduct(product)
      suspend fun delete(product: Product) = productDao.deleteProduct(product)*/

}