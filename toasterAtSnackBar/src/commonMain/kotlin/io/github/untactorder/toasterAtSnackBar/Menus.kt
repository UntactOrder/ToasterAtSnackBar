package io.github.untactorder.toasterAtSnackBar

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
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

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import io.github.untactorder.shared.MR


data class SnackBarColorData(
    val containerColor: Color,
    val contentColor: Color,
    val actionContentColor: Color,
    val dismissActionContentColor: Color
)


@Composable
fun SnackBarToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    shape: Shape = RoundedCornerShape(10.dp),
    innerMarginStart: Dp = 0.dp, innerMarginEnd: Dp = 0.dp,
    bottomShift: Dp = 10.dp,
    actionColor: Color = Color.White,
    snackBarColorData: SnackBarColorData? = null,
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
            Column(Modifier.padding(start = innerMarginStart, bottom = bottomShift), content = { contents() })
        }
    }

    val actionLabel = details.actionLabel
    val actionComposable: (@Composable () -> Unit)? = if (actionLabel != null) {
        @Composable {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
                onClick = { snackbarData.performAction() },
                content = { Text(actionLabel) }
            )
        }
    } else {
        null
    }
    val dismissActionComposable: (@Composable () -> Unit)? =
        if (details.withDismissAction) {
            @Composable {
                IconButton(
                    onClick = { snackbarData.dismiss() },
                    content = {
                        Icon(
                            Icons.Filled.Close,
                            modifier = Modifier.padding(end = innerMarginEnd),
                            contentDescription = StringDesc.Resource(MR.strings.dismiss_snackbar).toString()
                        )
                    }
                )
            }
        } else {
            null
        }
    if (snackBarColorData is SnackBarColorData) {
        Snackbar(
            modifier = modifier,
            action = actionComposable,
            dismissAction = dismissActionComposable,
            actionOnNewLine = actionOnNewLine,
            shape = shape,
            containerColor = snackBarColorData.containerColor,
            contentColor = snackBarColorData.contentColor,
            actionContentColor = snackBarColorData.actionContentColor,
            dismissActionContentColor = snackBarColorData.dismissActionContentColor,
            content = content
        )
    } else {
        Snackbar(modifier, actionComposable, dismissActionComposable, actionOnNewLine, shape, content = content)
    }
}


@Composable
fun SnackBarToastWithTitle(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    innerMarginStart: Dp = 0.dp, innerMarginEnd: Dp = 0.dp,
    bottomShift: Dp = 10.dp,
    actionColor: Color = Color(248, 228, 224),
    snackBarColorData: SnackBarColorData = SnackBarColorData(
        containerColor = Color(105, 84, 86),
        contentColor = Color(239, 237, 237),
        actionContentColor = actionColor,
        dismissActionContentColor = actionColor
    ),  // Material 3
    contentContainer: @Composable (@Composable () -> Unit) -> Unit = {
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
        snackbarData, modifier, shape,
        innerMarginStart,innerMarginEnd, bottomShift,
        actionColor, snackBarColorData)
    {
        val details = snackbarData.visuals
        contentContainer {
            title(details)
            body()
        }
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
fun IosStypeToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    shape: Shape = RoundedCornerShape(50.dp),
    fontFamily: FontFamily = FontFamily.Default,
    icon: @Composable (String) -> Unit = { contentDescription ->
        Row {
            Icon(
                Icons.Filled.Warning,
                contentDescription = contentDescription
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    },
    containerColor: Color = Color.White
) {
    val details = snackbarData.visuals
    if (details is BillLetterVisual) {
        details.actionLabel = null
        details.actionOnNewLine = false
    }
    val backgroundColor = if (containerColor == Color.White) Color.White else Color.Black
    val contentColor = if (containerColor != Color.White) Color.White else Color.Black
    SnackBarToastWithTitle(
        snackbarData = snackbarData,
        modifier = modifier,
        shape = shape,
        innerMarginStart = 14.dp,
        innerMarginEnd = 16.dp,
        actionColor = contentColor,
        snackBarColorData = SnackBarColorData(
            containerColor = backgroundColor,
            contentColor = contentColor,
            actionContentColor = contentColor,
            dismissActionContentColor = contentColor
        ),
        contentContainer = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                icon(details.message)
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    it()
                }
                if (!details.withDismissAction) {
                    Spacer(modifier = Modifier.padding(end = 8.dp))
                }
            }
        },
        title = { it ->
            if (it is BillLetterVisual) {
                it.title?.let {
                    Text(
                        text = it,
                        color = contentColor,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = fontFamily
                    )
                }
            }
        },
        body = {
            if (snackbarData.visuals.message != "") {
                Text(snackbarData.visuals.message, color = Color(0xFFA2A2A2), // Grey Color
                    fontSize = 12.sp, fontFamily = fontFamily)
            }
        }
    )
}


/**
 * A Simple Toast with iOS style.
 * @param containerColor: Only accept White and Black. And if containerColor is not Color.White, contentColor replaced with Color.Black.
 * [Notice] The variable snackbarData.visuals.message is only reflected in this function. Others will be ignored.
 */
@Composable
fun IosSimpleToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    shape: Shape = RoundedCornerShape(8.dp),
    fontFamily: FontFamily = FontFamily.Default,
    containerColor: Color = Color.White,
    contents: @Composable () -> Unit = {
        Text(snackbarData.visuals.message, color = Color(0xFF525358),  // Dark Grey Color
            fontSize = 15.sp, fontFamily = fontFamily, textAlign = TextAlign.Center)
    }
) {
    val details = snackbarData.visuals
    if (details is BillLetterVisual) {
        details.actionLabel = null
        details.actionOnNewLine = false
        details.withDismissAction = false
    }
    val backgroundColor = Color(0xF6dddfe3)  // Light Grey Color
    val contentColor = Color(0xFF525358)  // Dark Grey Color
    SnackBarToastWithTitle(
        snackbarData = snackbarData,
        modifier = modifier,
        shape = shape,
        actionColor = contentColor,
        bottomShift = 0.dp,
        snackBarColorData = SnackBarColorData(
            containerColor = backgroundColor,
            contentColor = contentColor,
            actionContentColor = contentColor,
            dismissActionContentColor = contentColor
        ),
        contentContainer = {
            Column(
                modifier = Modifier.padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                it()
            }
        },
        title = {},
        body = contents
    )
}