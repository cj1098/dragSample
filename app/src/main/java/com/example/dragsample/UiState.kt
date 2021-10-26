package com.example.dragsample


interface UiState

data class BottomNavUiState(val progress: Float = 0f, val bottomNavMenuExpanded: Boolean = false,) : UiState

interface BottomNavUiActions {
    fun onBottomMenuDragEnd(progress: Float) {}
    fun onMenuExpanded() {}
    fun onMenuCollapsed() {}

    companion object {
        fun default() = object : BottomNavUiActions {}
    }
}