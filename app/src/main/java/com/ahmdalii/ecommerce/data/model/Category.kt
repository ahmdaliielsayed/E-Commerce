package com.ahmdalii.ecommerce.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category")
data class Category(
    @PrimaryKey
    val name: String
)