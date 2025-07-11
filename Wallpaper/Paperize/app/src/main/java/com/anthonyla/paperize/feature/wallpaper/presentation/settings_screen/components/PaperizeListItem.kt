package com.anthonyla.paperize.feature.wallpaper.presentation.settings_screen.components

import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.anthonyla.paperize.R


/**
 * List item for app version and link to GitHub and PlayStore
 */
@Composable
fun PaperizeListItem(onGitHubClick: () -> Unit, onIzzyOnDroidClick: () -> Unit, onFdroidClick: () -> Unit) {
    val context = LocalContext.current
    var version = ""
    try {
        val pInfo: PackageInfo =
            context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName.toString()
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(10.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(2f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.app_icon_foreground),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .size(160.dp)
                        .scale(1.5f),
                    tint = MaterialTheme.colorScheme.primary,
                )
                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize
                )
                Text(
                    text = stringResource(R.string.version, version),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize
                )
                Text(
                    text = "Anthony La",
                    style = MaterialTheme.typography.labelSmall,
                )
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconButton(
                    onClick = onGitHubClick,
                    modifier = Modifier
                        .size(60.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.github),
                        contentDescription = "GitHub",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                IconButton(
                    onClick = onFdroidClick,
                    modifier = Modifier
                        .size(60.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.fdroid),
                        contentDescription = "F-Droid",
                        modifier = Modifier.size(20.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                IconButton(
                    onClick = onIzzyOnDroidClick,
                    modifier = Modifier
                        .size(60.dp)
                        .background(MaterialTheme.colorScheme.primary, CircleShape)
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.izzyondroid),
                        contentDescription = "Izzy-On-Droid-Droid",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}