package com.example.foodike.presentation.negocios

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.foodike.presentation.negocios.components.TopBar


@Composable
fun NegociosScreen(
    // viewModel: ProfileViewModel = hiltViewModel(),
    navController: NavController,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        TopBar(navController)
    }
}