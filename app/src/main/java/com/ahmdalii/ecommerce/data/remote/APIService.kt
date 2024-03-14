package com.ahmdalii.ecommerce.data.remote

import com.ahmdalii.ecommerce.data.model.Product
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService {

    @GET("products/categories")
    suspend fun getAllCategories(): Response<List<String>>

    @GET("products/category/{categoryName}")
    suspend fun getCategoryProducts(
        @Path("categoryName") categoryName: String
    ): Response<List<Product>>
}