package com.yanneckreiss.contentreceiveapp.ui.screens

import android.content.ClipData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReceiveContentViewModel : ViewModel() {

    private val _state = MutableStateFlow(ReceiveContentState())
    val state: StateFlow<ReceiveContentState> = _state.asStateFlow()

    fun setDroppedContent(clipData: ClipData) {

        val pastedText: String = (0 until clipData.itemCount)
            .map { index -> clipData.getItemAt(index).text }
            .filter { pastedText -> pastedText.isNotBlank() }
            .joinToString(separator = "\n")

        _state.update { current -> current.copy(text = pastedText) }
    }
}
