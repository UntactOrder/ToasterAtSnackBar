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
 *
 * The two functions defaultActionComposable and defaultDismissActionComposable are based on the source code
 *  extracted from the androidx.compose.material3.Snackbar.Snackbar(sackbarData, modifier, actionOnNewLine, shape,
 *                                                                  containerColor, contentColor, actionColor,
 *                                                                  actionContentColor, dismissActionContentColor)
 *  function.
 */


package io.github.untactorder.toasterAtSnackBar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush.Companion.horizontalGradient
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


data class SnackBarColorData(
    val containerColor: Color,
    val contentColor: Color,
    val actionContentColor: Color,
    val dismissActionContentColor: Color
)


var defaultActionComposable: @Composable (SnackbarData, String, Color)
-> Unit = { snackbarData, actionLabel, actionColor ->
    TextButton(
        colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
        onClick = { snackbarData.performAction() },
        content = { Text(actionLabel) }
    )
}


var defaultDismissActionComposable: @Composable (SnackbarData, Modifier, String)
-> Unit = { data, modifier, contentDescription ->
    IconButton(
        modifier = modifier,
        onClick = { data.dismiss() },
        content = {
            Icon(
                Icons.Filled.Close,
                contentDescription = contentDescription
            )
        }
    )
}


/**
 * [NOTICE] If snackbarData.visuals.actionOnNewLine is true, contentBoxModifier will be ignored.
 */
@Composable
fun SnackBarToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    shape: Shape = RoundedCornerShape(10.dp),
    actionColor: Color = Color.White,
    snackBarColorData: SnackBarColorData? = null,
    actionComposable: @Composable (String) -> Unit = @Composable { text ->
        defaultActionComposable(snackbarData, text, actionColor)
    },
    dismissActionComposable: @Composable () -> Unit = @Composable {
        defaultDismissActionComposable(snackbarData, Modifier, "Dismiss Snackbar")
    },
    contentsBoxModifier: Modifier = Modifier.padding(bottom = 10.dp),
    contentsBoxAlignment: Alignment = Alignment.TopStart,
    contents: @Composable () -> Unit = { Text(snackbarData.visuals.message) }
) {
    val details = snackbarData.visuals
    var actionOnNewLine = false
    if (details is BillLetterVisual) {
        actionOnNewLine = details.actionOnNewLine
    }
    var content = contents
    if (!actionOnNewLine) {
        content = {
            Box(contentsBoxModifier, contentsBoxAlignment) {
                contents()
            }
        }
    }

    val actionLabel = details.actionLabel
    val action: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            actionComposable(actionLabel)
        }
    } else {
        null
    }
    val dismissAction = if (details.withDismissAction) dismissActionComposable else null
    if (snackBarColorData is SnackBarColorData) {
        Snackbar(
            modifier = modifier,
            action = action,
            dismissAction = dismissAction,
            actionOnNewLine = actionOnNewLine,
            shape = shape,
            containerColor = snackBarColorData.containerColor,
            contentColor = snackBarColorData.contentColor,
            actionContentColor = snackBarColorData.actionContentColor,
            dismissActionContentColor = snackBarColorData.dismissActionContentColor,
            content = content
        )
    } else {
        Snackbar(modifier, action, dismissAction, actionOnNewLine, shape, content = content)
    }
}


