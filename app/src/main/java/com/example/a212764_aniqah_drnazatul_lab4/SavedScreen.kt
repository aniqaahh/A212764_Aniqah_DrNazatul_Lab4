package com.example.a212764_aniqah_drnazatul_lab4

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun SavedScreen(navController: NavController, viewModel: TrailViewModel) {
    // Ambil list trail yang dah di-save dari ViewModel
    val savedTrails = viewModel.allTrails.filter { it.isSaved.value }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            "Saved Trails",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (savedTrails.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No saved trails yet.", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(savedTrails) { trail ->
                    TrailFullCard(trail = trail, navController = navController, viewModel = viewModel)
                }
            }
        }
    }
}