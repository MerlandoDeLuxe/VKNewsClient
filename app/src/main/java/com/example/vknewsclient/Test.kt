package com.example.vknewsclient

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun GreetingContainer() {
    val text = rememberSaveable { mutableStateOf("") }

    InputGreeting(
        text.value,
        onValueChange = {
            text.value = it
        }
    )
    Question(text.value)
}

@Composable
fun InputGreeting(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        modifier = Modifier.padding(150.dp),
        value = value,
        onValueChange = onValueChange
    )
}

@Composable
fun Question(value: String) {
    Text(
        modifier = Modifier.padding(100.dp),
        text = "Hello $value"
    )
}

data class ApplicationState(
    val initialName: String = "",
    val shouldDisplayName: Boolean = false,
    val greetings: String = ""
)

@Composable
fun CustomApplication() {
    val viewModel: TestViewModel = viewModel()
    val applicationState by viewModel.state.observeAsState(ApplicationState())


    InputGreeting(
        value = applicationState.initialName,
        onValueChange = {
            viewModel.updateValue(it)
        }
    )

    Column {
        Text(text = applicationState.greetings)
        Spacer(modifier = Modifier.width(50.dp))
        Text(
            modifier = Modifier.height(150.dp),
            text = applicationState.shouldDisplayName.toString()
        )
        Question(applicationState.initialName)
    }
}