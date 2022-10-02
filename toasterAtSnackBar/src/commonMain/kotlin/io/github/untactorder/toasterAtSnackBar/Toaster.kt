package io.github.untactorder.toasterAtSnackBar

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


open class InjectableSnackBar(
    snackbarHostState: SnackbarHostState? = null,
    lifecycleScope: CoroutineScope? = null,
) {
    private lateinit var snackbarHostState: SnackbarHostState
    private lateinit var lifecycleScope: CoroutineScope

    val hostState: SnackbarHostState
        @Composable
        get() {
            this.issueState()
            return snackbarHostState
        }

    init {
        if (snackbarHostState != null) {
            this.snackbarHostState = snackbarHostState
        }
        if (lifecycleScope != null) {
            this.lifecycleScope = lifecycleScope
        }
    }

    @Composable
    private fun issueState() {
        if (!this::snackbarHostState.isInitialized) {
            this.snackbarHostState = remember { SnackbarHostState() }
        }
        if (!this::lifecycleScope.isInitialized) {
            this.lifecycleScope = rememberCoroutineScope()
        }
    }

    /**
     * Embed a Snackbar in the UI.
     */
    @Composable
    fun EmbeddedSnackBar(
        snackBarModifier: Modifier = Modifier.wrapContentSize(),
        snackbarHost: @Composable (SnackbarHostState) -> Unit = { Bartender(it) }
    ) {
        issueState()  // state have to be issued before using

        Surface(modifier = snackBarModifier, color = Color.Transparent, contentColor = contentColorFor(Color.Transparent)) {
            SubcomposeLayout { constraints ->
                val looseConstraints = constraints.copy(minWidth = 0, minHeight = 0)
                val boilerBody: @Composable (PaddingValues) -> Unit = {
                    Surface(Modifier.padding(it)) {}
                }

                val snackbarPlaceables = subcompose(1) {
                    snackbarHost(snackbarHostState)
                }.map {
                    it.measure(looseConstraints)
                }

                val snackbarHeight = snackbarPlaceables.maxByOrNull { it.height }?.height ?: 0
                val snackbarWidth = snackbarPlaceables.maxByOrNull { it.width }?.width ?: 0
                val bodyContentPlaceables = subcompose(0) {
                    boilerBody(PaddingValues(bottom = 0.dp))
                }.map { it.measure(looseConstraints.copy(maxHeight = constraints.maxHeight)) }

                layout(snackbarWidth, snackbarHeight) {
                    snackbarPlaceables.forEach {
                        it.place(0, 0)
                    }
                    bodyContentPlaceables.forEach {
                        it.place(0, 0)
                    }
                }
            }
        }
    }

    /**
     * Show a floating Snackbar with a message.
     */
    @Composable
    fun FloatingSnackBar(
        boxModifier: Modifier = Modifier.fillMaxSize(),
        snackBarModifier: Modifier = Modifier.wrapContentSize(),
        snackBarAlignment: Alignment = Alignment.BottomStart,
        snackbarHost: @Composable (SnackbarHostState) -> Unit = { Bartender(it) },
        content: @Composable () -> Unit = {}
    ) {
        Box(boxModifier) {
            content()
            EmbeddedSnackBar(snackBarModifier.align(snackBarAlignment), snackbarHost)
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    suspend fun showSnackbarInCoroutine(
        message: String,
        title: String? = null,
        actionLabel: String? = null,
        actionOnNewLine: Boolean = false,
        withDismissAction: Boolean = true,
        duration: SnackbarDuration = SnackbarDuration.Short,
        customToastDesign: @Composable ((SnackbarData) -> Unit)? = null,
        dismissed: () -> Unit = {}, performed: () -> Unit = {}
    ) {
        when (snackbarHostState.showSnackbar(
            BillLetterVisual(message, title, actionLabel, actionOnNewLine,
                withDismissAction, duration, customToastDesign)
        )) {
            SnackbarResult.Dismissed -> dismissed()
            SnackbarResult.ActionPerformed -> performed()
        }
    }

    /**
     * You should not use this method in a Composable function.
     * Main thread may interrupt by this method. So, use InComposableShowSnackbar instead.
     */
    fun showSnackbar(
        message: String,
        title: String? = null,
        actionLabel: String? = null,
        actionOnNewLine: Boolean = false,
        withDismissAction: Boolean = true,
        duration: SnackbarDuration = SnackbarDuration.Short,
        customToastDesign: @Composable ((SnackbarData) -> Unit)? = null,
        dismissed: () -> Unit = {}, performed: () -> Unit = {}
    ) {
        lifecycleScope.launch {
            showSnackbarInCoroutine(message, title, actionLabel, actionOnNewLine,
                withDismissAction, duration, customToastDesign, dismissed, performed)
        }
    }

    /**
     * Calls to launch should happen inside a LaunchedEffect and not composition.
     */
    @Composable
    fun InComposableShowSnackbar(
        message: String,
        title: String? = null,
        actionLabel: String? = null,
        actionOnNewLine: Boolean = false,
        withDismissAction: Boolean = true,
        duration: SnackbarDuration = SnackbarDuration.Short,
        customToastDesign: @Composable ((SnackbarData) -> Unit)? = null,
        dismissed: () -> Unit = {}, performed: () -> Unit = {}
    ) {
        LaunchedEffect(null) {
            showSnackbarInCoroutine(message, title, actionLabel, actionOnNewLine,
                withDismissAction, duration, customToastDesign, dismissed, performed)
        }
    }

    /**
     * Launch AlertDialog
     */
    fun launchAlertDialog(
        message: String,
        title: String? = null,
        actionLabel: String? = null,
        actionOnNewLine: Boolean = false,
        withDismissAction: Boolean = true,
        duration: SnackbarDuration = SnackbarDuration.Indefinite,
        isDialOpened: MutableState<Boolean>,
        touchBlocking: Boolean = false,
        blockFilterColor: Color = Color.Transparent,
        maxFilterAlpha: Float = 0.3f,
        alignment: Alignment = Alignment.Center,
        dismissed: () -> Unit = {}, performed: () -> Unit = {},
        enableOutsideClick: Boolean = false,
        customToastDesign: @Composable (SnackbarData) -> Unit = {}
    ) {
        isDialOpened.value = true
        showSnackbar(message, title, actionLabel, actionOnNewLine, withDismissAction, duration,
            dismissed = { isDialOpened.value = false; dismissed() },
            performed = { isDialOpened.value = false; performed() },
            customToastDesign = {
                var outsideClick: (() -> Unit)? = null
                if (enableOutsideClick) {
                    outsideClick = { it.dismiss() }
                }
                OpenAlertDialog(isDialOpened, touchBlocking, blockFilterColor, maxFilterAlpha, alignment, outsideClick) {
                    customToastDesign(it)
                }
            })
    }
}


@Composable
fun Bartender(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    customAnimation: (@Composable (
        current: SnackbarData?,
        modifier: Modifier,
        content: @Composable ((SnackbarData) -> Unit)
    ) -> Unit)? = null
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
    val snackbarWidget = @Composable { data: SnackbarData ->
        val details = data.visuals
        if (details is BillLetterVisual) {
            val customToastDesign = details.customToastDesign
            if (customToastDesign is @Composable ((SnackbarData) -> Unit)) {
                customToastDesign(data)
            } else {
                SnackBarToastWithTitle(data)
            }
        } else {
            SnackBarToast(data)
        }
    }
    if (customAnimation == null) {
        FadeInFadeOutWithScale(hostState.currentSnackbarData, modifier, snackbarWidget)
    } else {
        customAnimation(hostState.currentSnackbarData, modifier, snackbarWidget)
    }
}


open class BillLetterVisual(
    override var message: String,
    var title: String?,
    override var actionLabel: String?,
    var actionOnNewLine: Boolean,
    override var withDismissAction: Boolean,
    override var duration: SnackbarDuration,
    var customToastDesign: @Composable ((SnackbarData) -> Unit)?
) : SnackbarVisuals {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BillLetterVisual

        if (message != other.message) return false
        if (actionLabel != other.actionLabel) return false
        if (withDismissAction != other.withDismissAction) return false
        if (duration != other.duration) return false
        if (title != other.title) return false
        if (customToastDesign != other::customToastDesign) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + actionLabel.hashCode()
        result = 31 * result + withDismissAction.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + customToastDesign.hashCode()
        return result
    }
}
