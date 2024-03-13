package com.ahmdalii.ecommerce.data.repository

import android.util.Log
import com.ahmdalii.ecommerce.data.local.CategoryDao
import com.ahmdalii.ecommerce.data.model.Category
import com.ahmdalii.ecommerce.data.remote.APIService
import com.ahmdalii.ecommerce.domain.repository.ICategoriesRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoriesRepoImpl @Inject constructor(
    private val apiService: APIService,
    private val categoryDao: CategoryDao,
) : ICategoriesRepo {

    override fun getLocalCategories(): Flow<List<String>> =
        categoryDao.getLocalCategories().map { item -> item.map { it.name } }

    override suspend fun getAllCategoriesFromRemote(): Flow<Result<List<String>>> = flow {
        val response = apiService.getAllCategories()

        if (response.isSuccessful) {
            val categoriesList = response.body() ?: emptyList()
            categoriesList.map { insertCategory(Category(it)) }
            emit(Result.success(categoriesList))
        } else {
            emit(Result.failure(Exception("Error response: ${response.message()}")))
        }
    }.catch { e ->
        Log.e("asd -> ", "Coroutine exception: ${e.message}")
        emit(Result.failure(Exception("Error fetching data")))
    }

    override fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
}