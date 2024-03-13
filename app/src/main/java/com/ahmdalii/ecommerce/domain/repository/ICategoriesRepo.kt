package com.ahmdalii.ecommerce.domain.repository

import com.ahmdalii.ecommerce.data.model.Category
import kotlinx.coroutines.flow.Flow

interface ICategoriesRepo {

    fun getLocalCategories(): Flow<List<String>>
    fun insertCategory(category: Category)

    suspend fun getAllCategoriesFromRemote() : Flow<Result<List<String>>>
}