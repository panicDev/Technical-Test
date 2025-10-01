package com.android.technicaltest.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val userId: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val user: UserDto? = null
)

@Serializable
data class UserDto(
    val userId: String,
    val createdAt: String
)

@Serializable
data class ProductResponse(
    val data: List<ProductDto>? = null,
    val pagination: PaginationDto? = null,
    val id: Long? = null,
    val title: String? = null,
    val description: String? = null,
    val userId: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val user: UserDto? = null
)

@Serializable
data class PaginationDto(
    val page: Int,
    val limit: Int,
    val total: Int,
    val pages: Int
)

@Serializable
data class CreateProductRequest(
    val title: String,
    val description: String? = null
)

@Serializable
data class UpdateProductRequest(
    val title: String,
    val description: String? = null
)

@Serializable
data class ErrorResponse(
    val error: String? = null,
    val errors: List<ValidationError>? = null
)

@Serializable
data class ValidationError(
    val msg: String,
    val param: String,
    val location: String
)