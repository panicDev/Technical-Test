package com.android.technicaltest.domain.usecase

import com.android.technicaltest.data.repository.ProductRepository
import com.android.technicaltest.domain.model.Product
import kotlinx.coroutines.flow.Flow

class GetProductsUseCase(private val repository: ProductRepository) {
    operator fun invoke(): Flow<List<Product>> {
        return repository.getAllProducts()
    }
}

