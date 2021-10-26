package com.example.dragsample


interface UiState

data class BottomNavUiState(val progress: Float = 0f, val bottomNavMenuExpanded: Boolean = true, val time: String = "") : UiState

interface BottomNavUiActions {
    fun onBottomMenuDragEnd(progress: Float) {}
    fun onMenuExpanded() {}
    fun onMenuCollapsed() {}
}