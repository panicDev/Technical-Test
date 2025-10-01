package com.android.technicaltest

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform