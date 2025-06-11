package ca.hojat.gamehub.common_ui

import androidx.compose.foundation.Indication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

@Composable
fun OnLifecycleEvent(
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    onCreate: (() -> Unit)? = null,
    onStart: (() -> Unit)? = null,
    onResume: (() -> Unit)? = null,
    onPause: (() -> Unit)? = null,
    onStop: (() -> Unit)? = null,
    onDestroy: (() -> Unit)? = null,
    onAny: (() -> Unit)? = null,
) {
    val currentOnCreate by rememberUpdatedState(onCreate)
    val currentOnStart by rememberUpdatedState(onStart)
    val currentOnResume by rememberUpdatedState(onResume)
    val currentOnPause by rememberUpdatedState(onPause)
    val currentOnStop by rememberUpdatedState(onStop)
    val currentOnDestroy by rememberUpdatedState(onDestroy)
    val currentOnAny by rememberUpdatedState(onAny)

    DisposableEffect(lifecycleOwner) {
        val lifecycle = lifecycleOwner.lifecycle
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> currentOnCreate?.invoke()
                Lifecycle.Event.ON_START -> currentOnStart?.invoke()
                Lifecycle.Event.ON_RESUME -> currentOnResume?.invoke()
                Lifecycle.Event.ON_PAUSE -> currentOnPause?.invoke()
                Lifecycle.Event.ON_STOP -> currentOnStop?.invoke()
                Lifecycle.Event.ON_DESTROY -> currentOnDestroy?.invoke()
                Lifecycle.Event.ON_ANY -> currentOnAny?.invoke()
            }
        }

        lifecycle.addObserver(lifecycleObserver)
        onDispose { lifecycle.removeObserver(lifecycleObserver) }
    }
}

fun Modifier.clickable(
    indication: Indication?,
    onClick: () -> Unit,
) = composed {
    clickable(
        interactionSource = remember { MutableInteractionSource() },
        indication = indication,
        onClick = onClick,
    )
}
