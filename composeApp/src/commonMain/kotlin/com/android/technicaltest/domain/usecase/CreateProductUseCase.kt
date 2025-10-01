package com.android.technicaltest.domain.usecase

import com.android.technicaltest.data.repository.ProductRepository
import com.android.technicaltest.domain.model.Product
import com.android.technicaltest.util.Result

class CreateProductUseCase(private val repository: ProductRepository) {
    suspend operator fun invoke(title: String, description: String?): Result<Product> {
        if (title.isBlank()) {
            return Result.Error("Title cannot be empty")
        }

        if (title.length > 255) {
            return Result.Error("Title must be less than 255 characters")
        }

        if (description != null && description.length > 1000) {
            return Result.Error("Description must be less than 1000 characters")
        }

        return repository.createProduct(title.trim(), description?.trim())
    }
}