@Composable
fun SnackBarToastWithTitle(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    actionColor: Color = Color(248, 228, 224),
    snackBarColorData: SnackBarColorData = SnackBarColorData(
        containerColor = Color(105, 84, 86),
        contentColor = Color(239, 237, 237),
        actionContentColor = actionColor,
        dismissActionContentColor = actionColor
    ),  // Material 3
    actionComposable: @Composable (String) -> Unit = @Composable { text ->
        defaultActionComposable(snackbarData, text, actionColor)
    },
    dismissActionComposable: @Composable () -> Unit = @Composable {
        defaultDismissActionComposable(snackbarData, Modifier, "Dismiss Snackbar")
    },
    contentsBoxModifier: Modifier = Modifier.padding(bottom = 10.dp),
    contentsBoxAlignment: Alignment = Alignment.TopStart,
    toastContentContainer: @Composable (@Composable () -> Unit) -> Unit = {
        Column {
            it()
        }
    },
    title: @Composable (SnackbarVisuals) -> Unit = { details ->
        if (details is BillLetterVisual) {
            details.title?.let {
                Text(
                    text = it,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    },
    body: @Composable () -> Unit = {
        Text(snackbarData.visuals.message, color = Color(239, 237, 237))
    }
) {
    SnackBarToast(
        snackbarData, modifier, shape, actionColor, snackBarColorData,
        actionComposable, dismissActionComposable, contentsBoxModifier, contentsBoxAlignment)
    {
        val details = snackbarData.visuals
        toastContentContainer {
            title(details)
            body()
        }
    }
}


/**
 * A Toast with Android style.
 * [Notice] The variable snackbarData.visuals.actionLabel is not reflected in this function.
 * [Notice] The variable snackbarData.visuals.actionOnNewLine is fixed to false in this function.
 * [Warning] snackbarData.visuals should be BillLetterVisual or siblings of BillLetterVisual.
 */
@Composable
fun AndroidStyleToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    toastWidth: Dp? = 300.dp,
    contentsBoxModifier: Modifier = Modifier.padding(6.dp, 0.dp, 2.dp, 0.dp),
    contentsBoxAlignment: Alignment = Alignment.Center,
    dismissActionComposableModifier: Modifier = Modifier.padding(2.dp, 0.dp, 8.dp, 0.dp),
    shape: Shape = RoundedCornerShape(50.dp),
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = Color.White,
    containerColor: Color = FancyColorSet.info,
    dismissActionDescription: String = "Dismiss Snackbar",
    icon: @Composable (String) -> Unit = { contentDescription ->
        Row(modifier = Modifier.padding(end = 16.dp)) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = contentDescription
            )
        }
    },
    dismissActionComposable: @Composable () -> Unit = @Composable {
        IconButton(
            modifier = dismissActionComposableModifier,
            onClick = { snackbarData.dismiss() },
            colors = IconButtonDefaults.iconButtonColors(contentColor = textColor)
        ) {
            Icon(
                Icons.Filled.Close,
                contentDescription = dismissActionDescription
            )
        }
    },
    toastContentContainer: @Composable ((@Composable () -> Unit)) -> Unit = {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            icon(snackbarData.visuals.message)
            Column(
                modifier = Modifier.fillMaxWidth().weight(1.0f, true),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                it()
            }
        }
    },
    title: @Composable (SnackbarVisuals) -> Unit = { details ->
        if (details is BillLetterVisual) {
            details.title?.let {
                MarqueeText(
                    text = it,
                    color = textColor,
                    gradientEdgeColor = containerColor,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    fontFamily = fontFamily,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center
                )
            }
        }
    },
    body: @Composable () -> Unit = {
        if (snackbarData.visuals.message != "") {
            MarqueeText(snackbarData.visuals.message, color = textColor.copy(alpha = 0.7f),
                gradientEdgeColor = containerColor, fontSize = 12.sp, fontFamily = fontFamily,
                overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
        }
    }
) {
    val details = snackbarData.visuals
    if (details is BillLetterVisual) {
        details.actionLabel = null
        details.actionOnNewLine = false
    }
    val limitation = if (toastWidth is Dp) Modifier.width(toastWidth) else Modifier.fillMaxWidth()
    Box(modifier = limitation, contentAlignment = Alignment.Center) {
        SnackBarToastWithTitle(
            snackbarData = snackbarData,
            modifier = modifier,
            contentsBoxModifier = contentsBoxModifier,
            contentsBoxAlignment = contentsBoxAlignment,
            shape = shape,
            actionColor = containerColor,
            snackBarColorData = SnackBarColorData(
                containerColor = containerColor.copy(alpha = 0.98f),
                contentColor = textColor,
                actionContentColor = textColor,
                dismissActionContentColor = textColor
            ),
            actionComposable = @Composable {},
            dismissActionComposable = dismissActionComposable,
            toastContentContainer = toastContentContainer,
            title = title,
            body = body
        )
    }
}


/**
 * A Toast with iOS style.
 * @param containerColor: Only accept White and Black. And if containerColor is not Color.White, contentColor replaced with Color.Black.
 * [Notice] The variable snackbarData.visuals.actionLabel is not reflected in this function.
 * [Notice] The variable snackbarData.visuals.actionOnNewLine is fixed to false in this function.
 * [Warning] snackbarData.visuals should be BillLetterVisual or siblings of BillLetterVisual.
 */
