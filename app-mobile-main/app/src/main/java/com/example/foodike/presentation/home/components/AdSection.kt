package com.example.foodike.presentation.home.components
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodike.domain.model.Advertisement

@Composable
fun AdSection(
    adsList: List<Advertisement>,
    navController: NavController

) {
    Column()
    {

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow(
            contentPadding = PaddingValues(
                start = 8.dp,
                end = 8.dp
            ),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(adsList.size) {
                AdCard(
                    adsList[it],
                    navController
                )
            }
        }
    }
}
