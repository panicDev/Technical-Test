package com.android.technicaltest.di

import com.android.technicaltest.domain.usecase.CreateProductUseCase
import com.android.technicaltest.domain.usecase.DeleteProductUseCase
import com.android.technicaltest.domain.usecase.GetProductsUseCase
import com.android.technicaltest.domain.usecase.SearchProductsUseCase
import com.android.technicaltest.domain.usecase.SyncUseCase
import com.android.technicaltest.domain.usecase.UpdateProductUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

fun useCaseModule() = module {
    singleOf(::CreateProductUseCase)
    singleOf(::UpdateProductUseCase)
    singleOf(::DeleteProductUseCase)
    singleOf(::GetProductsUseCase)
    singleOf(::SearchProductsUseCase)
    singleOf(::SyncUseCase)
}