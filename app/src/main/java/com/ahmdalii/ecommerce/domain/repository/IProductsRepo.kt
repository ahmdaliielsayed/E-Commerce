package com.ahmdalii.ecommerce.domain.repository

import com.ahmdalii.ecommerce.data.model.Product
import kotlinx.coroutines.flow.Flow

interface IProductsRepo {

    fun getLocalProducts(categoryName: String): Flow<List<Product>>
    fun insertProduct(product: Product)

    suspend fun getAllProductsFromRemote(categoryName: String): Flow<Result<List<Product>>>
}