package com.android.technicaltest.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.technicaltest.ui.viewmodel.ProductEvent
import com.android.technicaltest.ui.viewmodel.ProductViewModel
import com.android.technicaltest.ui.viewmodel.SyncViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Routes between the product list and product form screens while keeping the associated view models alive.
 *
 * @param productViewModel View model that manages product data and events.
 * @param syncViewModel View model that manages synchronization status.
 * @param modifier Optional [Modifier] for applying layout behaviour.
 * @param isDarkTheme Indicates whether the dark theme is currently active.
 * @param onToggleTheme Invoked when the user toggles between light and dark themes.
 */
@Composable
fun MainScreen(
    productViewModel: ProductViewModel = koinViewModel(),
    syncViewModel: SyncViewModel = koinViewModel(),
    modifier: Modifier = Modifier,
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {
    val productUiState by productViewModel.uiState.collectAsStateWithLifecycle()
    val syncStatus by syncViewModel.syncStatus.collectAsStateWithLifecycle()

    var showFormScreen by remember { mutableStateOf(false) }

    LaunchedEffect(productUiState.selectedProduct) {
        showFormScreen = productUiState.selectedProduct != null
    }

    if (showFormScreen || productUiState.selectedProduct != null) {
        ProductFormScreen(
            product = productUiState.selectedProduct,
            onEvent = productViewModel::onEvent,
            onBackClick = {
                productViewModel.onEvent(ProductEvent.ProductSelected(null))
                showFormScreen = false
            },
            modifier = modifier
        )
    } else {
        ProductListScreen(
            uiState = productUiState,
            syncStatus = syncStatus,
            onEvent = productViewModel::onEvent,
            onSyncClick = { syncViewModel.sync() },
            onAddClick = { showFormScreen = true },
            modifier = modifier,
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme
        )
    }
}
