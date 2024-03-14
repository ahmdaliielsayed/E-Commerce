package com.ahmdalii.ecommerce.utils

import androidx.room.TypeConverter
import com.ahmdalii.ecommerce.data.model.Rating
import com.google.gson.Gson

class MyConverters {
    var gson = Gson()

    @TypeConverter
    fun ratingToString(rating : Rating) : String{
        return gson.toJson(rating)
    }

    @TypeConverter
    fun stringToRating(ratingString : String) : Rating{
        return gson.fromJson(ratingString, Rating::class.java)
    }
}