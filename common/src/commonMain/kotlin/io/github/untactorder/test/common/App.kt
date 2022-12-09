package io.github.untactorder.test.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.untactorder.toasterAtSnackBar.*

@Composable
fun App(modifier: Modifier = Modifier.fillMaxSize()) {
    MaterialTheme {
        MainScreen(modifier)
    }
}


@Composable
fun MainScreen(modifier: Modifier = Modifier.fillMaxSize()) {
    val scrollState = rememberScrollState()
    var textState by rememberSaveable { mutableStateOf("") }
    var boolState by rememberSaveable { mutableStateOf(true) }

    InjectableSnackBar().FloatingSnackBar { injector ->
        Column(
            modifier = modifier.verticalScroll(scrollState),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(modifier = Modifier.padding(12.dp), onClick = {
                textState += "Hello World! "
                injector.showSnackbar(
                    "textState : $textState",
                    title = "Title",
                    actionLabel = "Do something",
                    actionOnNewLine = false,
                    snackBarAlignment = Alignment.BottomStart,
                    customToastDesign = { data ->
                        SnackBarToastWithTitle(data)
                    }
                )
            }) {
                Text("Basic Snackbar")
            }

            Button(modifier = Modifier.padding(12.dp), onClick = {
                injector.showSnackbar(
                    "textState : $textState",
                    title = "Title",
                    withDismissAction = false,
                    snackBarAlignment = Alignment.BottomStart,
                    customToastDesign = { data ->
                        SnackBarToastWithTitle(data, shape = RoundedCornerShape(36.dp),
                            contentsBoxModifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp))
                    }
                )
            }) {
                Text("Show Round-Shaped Toast")
            }
            val fancyBright = FancyBrightColorSet.toList()
            val fancyDark = FancyDarkColorSet.toList()
            Button(modifier = Modifier.padding(12.dp), onClick = {
                val index = listOf(0, 1, 2, 3, 4, 5, 6).random()
                injector.showSnackbar(
                    "This is a snackbar for test purpose.",
                    title = "Fancy Dark",
                    actionLabel = "Okay",
                    actionOnNewLine = false,
                    snackBarAlignment = Alignment.BottomStart,
                    customToastDesign = { data ->
                        SnackBarToastWithTitle(data, actionColor = fancyBright[index],
                            snackBarColorData = SnackBarColorData(
                                containerColor = fancyDark[index],
                                contentColor = fancyBright[index],
                                actionContentColor = fancyBright[index],
                                dismissActionContentColor = fancyBright[index]))
                    }
                )
            }) {
                Text("Fancy-Colored Snackbar")
            }

            Button(modifier = Modifier.padding(12.dp), onClick = {
                injector.showSnackbar(
                    "This is a error notification. Please retry the previous action",
                    title = "An Error Occurred",
                    withDismissAction = true,
                    actionLabel = "Okay",
                    snackBarAlignment = Alignment.BottomStart,
                    customToastDesign = { data ->
                        PastelToast(data, containerColor = PastelColorSet.toList().random())
                    }
                )
            }) {
                Text("Pastel Toast")
            }

            val isDialOpened = rememberSaveable { mutableStateOf(false) }
            val isRestored = remember { mutableStateOf(true) }
            val dialog = {
                injector.launchAlertDialogWithPastelToast(
                    "This is a error notification. Please retry the previous action",
                    title = "An Error Occurred",
                    withDismissAction = true,
                    actionLabel = "Okay",
                    actionOnNewLine = true,
                    isDialOpened = isDialOpened,
                    touchBlocking = true,
                    blockFilterColor = Color.Black,
                    enableOutsideClick = true
                )
            }
            if (isRestored.value) {
                isRestored.value = false
                if (isDialOpened.value) {
                    LaunchedEffect(null) { dialog() }
                }
            }
            Button(modifier = Modifier.padding(12.dp), onClick = dialog) {
                Text("Pastel AlertDialog")
            }

            Button(modifier = Modifier.padding(12.dp), onClick = {
                boolState = !boolState
                injector.showSnackbar(
                    "",
                    title = "An Error Occurred",
                    withDismissAction = false,
                    snackBarAlignment = Alignment.Center,
                    customToastDesign = { data ->
                        AndroidStyleToast(data, containerColor = FancyColorSet.toList().random(),
                            contentsBoxModifier = Modifier.padding(6.dp, 0.dp, 4.dp, 0.dp), toastWidth = 250.dp)
                    }
                )
            }) {
                Text("Show Android Toast")
            }

            Button(modifier = Modifier.padding(12.dp), onClick = {
                boolState = !boolState
                injector.showSnackbar(
                    "Please retry the previous action",
                    title = "An Error Occurred",
                    withDismissAction = true,
                    snackBarAlignment = Alignment.Center,
                    customToastDesign = { data ->
                        IosStyleToast(data, containerColor = if (boolState) Color.Black else Color.White,
                            toastWidth = 300.dp)
                    }
                )
            }) {
                Text("Show iOS Toast")
            }

            Button(modifier = Modifier.padding(12.dp), onClick = {
                boolState = !boolState
                injector.showSnackbar(
                    "Hi there! Welcome to the Toast! Have a nice day!",
                    snackBarAlignment = Alignment.Center,
                    customToastDesign = { data ->
                        IosSimpleToast(data, darkBackground = boolState, toastWidth = 250.dp)
                    }
                )
            }) {
                Text("Show iOS Simple Toast")
            }
        }
    }
}
