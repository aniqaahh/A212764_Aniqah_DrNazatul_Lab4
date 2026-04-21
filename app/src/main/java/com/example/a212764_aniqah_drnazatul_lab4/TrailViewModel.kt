package com.example.a212764_aniqah_drnazatul_lab4

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class TrailViewModel : ViewModel() {

    val allTrails = mutableStateListOf<Trail>()
    var selectedTrail = mutableStateOf<Trail?>(null)

    init {
        allTrails.addAll(
            listOf(
                Trail(
                    1,
                    "Gunung Datuk",
                    "Rembau, Negeri Sembilan",
                    "Moderate",
                    "4.6",
                    "4.7km",
                    "586m",
                    "3.5hr",
                    R.drawable.datuk
                ),
                Trail(
                    2,
                    "Taman Negeri Rompin",
                    "Kuala Rompin, Pahang",
                    "Hard",
                    "4.8",
                    "31.5km",
                    "1,048m",
                    "10hr",
                    R.drawable.rompin
                )
            )
        )
    }
}