package com.android.technicaltest.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.android.technicaltest.domain.model.ConnectionStatus
import com.android.technicaltest.domain.model.SyncState
import com.android.technicaltest.domain.model.SyncStatus
import com.android.technicaltest.util.DateTimeUtil
import kotlin.time.ExperimentalTime

/**
 * Displays the current connection and sync status with contextual actions.
 *
 * @param syncStatus Aggregated sync information from the sync view model.
 * @param onSyncClick Invoked when the user taps the manual sync action.
 * @param modifier Optional [Modifier] for applying layout behaviour.
 */
@OptIn(ExperimentalTime::class)
@Composable
fun ConnectionStatusBar(
    syncStatus: SyncStatus,
    onSyncClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    data class StatusPresentation(
        val containerColor: Color,
        val contentColor: Color,
        val icon: ImageVector,
        val message: String
    )

    val status = when {
        syncStatus.connectionStatus is ConnectionStatus.Offline -> StatusPresentation(
            containerColor = colorScheme.errorContainer,
            contentColor = colorScheme.onErrorContainer,
            icon = Icons.Default.CloudOff,
            message = "Offline - changes will sync when online"
        )

        syncStatus.syncState is SyncState.Syncing -> StatusPresentation(
            containerColor = colorScheme.tertiaryContainer,
            contentColor = colorScheme.onTertiaryContainer,
            icon = Icons.Default.Sync,
            message = "Synchronising"
        )

        syncStatus.syncState is SyncState.Error -> StatusPresentation(
            containerColor = colorScheme.secondaryContainer,
            contentColor = colorScheme.onSecondaryContainer,
            icon = Icons.Default.Error,
            message = "Sync error - retrying soon"
        )

        syncStatus.connectionStatus is ConnectionStatus.Online -> StatusPresentation(
            containerColor = colorScheme.primaryContainer,
            contentColor = colorScheme.onPrimaryContainer,
            icon = Icons.Default.CloudDone,
            message = syncStatus.lastSyncTime?.let { lastSync ->
                "Last sync ${DateTimeUtil.formatRelativeTime(lastSync)}"
            } ?: "Connected and up to date"
        )

        else -> StatusPresentation(
            containerColor = colorScheme.surfaceColorAtElevation(2.dp),
            contentColor = colorScheme.onSurface,
            icon = Icons.Default.CloudOff,
            message = "Status unavailable"
        )
    }

    val containerColor = status.containerColor
    val contentColor = status.contentColor
    val icon = status.icon
    val message = status.message

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = containerColor,
        tonalElevation = 0.dp,
        shape = MaterialTheme.shapes.small
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = contentColor
                )
            }

            if (syncStatus.pendingOperationsCount > 0) {
                SuggestionChip(
                    onClick = {},
                    label = { Text("${syncStatus.pendingOperationsCount}") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = null,
                            tint = contentColor
                        )
                    },
                    colors = SuggestionChipDefaults.suggestionChipColors(
                        containerColor = containerColor.copy(alpha = 0.4f),
                        labelColor = contentColor
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))
            }

            if (syncStatus.connectionStatus is ConnectionStatus.Online &&
                syncStatus.syncState !is SyncState.Syncing) {
                FilledTonalIconButton(
                    onClick = onSyncClick,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sync,
                        contentDescription = "Sync now"
                    )
                }
            }
        }
    }
}
