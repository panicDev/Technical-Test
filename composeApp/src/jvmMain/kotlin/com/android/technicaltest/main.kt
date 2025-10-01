package com.android.technicaltest

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.android.technicaltest.di.initKoinJvm

val koin = initKoinJvm()

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Product Sync - Offline/Online Desktop App",
        state = rememberWindowState(width = 1200.dp, height = 800.dp)
    ) {
        App()
    }
}