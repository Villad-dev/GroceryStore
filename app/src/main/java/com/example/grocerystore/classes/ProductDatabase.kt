package com.example.grocerystore.classes;

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

//@Database(entities = [Product::class], version = 1)
abstract class ProductDatabase : RoomDatabase() {

    abstract fun productDao(): ProductDao

    companion object{
        private var instance: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase{
            if(instance!= null){
                return instance as ProductDatabase
            }
            instance = Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                "Products"
            ).build()
            return instance as ProductDatabase
        }
    }
}