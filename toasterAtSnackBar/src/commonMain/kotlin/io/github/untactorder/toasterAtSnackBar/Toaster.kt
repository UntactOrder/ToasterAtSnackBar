package io.github.untactorder.toasterAtSnackBar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class InjectableSnackBar {
    lateinit var snackbarHostState: SnackbarHostState
    lateinit var lifecycleScope: CoroutineScope

    @Composable
    fun EmbeddedSnackBar(
        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
        lifecycleScope: CoroutineScope = rememberCoroutineScope(),
        snackBarModifier: Modifier = Modifier.wrapContentSize(),
        snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) }
    ) {
        this.snackbarHostState = snackbarHostState
        this.lifecycleScope = lifecycleScope

        Surface(modifier = snackBarModifier, color = Color.Transparent, contentColor = contentColorFor(Color.Transparent)) {
            SubcomposeLayout { constraints ->
                val layoutWidth = constraints.maxWidth
                val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
                val boilerBody: @Composable (PaddingValues) -> Unit = {}

                val snackbarPlaceables = subcompose(1) {
                    snackbarHost(snackbarHostState)
                }.map {
                    it.measure(looseConstraints)
                }

                val snackbarHeight = snackbarPlaceables.maxByOrNull { it.height }?.height ?: 0
                val bodyContentPlaceables = subcompose(0) {
                    boilerBody(PaddingValues(bottom = 0.dp))
                }.map { it.measure(looseConstraints.copy(maxHeight = snackbarHeight)) }

                layout(layoutWidth, snackbarHeight) {
                    bodyContentPlaceables.forEach {
                        it.place(0, 0)
                    }
                    snackbarPlaceables.forEach {
                        it.place(0, 0)
                    }
                }
            }
        }
    }


    @Composable
    fun FloatingSnackBar(
        modifier: Modifier = Modifier.fillMaxSize(),
        snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
        lifecycleScope: CoroutineScope = rememberCoroutineScope(),
        snackBarModifier: Modifier = Modifier.wrapContentSize(),
        snackBarAlignment: Alignment = Alignment.BottomCenter,
        snackbarHost: @Composable (SnackbarHostState) -> Unit = { SnackbarHost(it) },
        content: @Composable () -> Unit = {}
    ) {
        Box(modifier) {
            content()
            // @TODO this.sefelkjlkjkj
            EmbeddedSnackBar(snackbarHostState, lifecycleScope, snackBarModifier.align(snackBarAlignment), snackbarHost)
        }
    }

    suspend fun showSnackbarInCoroutine(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short,
        dismissed: () -> Unit = {}, performed: () -> Unit = {}
    ) {
        when (snackbarHostState.showSnackbar(message, actionLabel, true, duration)) {
            SnackbarResult.Dismissed -> dismissed()
            SnackbarResult.ActionPerformed -> performed()
        }
    }

    fun showSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        dismissed: () -> Unit = {}, performed: () -> Unit = {}
    ) {
        lifecycleScope.launch {
            showSnackbarInCoroutine(message, actionLabel, true, duration, dismissed, performed)
        }
    }

    /**
     * Calls to launch should happen inside a LaunchedEffect and not composition.
     */
    @Composable
    fun InComposableShowSnackbar(
        message: String,
        actionLabel: String? = null,
        duration: SnackbarDuration = SnackbarDuration.Short,
        dismissed: () -> Unit = {}, performed: () -> Unit = {}
    ) {
        LaunchedEffect(snackbarHostState) {
            showSnackbarInCoroutine(message, actionLabel, true, duration, dismissed, performed)
        }
    }
}

@Composable
fun Bartender(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    animated: @Composable () -> Unit = { FadeInFadeOutWithScale(
        current = hostState.currentSnackbarData,
        modifier = modifier,
        content = snackbar
    )},
    snackbar: @Composable (SnackbarData) -> Unit = { Snackbar(it) }
) {
    val currentSnackbarData = hostState.currentSnackbarData
    val accessibilityManager = LocalAccessibilityManager.current
    LaunchedEffect(currentSnackbarData) {
        if (currentSnackbarData != null) {
            val duration = currentSnackbarData.visuals.duration.toMillis(
                currentSnackbarData.visuals.actionLabel != null,
                accessibilityManager
            )
            delay(duration)
            currentSnackbarData.dismiss()
        }
    }
    animated()
}

/*
@Composable
fun DefaultSnackbar(
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { }
) {
    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                content = {
                    Text(
                        text = data.message,
                        style = MaterialTheme.typography.body2
                    )
                },
                action = {
                    data.actionLabel?.let { actionLabel ->
                        TextButton(onClick = onDismiss) {
                            Text(
                                text = actionLabel,
                                color= MaterialTheme.colors.primary,
                                style = MaterialTheme.typography.body2
                            )
                        }
                    }
                }
            )
        },
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Bottom)
    )
}
*/