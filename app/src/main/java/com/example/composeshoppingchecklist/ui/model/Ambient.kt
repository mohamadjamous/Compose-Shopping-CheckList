package com.example.composeshoppingchecklist.ui.model


import androidx.compose.runtime.compositionLocalOf
import com.example.composeshoppingchecklist.ui.model.model.ScreenUi

val AmbientSharedViewModel = compositionLocalOf<SharedViewModel> {
    throw IllegalStateException("SharedViewModel is not initialized")
}

val AmbientScreenState = compositionLocalOf<ScreenUi> {
    throw IllegalStateException("SharedViewModel is not initialized")
}