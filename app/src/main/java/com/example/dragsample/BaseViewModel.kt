package com.example.dragsample

import android.annotation.SuppressLint
import androidx.compose.runtime.Stable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


@Stable
data class ItemData(
    val title: String,
    val key: String,
    val isLocked: Boolean = false,
    val url: String = ""
)

class BaseViewModel : ViewModel(), BottomNavUiActions {

    val uiState: BottomNavUiState
        get() = uiStateLiveData.value ?: throw IllegalStateException("UiState is null")
    protected val _uiStateLiveData: MutableLiveData<BottomNavUiState> = MutableLiveData()
    val uiStateLiveData: LiveData<BottomNavUiState> get() = _uiStateLiveData

    init {
        _uiStateLiveData.value = BottomNavUiState(0f)
    }

    override fun onBottomMenuDragEnd(progress: Float) {
        updateUiState { uiState ->
            uiState.copy(progress = progress)
        }
    }

    @SuppressLint("RestrictedApi")
    protected fun updateUiState(update: (BottomNavUiState) -> BottomNavUiState) {
        uiStateLiveData.value?.let {
            val newModel = update(it)

            check(!(newModel === uiStateLiveData.value)) { "UiModel is the same object. Use .copy" }

            _uiStateLiveData.value = newModel
        } ?: throw IllegalStateException("UiModel is null")
    }

}