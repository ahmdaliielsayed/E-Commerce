package com.ahmdalii.ecommerce.di

import com.ahmdalii.ecommerce.data.local.CategoryDao
import com.ahmdalii.ecommerce.data.remote.APIService
import com.ahmdalii.ecommerce.data.repository.CategoriesRepoImpl
import com.ahmdalii.ecommerce.domain.repository.ICategoriesRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideCategoriesRepo(
        apiService: APIService,
        categoryDao: CategoryDao
    ): ICategoriesRepo = CategoriesRepoImpl(apiService, categoryDao)
}