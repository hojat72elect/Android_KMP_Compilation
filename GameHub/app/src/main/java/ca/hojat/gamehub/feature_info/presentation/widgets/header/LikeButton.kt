package ca.hojat.gamehub.feature_info.presentation.widgets.header

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import ca.hojat.gamehub.R
import ca.hojat.gamehub.common_ui.clickable
import ca.hojat.gamehub.common_ui.theme.GameHubTheme
import com.airbnb.lottie.compose.LottieAnimatable
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.airbnb.lottie.compose.resetToBeginning

/**
 * param shouldPlay: if it's null, the animation shouldn't be played. If it's true,
 * animation should be played forward once; and if
 */
@Stable
class LikeButtonState {
    val animatable = LottieAnimatable()
    var isLiked by mutableStateOf(false)
    var shouldPlay: Boolean? = null
}

@Composable
fun LikeButton(
    modifier: Modifier = Modifier,
    @RawRes resId: Int,
    alignment: Alignment = Alignment.Center,
    enableMergePaths: Boolean = true,
    clipToCompositionBounds: Boolean = true,
    maintainOriginalImageBounds: Boolean = false,
    contentScale: ContentScale = ContentScale.Fit,
    state: LikeButtonState,
    onClick: () -> Unit
) {

    val composition by rememberLottieComposition(
        spec = LottieCompositionSpec.RawRes(resId = resId)
    )

    LaunchedEffect(
        composition,
        state.shouldPlay,
        state.isLiked
    ) {
        composition ?: return@LaunchedEffect
        if (state.isLiked) {
            state.animatable.snapTo(composition, 0.99f)
        } else {
            state.animatable.resetToBeginning()
        }
        when (state.shouldPlay) {
            true -> {
                state.animatable.animate(
                    composition = composition,
                    speed = 1.5f
                )
                state.shouldPlay = null
            }
            false -> {
                state.shouldPlay = null
            }
            else -> {
                // Don't do anything, the animation shouldn't be played.
            }
        }
    }


    LottieAnimation(
        composition = composition,
        progress = { state.animatable.progress },
        modifier = modifier.clickable(
            indication = null
        ) {
            state.isLiked = !state.isLiked
            state.shouldPlay = state.isLiked
            onClick()
        },
        enableMergePaths = remember {
            enableMergePaths
        },
        alignment = alignment,
        clipToCompositionBounds = clipToCompositionBounds,
        maintainOriginalImageBounds = maintainOriginalImageBounds,
        contentScale = contentScale,
    )
}

@Preview
@Composable
private fun LikeButtonPreview() {
    GameHubTheme {
        LikeButton(
            resId = R.raw.like_animation,
            state = LikeButtonState()
        ) {

        }
    }
}