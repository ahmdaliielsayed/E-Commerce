package com.ahmdalii.ecommerce.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ahmdalii.ecommerce.data.model.Category
import com.ahmdalii.ecommerce.data.model.Product
import com.ahmdalii.ecommerce.utils.MyConverters

@Database(entities = [Category::class, Product::class], version = 1)
@TypeConverters(MyConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun productDao(): ProductDao
}