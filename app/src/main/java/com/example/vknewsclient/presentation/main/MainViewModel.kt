package com.example.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import com.example.vknewsclient.domain.usecase.GetAuthentificationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAuthentificationUseCase: GetAuthentificationUseCase
) : ViewModel() {

    private val TAG = "MainViewModel"

    val state = getAuthentificationUseCase()

}