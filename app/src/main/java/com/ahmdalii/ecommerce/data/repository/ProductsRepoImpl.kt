package com.ahmdalii.ecommerce.data.repository

import android.util.Log
import com.ahmdalii.ecommerce.data.local.ProductDao
import com.ahmdalii.ecommerce.data.model.Product
import com.ahmdalii.ecommerce.data.remote.APIService
import com.ahmdalii.ecommerce.domain.repository.IProductsRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProductsRepoImpl @Inject constructor(
    private val apiService: APIService,
    private val productDao: ProductDao,
) : IProductsRepo {
    override fun getLocalProducts(categoryName: String): Flow<List<Product>> =
        productDao.getLocalProducts(categoryName).map { item -> item.map { it } }

    override fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }

    override suspend fun getAllProductsFromRemote(categoryName: String): Flow<Result<List<Product>>> = flow {
        val response = apiService.getCategoryProducts(categoryName)

        if (response.isSuccessful) {
            val productsList = response.body() ?: emptyList()
            productsList.map { insertProduct(it) }
            emit(Result.success(productsList))
        } else {
            emit(Result.failure(Exception("Error response: ${response.message()}")))
        }
    }.catch { e ->
        Log.e("asd -> ", "Coroutine exception: ${e.message}")
        emit(Result.failure(Exception("Error fetching data")))
    }
}