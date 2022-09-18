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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.StringDesc
import io.github.untactorder.shared.MR


@Composable
fun SnackBarToast(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier.padding(12.dp),
    shape: Shape = RoundedCornerShape(10.dp),
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
            Column(Modifier.padding(bottom = 10.dp), content = { contents() })
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
    actionColor: Color = Color(248, 228, 224),
    snackBarColorData: SnackBarColorData = SnackBarColorData(
        containerColor = Color(105, 84, 86),
        contentColor = Color(239, 237, 237),
        actionContentColor = actionColor,
        dismissActionContentColor = actionColor
    ),  // Material 3
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
    SnackBarToast(snackbarData, modifier, shape, actionColor, snackBarColorData) {
        val details = snackbarData.visuals
        Column {
            title(details)
            body()
        }
    }
}


data class SnackBarColorData(
    val containerColor: Color,
    val contentColor: Color,
    val actionContentColor: Color,
    val dismissActionContentColor: Color
)
