package io.github.untactorder.test.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.untactorder.toasterAtSnackBar.InjectableSnackBar

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
        snackbarHost = {
            Text("Show SnackBar")
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(modifier = Modifier.padding(12.dp), onClick = {
                injector.showSnackbar(
                    "textState : $textState",
                    actionLabel = "Do something"
                )
            }) {
                Text("Sweet Toast Info")
            }

            Column {
                //TextField(value = textState, onValueChange = { textValue -> textState = textValue })
            }
        }

    }
}
