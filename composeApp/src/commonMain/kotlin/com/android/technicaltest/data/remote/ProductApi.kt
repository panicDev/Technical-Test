package com.android.technicaltest.data.remote

import com.android.technicaltest.data.remote.dto.CreateProductRequest
import com.android.technicaltest.data.remote.dto.ProductDto
import com.android.technicaltest.data.remote.dto.ProductResponse
import com.android.technicaltest.data.remote.dto.UpdateProductRequest
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import com.android.technicaltest.util.Result
import io.ktor.client.request.delete
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ProductApi {
    private val client = ApiClient.httpClient

    suspend fun getAllProducts(userId: String, page: Int = 1, limit: Int = 100): Result<List<ProductDto>> {
        return try {
            val response: ProductResponse = client.get("/products/$userId") {
                parameter("page", page)
                parameter("limit", limit)
            }.body()

            Result.Success(response.data ?: emptyList())
        } catch (e: Exception) {
            Result.Error(
                message = e.message ?: "Failed to fetch products",
                exception = e
            )
        }
    }

    suspend fun getProductById(userId: String, productId: Long): Result<ProductDto> {
        return try {
            val response: ProductResponse = client.get("/products/$userId/$productId").body()

            val product = ProductDto(
                id = response.id,
                title = response.title ?: "",
                description = response.description,
                userId = response.userId ?: userId,
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
                user = response.user
            )

            Result.Success(product)
        } catch (e: Exception) {
            Result.Error(
                message = e.message ?: "Failed to fetch product",
                exception = e
            )
        }
    }

    suspend fun createProduct(userId: String, request: CreateProductRequest): Result<ProductDto> {
        return try {
            val response: ProductResponse = client.post("/products/$userId") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

            val product = ProductDto(
                id = response.id,
                title = response.title ?: request.title,
                description = response.description ?: request.description,
                userId = response.userId ?: userId,
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
                user = response.user
            )

            Result.Success(product)
        } catch (e: Exception) {
            Result.Error(
                message = e.message ?: "Failed to create product",
                exception = e
            )
        }
    }

    suspend fun updateProduct(userId: String, productId: Long, request: UpdateProductRequest): Result<ProductDto> {
        return try {
            val response: ProductResponse = client.put("/products/$userId/$productId") {
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

            val product = ProductDto(
                id = response.id ?: productId,
                title = response.title ?: request.title,
                description = response.description ?: request.description,
                userId = response.userId ?: userId,
                createdAt = response.createdAt,
                updatedAt = response.updatedAt,
                user = response.user
            )

            Result.Success(product)
        } catch (e: Exception) {
            Result.Error(
                message = e.message ?: "Failed to update product",
                exception = e
            )
        }
    }

    suspend fun deleteProduct(userId: String, productId: Long): Result<Unit> {
        return try {
            client.delete("/products/$userId/$productId")
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(
                message = e.message ?: "Failed to delete product",
                exception = e
            )
        }
    }
}