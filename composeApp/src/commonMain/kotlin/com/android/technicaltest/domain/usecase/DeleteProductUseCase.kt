package com.android.technicaltest.domain.usecase

import com.android.technicaltest.data.repository.ProductRepository
import com.android.technicaltest.util.Result

class DeleteProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(id: Long): Result<Unit> {
        return repository.deleteProduct(id)
    }
}