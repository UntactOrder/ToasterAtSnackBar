/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * This file contains sources from the Android Open Source Project (AOSP)
 * that are licensed under the Apache License, Version 2.0.
 *
 *
 * Modified by @IRACK000
 * @source androidx.compose.material3.SnackbarHost.SnackbarHost
 * @see <a href="URL">https://github.com/JetBrains/androidx/blob/b9c411c51a4d4ea28851c89639a2ef4ae28473cf/compose/material3/material3/src/commonMain/kotlin/androidx/compose/material3/SnackbarHost.kt</a>
 *
 * FadeInFadeOutWithScale:
 * This has not been modified in the original code, and the scope has been changed from private to public
 *
 * FadeInFadeOutState, FadeInFadeOutAnimationItem, FadeInFadeOutTransition, animatedOpacity, animatedScale,
 *  SnackbarInBetweenDelayMillis, SnackbarFadeOutMillis, SnackbarFadeInMillis:
 * It was only copied and pasted to access the private elements. There are no amendments.
 *
 * SnackbarDuration.toMillis:
 * This has not been modified in the original code, and the scope has been changed from private to public
 */


package io.github.untactorder.toasterAtSnackBar

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.AccessibilityManager
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.dismiss
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.delay


/**
 * Open AlertDialog by using toast or snackbar.
 * @param touchBlocking: If enabled, user touches will be blocked while the alert dialog is shown.
 * @param blockFilterColor: Only works if [touchBlocking] is enabled.
 * @param maxFilterAlpha: Only works if [touchBlocking] is enabled.
 * @param snackBarAlignment: Only works if [touchBlocking] is enabled.
 * @param outsideClick: Only works if [touchBlocking] is enabled.
 * [Warning] This should be used with FloatingSnackBar.
 */
@Composable
fun OpenAlertDialog(
    isDialOpened: MutableState<Boolean>,
    touchBlocking: Boolean = false,
    blockFilterColor: Color = Color.Transparent,
    maxFilterAlpha: Float = 0.2f,
    snackBarAlignment: Alignment = Alignment.Center,
    outsideClick: (() -> Unit)? = null,
    dialog: @Composable () -> Unit
) {
    if (touchBlocking) {
        var currentAlpha by rememberSaveable {
            mutableStateOf(0f)
        }
        LaunchedEffect(key1 = currentAlpha) {
            if (isDialOpened.value) {
                if (currentAlpha < maxFilterAlpha) {
                    currentAlpha += 0.008f
                }
            }
        }
        LaunchedEffect(key1 = isDialOpened.value) {
            if (!isDialOpened.value) {
                currentAlpha = 0.0f
            }
        }
        var modifier = Modifier.fillMaxSize().background(blockFilterColor.copy(currentAlpha))
        if (outsideClick is (() -> Unit)) {
            modifier = modifier.clickable(MutableInteractionSource(), null) { outsideClick() }
        }
        Box(modifier, snackBarAlignment) {
            dialog()
        }
    } else {
        dialog()
    }
}


@Composable
fun FadeInFadeOutWithScale(
    current: SnackbarData?,
    modifier: Modifier = Modifier,
    opacityEasing: Easing = LinearEasing,
    scaleEasing: Easing = FastOutSlowInEasing,
    onAnimationStarted: @Composable () -> Unit = {},
    onAnimationFinished: (Boolean) -> Unit = {},
    content: @Composable (SnackbarData) -> Unit
) {
    val state = remember { FadeInFadeOutState<SnackbarData?>() }
    if (current != state.current) {
        state.current = current
        val keys = state.items.map { it.key }.toMutableList()
        if (!keys.contains(current)) {
            keys.add(current)
        }
        state.items.clear()
        keys.filterNotNull().mapTo(state.items) { key ->
            FadeInFadeOutAnimationItem(key) { children ->
                val isVisible = key == current
                val duration = if (isVisible) SnackbarFadeInMillis else SnackbarFadeOutMillis
                val delay = SnackbarFadeOutMillis + SnackbarInBetweenDelayMillis
                val animationDelay = if (isVisible && keys.filterNotNull().size != 1) delay else 0
                val opacity = animatedOpacity(
                    animation = tween(
                        easing = opacityEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration
                    ),
                    visible = isVisible,
                    onAnimationStarted = onAnimationStarted,
                    onAnimationFinish = { visible ->
                        if (key != state.current) {
                            // leave only the current in the list
                            state.items.removeAll { it.key == key }
                            state.scope?.invalidate()
                        }
                        onAnimationFinished(visible)
                    }
                )
                val scale = animatedScale(
                    animation = tween(
                        easing = scaleEasing,
                        delayMillis = animationDelay,
                        durationMillis = duration
                    ),
                    visible = isVisible
                )
                Box(
                    Modifier
                        .graphicsLayer(
                            scaleX = scale.value,
                            scaleY = scale.value,
                            alpha = opacity.value
                        )
                        .semantics {
                            liveRegion = LiveRegionMode.Polite
                            dismiss { key.dismiss(); true }
                        }
                ) {
                    children()
                }
            }
        }
    }
    Box(modifier) {
        state.scope = currentRecomposeScope
        state.items.forEach { (item, opacity) ->
            key(item) {
                opacity {
                    content(item!!)
                }
            }
        }
    }
}

private class FadeInFadeOutState<T> {
    // we use Any here as something which will not be equals to the real initial value
    var current: Any? = Any()
    var items = mutableListOf<FadeInFadeOutAnimationItem<T>>()
    var scope: RecomposeScope? = null
}

private data class FadeInFadeOutAnimationItem<T>(
    val key: T,
    val transition: FadeInFadeOutTransition
)

private typealias FadeInFadeOutTransition = @Composable (content: @Composable () -> Unit) -> Unit

@Composable
private fun animatedOpacity(
    animation: AnimationSpec<Float>,
    visible: Boolean,
    onAnimationStarted: @Composable () -> Unit = {},
    onAnimationFinish: (Boolean) -> Unit = {}
): State<Float> {
    val alpha = remember { Animatable(if (!visible) 1f else 0f) }
    onAnimationStarted()
    LaunchedEffect(visible) {
        alpha.animateTo(
            if (visible) 1f else 0f,
            animationSpec = animation
        )
        onAnimationFinish(visible)
    }
    return alpha.asState()
}

@Composable
private fun animatedScale(animation: AnimationSpec<Float>, visible: Boolean): State<Float> {
    val scale = remember { Animatable(if (!visible) 1f else 0.8f) }
    LaunchedEffect(visible) {
        scale.animateTo(
            if (visible) 1f else 0.8f,
            animationSpec = animation
        )
    }
    return scale.asState()
}

private const val SnackbarFadeInMillis = 150
private const val SnackbarFadeOutMillis = 75
private const val SnackbarInBetweenDelayMillis = 0

fun SnackbarDuration.toMillis(
    hasAction: Boolean,
    accessibilityManager: AccessibilityManager?
): Long {
    val original = when (this) {
        SnackbarDuration.Indefinite -> Long.MAX_VALUE
        SnackbarDuration.Long -> 10000L
        SnackbarDuration.Short -> 4000L
    }
    if (accessibilityManager == null) {
        return original
    }
    return accessibilityManager.calculateRecommendedTimeoutMillis(
        original,
        containsIcons = true,
        containsText = true,
        containsControls = hasAction
    )
}
