package com.android.technicaltest.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.technicaltest.domain.model.Product
import com.android.technicaltest.domain.usecase.CreateProductUseCase
import com.android.technicaltest.domain.usecase.DeleteProductUseCase
import com.android.technicaltest.domain.usecase.GetProductsUseCase
import com.android.technicaltest.domain.usecase.SearchProductsUseCase
import com.android.technicaltest.domain.usecase.UpdateProductUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.android.technicaltest.util.Result

data class ProductUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedProduct: Product? = null
)

sealed class ProductEvent {
    data class SearchQueryChanged(val query: String) : ProductEvent()
    data class ProductSelected(val product: Product?) : ProductEvent()
    data class CreateProduct(val title: String, val description: String?) : ProductEvent()
    data class UpdateProduct(val id: Long, val title: String, val description: String?) : ProductEvent()
    data class DeleteProduct(val id: Long) : ProductEvent()
    data object ClearError : ProductEvent()
}

class ProductViewModel(
    private val getProductsUseCase: GetProductsUseCase,
    private val searchProductsUseCase: SearchProductsUseCase,
    private val createProductUseCase: CreateProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductUiState())
    val uiState: StateFlow<ProductUiState> = _uiState.asStateFlow()

    private val _snackbarMessage = MutableSharedFlow<String>()
    val snackbarMessage: SharedFlow<String> = _snackbarMessage.asSharedFlow()

    init {
        observeProducts()
    }

    private fun observeProducts() {
        viewModelScope.launch {
            _uiState.value.let { state ->
                if (state.searchQuery.isBlank()) {
                    getProductsUseCase()
                } else {
                    searchProductsUseCase(state.searchQuery)
                }.collect { products ->
                    _uiState.update { it.copy(products = products, isLoading = false) }
                }
            }
        }
    }

    fun onEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.SearchQueryChanged -> {
                _uiState.update { it.copy(searchQuery = event.query) }
                searchProducts(event.query)
            }

            is ProductEvent.ProductSelected -> {
                _uiState.update { it.copy(selectedProduct = event.product) }
            }

            is ProductEvent.CreateProduct -> {
                createProduct(event.title, event.description)
            }

            is ProductEvent.UpdateProduct -> {
                updateProduct(event.id, event.title, event.description)
            }

            is ProductEvent.DeleteProduct -> {
                deleteProduct(event.id)
            }

            is ProductEvent.ClearError -> {
                _uiState.update { it.copy(error = null) }
            }
        }
    }

    private fun searchProducts(query: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            searchProductsUseCase(query).collect { products ->
                _uiState.update { it.copy(products = products, isLoading = false) }
            }
        }
    }

    private fun createProduct(title: String, description: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = createProductUseCase(title, description)) {
                is Result.Success -> {
                    _snackbarMessage.emit("Product created successfully")
                    _uiState.update { it.copy(isLoading = false, error = null) }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                    _snackbarMessage.emit(result.message)
                }
                else -> {}
            }
        }
    }

    private fun updateProduct(id: Long, title: String, description: String?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = updateProductUseCase(id, title, description)) {
                is Result.Success -> {
                    _snackbarMessage.emit("Product updated successfully")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            selectedProduct = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                    _snackbarMessage.emit(result.message)
                }
                else -> {}
            }
        }
    }

    private fun deleteProduct(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = deleteProductUseCase(id)) {
                is Result.Success -> {
                    _snackbarMessage.emit("Product deleted successfully")
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            selectedProduct = null
                        )
                    }
                }
                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                    _snackbarMessage.emit(result.message)
                }
                else -> {}
            }
        }
    }
}