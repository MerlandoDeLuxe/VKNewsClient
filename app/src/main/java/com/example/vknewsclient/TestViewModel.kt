package com.example.vknewsclient

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class TestViewModel : ViewModel() {
    private val TAG = "TestViewModel"
    val rejectedNames = listOf("Name1", "Name2", "Name3")
    private val _state = MutableLiveData(ApplicationState())
    val state: LiveData<ApplicationState> get() = _state

    fun updateValue(initialName: String) {
        if (!rejectedNames.contains(initialName)) {
            _state.value = _state.value?.copy(
                initialName = initialName,
                shouldDisplayName = true,
                greetings = "Добрый день, $initialName!"
            )
        } else {
            _state.value = _state.value?.copy(
                initialName = initialName,
                shouldDisplayName = false,
                greetings = "Вам тут не рады, $initialName!"
            )
        }
    }
}