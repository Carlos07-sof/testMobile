package com.example.foodike.presentation.tasks.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodike.data.repository.RetrofitServiceFactory
import com.example.foodike.domain.model.Data
import com.example.foodike.domain.model.RemoteResult
import com.example.foodike.domain.model.ResponseData
import com.example.foodike.domain.model.TodoItem
import com.example.foodike.presentation.util.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    var expanded by remember { mutableStateOf(false) }

    var empleados by remember { mutableStateOf<List<ResponseData>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val response = RetrofitServiceFactory.makeRetrofitService().listaEmpleados()
            empleados = response
        } catch (e: Exception) {
            println(e.localizedMessage)
            errorMessage = "Error al cargar empleados: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Tareas")
                },
                navigationIcon = {
                    IconButton(onClick = {  expanded = false
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        } }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Regresar"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Agregar Tareas"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { values ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(values)
        ) {
            when {
                isLoading -> item {
                    Text(text = "Cargando empleados...", modifier = Modifier.padding(16.dp))
                }

                errorMessage != null -> item {
                    println(errorMessage)
                    Text(text = errorMessage!!, modifier = Modifier.padding(16.dp))
                }

                empleados.isNullOrEmpty() -> item {
                    Text(text = "No hay empleados disponibles", modifier = Modifier.padding(16.dp))
                }

                else -> {
                    items(empleados!!) { empleado ->
                        ListItem(
                            headlineContent = { Text(text = empleado.title) },
                            supportingContent = { Text(text = empleado.id.toString()) },
                            trailingContent = {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = empleado.id.toString()
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}