@Composable
fun IosStyleToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    toastWidth: Dp? = 300.dp,
    contentsBoxModifier: Modifier = Modifier.padding(6.dp, 0.dp, 2.dp, 0.dp),
    contentsBoxAlignment: Alignment = Alignment.Center,
    dismissActionComposableModifier: Modifier = Modifier.padding(2.dp, 0.dp, 8.dp, 0.dp),
    shape: Shape = RoundedCornerShape(50.dp),
    fontFamily: FontFamily = FontFamily.Default,
    icon: @Composable (String) -> Unit = { contentDescription ->
        Row(modifier = Modifier.padding(end = 16.dp)) {
            Icon(
                Icons.Filled.Warning,
                contentDescription = contentDescription
            )
        }
    },
    containerColor: Color = Color.White,
    dismissActionDescription: String = "Dismiss Snackbar"
) {
    val backgroundColor = if (containerColor == Color.White) Color.White else Color.Black
    val contentColor = if (containerColor != Color.White) Color.White else Color.Black
    AndroidStyleToast(
        snackbarData = snackbarData,
        modifier = modifier,
        toastWidth = toastWidth,
        contentsBoxModifier = contentsBoxModifier,
        contentsBoxAlignment = contentsBoxAlignment,
        dismissActionComposableModifier = dismissActionComposableModifier,
        shape = shape,
        textColor = contentColor,
        containerColor = backgroundColor,
        dismissActionDescription = dismissActionDescription,
        icon = icon,
        title = { it ->
            if (it is BillLetterVisual) {
                it.title?.let {
                    MarqueeText(
                        text = it,
                        color = contentColor,
                        gradientEdgeColor = backgroundColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        body = {
            if (snackbarData.visuals.message != "") {
                MarqueeText(snackbarData.visuals.message, color = Color(0xFFA2A2A2), // Grey Color
                    gradientEdgeColor = backgroundColor, fontSize = 12.sp, fontFamily = fontFamily,
                    overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Center)
            }
        }
    )
}


/**
 * A Simple Toast with iOS style.
 * [Notice] The variable snackbarData.visuals.message is only reflected in this function. Others will be ignored.
 */
@Composable
fun IosSimpleToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    toastWidth: Dp? = 300.dp,
    contentsBoxModifier: Modifier = Modifier.padding(0.dp, 0.dp, 0.dp, 10.dp),
    contentsBoxAlignment: Alignment = Alignment.CenterStart,
    shape: Shape = RoundedCornerShape(8.dp),
    fontFamily: FontFamily = FontFamily.Default,
    darkBackground: Boolean = false,
    contents: @Composable (Color) -> Unit = { color ->
        Text(snackbarData.visuals.message, color = color,
            fontSize = 15.sp, fontFamily = fontFamily, textAlign = TextAlign.Center)
    }
) {
    val details = snackbarData.visuals
    if (details is BillLetterVisual) {
        details.actionLabel = null
        details.actionOnNewLine = false
        details.withDismissAction = false
    }
    var backgroundColor = Color(0xF6dddfe3)  // Light Grey Color
    var contentColor = Color(0xFF525358)  // Dark Grey Color
    if (darkBackground) {
        contentColor = backgroundColor
        backgroundColor = Color(0xF635373f)  // Dark Blue-Grey Color
    }
    val limitation = if (toastWidth is Dp) Modifier.width(toastWidth) else Modifier.fillMaxWidth()
    Box(modifier = limitation, contentAlignment = Alignment.Center) {
        SnackBarToastWithTitle(
            snackbarData = snackbarData,
            modifier = modifier,
            contentsBoxModifier = contentsBoxModifier,
            contentsBoxAlignment = contentsBoxAlignment,
            shape = shape,
            actionColor = contentColor,
            snackBarColorData = SnackBarColorData(
                containerColor = backgroundColor,
                contentColor = contentColor,
                actionContentColor = contentColor,
                dismissActionContentColor = contentColor
            ),
            toastContentContainer = {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    it()
                }
            },
            title = {},
            body = { contents(contentColor) }
        )
    }
}

@Composable
fun PastelToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    contentsBoxModifier: Modifier = Modifier.padding(2.dp, 0.dp, 2.dp, 0.dp),
    contentsBoxAlignment: Alignment = Alignment.Center,
    actionComposableModifier: Modifier = Modifier.padding(8.dp, 0.dp, 8.dp, 0.dp),
    dismissActionComposableModifier: Modifier = Modifier.padding(2.dp, 0.dp, 10.dp, 0.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke = BorderStroke(1.dp, horizontalGradient(
        listOf(Color.White.copy(alpha = 0.5f), Color.White.copy(alpha = 0.3f), Color.White.copy(alpha = 0.4f)))),
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = Color.White,
    containerColor: Color = PastelColorSet.info,
    dismissActionDescription: String = "Dismiss Snackbar"
) {
    SnackBarToastWithTitle(
        snackbarData = snackbarData,
        modifier = modifier.border(border, shape),
        contentsBoxModifier = contentsBoxModifier,
        contentsBoxAlignment = contentsBoxAlignment,
        shape = shape,
        actionColor = containerColor,
        snackBarColorData = SnackBarColorData(
            containerColor = containerColor.copy(alpha = 0.98f),
            contentColor = containerColor,
            actionContentColor = textColor,
            dismissActionContentColor = textColor
        ),
        actionComposable = @Composable { text ->
            TextButton(
                modifier = actionComposableModifier,
                colors = ButtonDefaults.textButtonColors(Color.White.copy(alpha = 0.1f), contentColor = textColor),
                shape = shape,
                onClick = { snackbarData.performAction() },
                border = border,
                content = { Text(text) }
            )
        },
        dismissActionComposable = @Composable {
            IconButton(
                modifier = dismissActionComposableModifier,
                onClick = { snackbarData.dismiss() },
                colors = IconButtonDefaults.iconButtonColors(contentColor = textColor)
            ) {
                Surface(shape = CircleShape, border = border, color = Color.White.copy(alpha = 0.1f)) {
                    Icon(
                        Icons.Filled.Close,
                        contentDescription = dismissActionDescription,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        },
        title = { it ->
            if (it is BillLetterVisual) {
                it.title?.let {
                    MarqueeText(
                        text = it,
                        color = textColor,
                        gradientEdgeColor = containerColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                }
            }
        },
        body = {
            if (snackbarData.visuals.message != "") {
                MarqueeText(snackbarData.visuals.message, color = textColor.copy(alpha = 0.7f),
                    gradientEdgeColor = containerColor, fontSize = 12.sp, fontFamily = fontFamily,
                    overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Start)
            }
        }
    )
}

@Composable
fun PastelAlertDialog(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(30.dp),
    contentsBoxModifier: Modifier = Modifier.padding(0.dp, 0.dp, 6.dp, 0.dp),
    contentsBoxAlignment: Alignment = Alignment.Center,
    actionComposableModifier: Modifier = Modifier.padding(0.dp, 0.dp, 10.dp, 8.dp),
    dismissActionComposableModifier: Modifier = Modifier.padding(2.dp, 0.dp, 14.dp, 8.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    border: BorderStroke = BorderStroke(1.dp, horizontalGradient(
        listOf(Color.White.copy(alpha = 0.5f), Color.White.copy(alpha = 0.3f), Color.White.copy(alpha = 0.4f)))),
    fontFamily: FontFamily = FontFamily.Default,
    textColor: Color = Color.White,
    containerColor: Color = PastelColorSet.info,
    dismissActionDescription: String = "Dismiss"
) {
    SnackBarToastWithTitle(
        snackbarData = snackbarData,
        modifier = modifier.border(border, shape),
        contentsBoxModifier = Modifier,
        contentsBoxAlignment = contentsBoxAlignment,
        shape = shape,
        actionColor = containerColor,
        snackBarColorData = SnackBarColorData(
            containerColor = containerColor.copy(alpha = 0.98f),
            contentColor = containerColor,
            actionContentColor = textColor,
            dismissActionContentColor = textColor
        ),
        actionComposable = @Composable { text ->
            TextButton(
                modifier = actionComposableModifier,
                colors = ButtonDefaults.textButtonColors(Color.White.copy(alpha = 0.1f), contentColor = textColor),
                shape = shape,
                onClick = { snackbarData.performAction() },
                border = border,
                content = { Text(text) }
            )
        },
        dismissActionComposable = @Composable {
            TextButton(
                modifier = dismissActionComposableModifier,
                onClick = { snackbarData.dismiss() },
                colors = ButtonDefaults.textButtonColors(Color.White.copy(alpha = 0.1f), contentColor = textColor),
                shape = shape,
                border = border,
                content = { Text(dismissActionDescription) }
            )
        },
        title = { it ->
            if (it is BillLetterVisual) {
                it.title?.let {
                    Text(
                        it,
                        modifier = contentsBoxModifier,
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Start
                    )
                }
            }
        },
        body = {
            if (snackbarData.visuals.message != "") {
                Text(snackbarData.visuals.message, color = textColor.copy(alpha = 0.7f),
                    fontSize = 14.sp, fontFamily = fontFamily, modifier = contentsBoxModifier,
                    overflow = TextOverflow.Ellipsis, textAlign = TextAlign.Start)
            }
        }
    )
}


fun InjectableSnackBar.launchAlertDialogWithPastelToast(
    message: String,
    title: String? = null,
    actionLabel: String? = null,
    actionOnNewLine: Boolean = false,
    withDismissAction: Boolean = true,
    duration: SnackbarDuration = SnackbarDuration.Indefinite,
    isDialOpened: MutableState<Boolean>,
    touchBlocking: Boolean = false,
    blockFilterColor: Color = Color.Transparent,
    maxFilterAlpha: Float = 0.2f,
    snackBarAlignment: Alignment = Alignment.Center,
    dismissed: () -> Unit = {}, performed: () -> Unit = {},
    enableOutsideClick: Boolean = false,
    customToastDesign: @Composable (SnackbarData) -> Unit = { data ->
        PastelAlertDialog(data, modifier = Modifier.padding(30.dp),
            containerColor = PastelColorSet.toList().random())
    }
) {
    launchAlertDialog(message, title, actionLabel, actionOnNewLine, withDismissAction, duration, isDialOpened, touchBlocking,
        blockFilterColor, maxFilterAlpha, snackBarAlignment, dismissed, performed, enableOutsideClick, customToastDesign)
}
