package com.anthonyla.paperize.feature.wallpaper.presentation.settings_screen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.anthonyla.paperize.R

/**
 * Dynamic theming switch
 */
@Composable
fun DynamicThemingListItem(dynamicTheming: Boolean, onDynamicThemingClick: (Boolean) -> Unit) {
    val context = LocalContext.current
    Row {
        ListItem(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .clickable { onDynamicThemingClick(!dynamicTheming) },
            headlineContent = {
                Text(
                    text = stringResource(R.string.dynamic_theme),
                    style = MaterialTheme.typography.titleMedium

                ) },
            supportingContent = {
                Text(
                    text = stringResource(R.string.material_you),
                    style = MaterialTheme.typography.bodySmall
                ) },
            trailingContent = {
                Switch(
                    modifier = Modifier.semantics { contentDescription = context.getString(R.string.dynamic_theme) },
                    checked = dynamicTheming,
                    onCheckedChange = onDynamicThemingClick,
                    enabled = true
                ) },
            leadingContent = {
                Icon(
                    Icons.Outlined.Palette,
                    contentDescription = stringResource(R.string.dynamic_theming),
                    tint = MaterialTheme.colorScheme.primary
                ) },
            tonalElevation = 5.dp
        )
    }
}