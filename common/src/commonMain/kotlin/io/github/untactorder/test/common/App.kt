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
import io.github.untactorder.toasterAtSnackBar.Bartender
import io.github.untactorder.toasterAtSnackBar.InjectableSnackBar
import io.github.untactorder.toasterAtSnackBar.SnackBarToastWithTitle

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

    var injector = InjectableSnackBar()

    injector.FloatingSnackBar(
        snackbarHost = { hostState ->
            Bartender(hostState = hostState)
        },
        snackBarAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(modifier = Modifier.padding(12.dp), onClick = {
                textState += "hi hihi ho hohohoohohohohho "
                injector.showSnackbar(
                    "textState : $textState",
                    title = "제목입니다.",
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
                    title = "제목",
                    withDismissAction = false,
                    customToastDesign = { data ->
                        SnackBarToastWithTitle(data, shape = RoundedCornerShape(36.dp))
                    }
                )
            }) {
                Text("Show Toast")
            }
        }

    }
}
