package com.example.a212764_aniqah_drnazatul_lab4

import androidx.compose.runtime.mutableStateOf

data class Trail(
    val id: Int,
    val title: String,
    val location: String,
    val level: String,
    val rating: String,
    val length: String,
    val elevation: String,
    val time: String,
    val imageRes: Int,
    var isSaved: androidx.compose.runtime.MutableState<Boolean> = mutableStateOf(false)
)