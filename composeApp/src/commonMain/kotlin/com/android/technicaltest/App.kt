package com.android.technicaltest

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.android.technicaltest.ui.screen.MainScreen
import com.android.technicaltest.ui.theme.MinimalistAppTheme
import org.koin.compose.KoinContext

/**
 * Hosts the root navigation graph and applies the minimalist Material 3 styling to the app.
 *
 * @param navController The [NavHostController] shared across the composable tree.
 */
@Composable
@Preview
fun App(
    navController: NavHostController = rememberNavController()
) {
    val systemDarkTheme = isSystemInDarkTheme()
    var useDarkTheme by remember { mutableStateOf(systemDarkTheme) }

    MinimalistAppTheme(darkTheme = useDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            KoinContext {
                MainScreen(
                    modifier = Modifier.fillMaxSize(),
                    isDarkTheme = useDarkTheme,
                    onToggleTheme = { useDarkTheme = !useDarkTheme }
                )
            }
        }
    }
}
