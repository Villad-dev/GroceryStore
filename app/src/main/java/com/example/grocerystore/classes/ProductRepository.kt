package com.example.grocerystore.classes

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow

class ProductRepository(private val firebaseDatabase: FirebaseDatabase) {

    private var productListReference: DatabaseReference
    private var childEventListener: ChildEventListener

    val allProducts = MutableStateFlow(HashMap<String, Product>())

    companion object {
        var key: String = "Shared"
    }

    init {
        productListReference = firebaseDatabase.getReference(key)
        childEventListener = returnChildListener()
        productListReference.addChildEventListener(childEventListener)
    }

    fun switchList(listName: String) {
        productListReference = firebaseDatabase.getReference(listName)
        productListReference.addChildEventListener(returnChildListener())

        firebaseDatabase.getReference(listName).get().addOnSuccessListener { snapshot ->
            val newProducts = snapshot.children.mapNotNull { childSnapshot ->
                val product = childSnapshot.getValue(Product::class.java)
                product?.id = childSnapshot.key.toString()
                product
            }.associateBy { it.id }

            allProducts.value = newProducts.toMutableMap() as HashMap<String, Product>

            println("Products: ${allProducts.value.size}  ${firebaseDatabase.getReference(listName).key} ${allProducts.value.size}")
        }.addOnFailureListener { error ->
            Log.e("error", error.message, error)
        }
    }

    private fun returnChildListener(): ChildEventListener {
        return object : ChildEventListener {
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
                    boughtQuantity = snapshot.child("boughtQuantity").value as Long,
                    imageId = snapshot.child("imageId").value as Long,
                    bought = snapshot.child("bought").value as Boolean
                )
                allProducts.value = allProducts.value.toMutableMap().apply {
                    put(product.id, product)
                } as HashMap<String, Product>

                println("Product updated: ${product.boughtQuantity}")
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
        }
    }

    suspend fun insert(product: Product) {
        firebaseDatabase.getReference(key).push().also { reference ->
            Log.i(
                "msg1",
                firebaseDatabase.getReference(key).toString() + "\n ${reference.ref.key} "
            )
            product.id = reference.ref.key.toString()
            reference.setValue(product).addOnSuccessListener { msg ->
                Log.i("msg2", "Success $msg")
            }.addOnFailureListener { msg ->
                Log.i("msg3", "Failure ${msg.message}")
            }
        }
    }

    suspend fun update(product: Product) {
        firebaseDatabase.getReference(key + "/${product.id}").child("productPrice")
            .setValue(product.productPrice)
        firebaseDatabase.getReference(key + "/${product.id}").child("productQuantity")
            .setValue(product.productQuantity)
        firebaseDatabase.getReference(key + "/${product.id}").child("boughtQuantity")
            .setValue(product.boughtQuantity)
        firebaseDatabase.getReference(key + "/${product.id}").child("productName")
            .setValue(product.productName)
        firebaseDatabase.getReference(key + "/${product.id}").child("imageId")
            .setValue(product.imageId)
        firebaseDatabase.getReference(key + "/${product.id}").child("bought")
            .setValue(product.bought)
        firebaseDatabase.getReference(key + "/${product.id}").child("id").setValue(product.id)
    }

    suspend fun delete(productId: String) {
        firebaseDatabase.getReference(key + "/${productId}").removeValue()
    }

}