package com.example.dragsample

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DragViewModel : ViewModel(), BottomNavUiActions {
    private val ticker: Ticker = Ticker()
    val uiState: BottomNavUiState
        get() = uiStateLiveData.value ?: throw IllegalStateException("UiState is null")
    private val _uiStateLiveData: MutableLiveData<BottomNavUiState> = MutableLiveData()
    val uiStateLiveData: LiveData<BottomNavUiState> get() = _uiStateLiveData

    init {
        _uiStateLiveData.value = BottomNavUiState(0f)
        val startTime = System.currentTimeMillis()
        ticker.start {
            updateUiState { uiState ->
                uiState.copy(
                    time = getCurrentDuration(startTime)
                )
            }
        }
    }

    override fun onBottomMenuDragEnd(progress: Float) {
        updateUiState { uiState ->
            uiState.copy(progress = progress)
        }
    }

    @SuppressLint("RestrictedApi")
    fun updateUiState(update: (BottomNavUiState) -> BottomNavUiState) {
        uiStateLiveData.value?.let {
            val newModel = update(it)

            check(!(newModel === uiStateLiveData.value)) { "UiModel is the same object. Use .copy" }

            _uiStateLiveData.value = newModel
        } ?: throw IllegalStateException("UiModel is null")
    }

    private fun getCurrentDuration(startTime: Long): String {
        val current = System.currentTimeMillis() - startTime

        return (current / 1000).toInt().toString().toDurationString()
    }


    private fun String.toDurationString(): String {
        val durationInSeconds = this.toInt()
        var min = (durationInSeconds / 60).toString()
        var secs = (durationInSeconds % 60).toString()

        min = if (min.length == 1) "0$min" else min
        secs = if (secs.length == 1) "0$secs" else secs

        return "$min:$secs"
    }
}