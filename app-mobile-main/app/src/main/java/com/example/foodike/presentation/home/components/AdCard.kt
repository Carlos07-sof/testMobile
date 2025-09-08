package com.example.foodike.presentation.home.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.foodike.R
import com.example.foodike.domain.model.Advertisement
import com.example.foodike.presentation.util.Screen

@Composable
fun AdCard(
    ad: Advertisement,
    navController: NavController
) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        shape = RoundedCornerShape(24.dp),
        backgroundColor = ad.color
    ) {
        Row(
            modifier = Modifier
                .clickable {
                    navController.navigate(ad.rutas){
                    popUpTo(Screen.Home.route) { inclusive = true }
                }}
                .animateContentSize()
                .padding(16.dp)
                .size(210.dp, 140.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(0.5f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start) {
                Text(
                    text = ad.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            Image(
                painter = painterResource(id = ad.image),
                contentDescription = stringResource(R.string.ad),
                modifier = Modifier
                    .size(150.dp)
                    .weight(0.5f)
            )
//            if (expanded) {
//                when (ad.rutas) {
//                    Screen.Home.route -> {
//                        navController.navigate(Screen.Home.route) {
//                            popUpTo(Screen.Home.route) { inclusive = true }
//                        }
//                    }
//                    Screen.Tareas.route -> {
//                        navController.navigate(Screen.Tareas.route)
//                    }
//                    else -> {
//                        // Acción por defecto si ad.rutas no coincide con ninguna ruta definida
//                        // Puedes manejar un caso por defecto aquí si es necesario
//                    }
//                }
//            }

//            if (expanded) {
//                navController.navigate(ad.rutas){
//                    popUpTo(Screen.Home.route) { inclusive = true }
//                }
//            }

        }

    }
}




