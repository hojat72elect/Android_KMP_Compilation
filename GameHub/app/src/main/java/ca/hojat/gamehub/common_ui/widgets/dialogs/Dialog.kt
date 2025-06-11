package ca.hojat.gamehub.common_ui.widgets.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import ca.hojat.gamehub.common_ui.theme.GameHubTheme

@Composable
fun GameNewsDialog(
    onDialogDismissed: () -> Unit,
    properties: DialogProperties = DialogProperties(),
    content: @Composable ColumnScope.() -> Unit,
) {
    Dialog(
        onDismissRequest = onDialogDismissed,
        properties = properties,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = GameHubTheme.shapes.medium,
        ) {
            Column(
                modifier = Modifier.padding(vertical = GameHubTheme.spaces.spacing_4_5),
                content = content,
            )
        }
    }
}
