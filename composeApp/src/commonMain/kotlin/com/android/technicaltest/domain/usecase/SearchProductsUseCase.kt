package com.android.technicaltest.domain.usecase

import com.android.technicaltest.data.repository.ProductRepository
import com.android.technicaltest.domain.model.Product
import kotlinx.coroutines.flow.Flow

class SearchProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(query: String): Flow<List<Product>> {
        return if (query.isBlank()) {
            repository.getAllProducts()
        } else {
            repository.searchProducts(query)
        }
    }
}

