package com.breens.detectscreenshots

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor() : ViewModel() {
    private val _uiEvents = MutableSharedFlow<UiEvents>()
    val uiEvents: SharedFlow<UiEvents> = _uiEvents.asSharedFlow()

    fun showSnackBar(message: String) {
        viewModelScope.launch {
            _uiEvents.emit(
                UiEvents.ShowSnackBar(message = message),
            )
        }
    }
}

sealed class UiEvents {
    data class ShowSnackBar(val message: String) : UiEvents()
}
