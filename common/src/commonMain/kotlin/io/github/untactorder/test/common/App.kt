package io.github.untactorder.test.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.untactorder.toasterAtSnackBar.*

@Composable
fun App(modifier: Modifier = Modifier.fillMaxSize()) {
    MaterialTheme {
        /*val scrollState = rememberScrollState()
        Column(Modifier.fillMaxSize().verticalScroll(scrollState)) {
            val injector = InjectableSnackBar()

            Column(modifier) {
                var text by remember { mutableStateOf("Hello, World!") }

                Button(modifier = Modifier, onClick = {
                    text = "Hello, ${getPlatformName()}"
                }) {
                    Text(text)
                }

                injector.EmbeddedSnackBar { snackBar ->
                    Text("Show SnackBar")
                }
            }
        }*/
        MainScreen2()
    }
}



@Composable
fun MainScreen2() {
    var textState by rememberSaveable { mutableStateOf("") }
    var boolState by rememberSaveable { mutableStateOf(true) }

    var injector = InjectableSnackBar()
    var injector2 = InjectableSnackBar()

    injector.FloatingSnackBar(
        snackBarAlignment = Alignment.BottomStart
    ) {
        injector2.FloatingSnackBar(
            snackBarModifier = Modifier.wrapContentSize(),
            snackBarAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
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
                        customToastDesign = { data ->
                            SnackBarToastWithTitle(data)
                        }
                    )
                }) {
                    Text("Sweet Toast Info")
                }

                Button(modifier = Modifier.padding(12.dp), onClick = {
                    injector.showSnackbar(
                        "textState : $textState",
                        title = "Title",
                        withDismissAction = false,
                        customToastDesign = { data ->
                            SnackBarToastWithTitle(data, shape = RoundedCornerShape(36.dp))
                        }
                    )
                }) {
                    Text("Show Toast")
                }

                Button(modifier = Modifier.padding(12.dp), onClick = {
                    injector2.showSnackbar(
                        "Please retry",
                        title = "An Error Occurred",
                        withDismissAction = true,
                        customToastDesign = { data ->
                            IosStypeToast(data, containerColor = Color.Black, modifier = Modifier
                                .width(400.dp).padding(12.dp))
                        }
                    )
                }) {
                    Text("Show IOS Toast")
                }

                Button(modifier = Modifier.padding(12.dp), onClick = {
                    boolState = !boolState
                    injector2.showSnackbar(
                        "Hi there! Welcome to the Toast! Have a nice day!",
                        customToastDesign = { data ->
                            IosSimpleToast(data, darkBackground = boolState, modifier = Modifier
                                .width(200.dp).padding(12.dp))
                        }
                    )
                }) {
                    Text("Show IOS Simple Toast")
                }
            }
        }
    }
}
