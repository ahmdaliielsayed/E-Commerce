package com.ahmdalii.ecommerce.ui.products.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmdalii.ecommerce.data.model.Product
import com.ahmdalii.ecommerce.domain.repository.IProductsRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val _repo: IProductsRepo
) : ViewModel() {

    private var _productsResponse = MutableLiveData<List<Product>>()
    val productsResponse: LiveData<List<Product>> = _productsResponse

    private var _progressLoading = MutableLiveData<Boolean>()
    val progressLoading: LiveData<Boolean> = _progressLoading

    private var _errorMessageResponse = MutableLiveData<String>()
    val errorMessageResponse: LiveData<String> = _errorMessageResponse

    private var _navigateToSelectedProductDetails = MutableLiveData<Product?>()
    val navigateToSelectedProductDetails: LiveData<Product?> = _navigateToSelectedProductDetails

    private val coroutineExceptionHandler = CoroutineExceptionHandler { _, throwable ->
        run {
            throwable.printStackTrace()
            _errorMessageResponse.value = throwable.message
        }
    }

    fun getProductsFromRemote(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _repo.getAllProductsFromRemote(categoryName)
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

    fun getLocalProducts(categoryName: String) {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            _repo.getLocalProducts(categoryName)
                .catch {
                    _errorMessageResponse.postValue(it.message)
                }
                .collect { result ->
                    _productsResponse.postValue(result)
                }
        }
    }

    fun onProductItemClicked(product: Product) {
        _navigateToSelectedProductDetails.value = product
    }

    fun onProductItemNavigated() {
        _navigateToSelectedProductDetails.value = null
    }
}