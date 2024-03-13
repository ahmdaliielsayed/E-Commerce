package com.ahmdalii.ecommerce.data.remote

import retrofit2.Response
import retrofit2.http.GET

interface APIService {

    @GET("products/categories")
    suspend fun getAllCategories(): Response<List<String>>
}