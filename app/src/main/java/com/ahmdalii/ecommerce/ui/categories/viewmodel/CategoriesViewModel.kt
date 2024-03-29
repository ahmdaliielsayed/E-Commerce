package com.ahmdalii.ecommerce.ui.categories.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmdalii.ecommerce.domain.repository.ICategoriesRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val _repo: ICategoriesRepo
) : ViewModel() {

    private var _categoriesResponse = MutableLiveData<List<String>>()
    val categoriesResponse: LiveData<List<String>> = _categoriesResponse

    private var _progressLoading = MutableLiveData<Boolean>()
    val progressLoading: LiveData<Boolean> = _progressLoading

    private var _errorMessageResponse = MutableLiveData<String>()
    val errorMessageResponse: LiveData<String> = _errorMessageResponse

    private var _navigateToSelectedCategory = MutableLiveData<String?>()
    val navigateToSelectedCategory: LiveData<String?> = _navigateToSelectedCategory

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        run {
            throwable.printStackTrace()
            _errorMessageResponse.value = throwable.message
        }
    }

    init {
        getLocalCategories()
    }

    fun getCategoriesFromRemote() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _repo.getAllCategoriesFromRemote()
                .onStart {
                    _progressLoading.postValue(true)
                }
                .catch {
                    _errorMessageResponse.postValue(it.message)
                }
                .collect { result ->
                    if (result.isFailure) {
                        _errorMessageResponse.postValue(result.exceptionOrNull()?.message)
                    }
                    _progressLoading.postValue(false)
                }
        }
    }

    private fun getLocalCategories() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _repo.getLocalCategories()
                .catch {
                    _errorMessageResponse.postValue(it.message)
                }
                .collect { result ->
                    _categoriesResponse.postValue(result)
                }
        }
    }

    fun onCategoryItemClicked(categoryName: String) {
        _navigateToSelectedCategory.value = categoryName
    }

    fun onCategoryItemNavigated() {
        _navigateToSelectedCategory.value = null
    }
}