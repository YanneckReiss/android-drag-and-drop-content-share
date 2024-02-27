package com.yanneckreiss.tutorial.ui.screens.timer

import androidx.compose.runtime.Stable

@Stable
data class TimerScreenState(
    val passedMilliseconds: Long = 0L
)
