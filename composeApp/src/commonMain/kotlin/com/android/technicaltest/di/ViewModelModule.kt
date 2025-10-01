package com.android.technicaltest.di

import com.android.technicaltest.ui.viewmodel.ProductViewModel
import com.android.technicaltest.ui.viewmodel.SyncViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun viewModelModule()  = module {

    viewModelOf(::ProductViewModel)
    viewModelOf(::SyncViewModel)
}