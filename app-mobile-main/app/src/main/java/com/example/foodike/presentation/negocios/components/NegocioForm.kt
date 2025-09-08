package com.example.foodike.presentation.negocios.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.foodike.presentation.negocios.NegocioViewModel
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodike.presentation.negocios.NegocioEvent
import kotlinx.coroutines.launch

// ---------------------------- Composables ----------------------------
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NegocioForm(viewModel: NegocioViewModel = hiltViewModel()) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    val estados by viewModel.estados.collectAsState()
    val municipios by viewModel.municipios.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    // Limpiar inputs cuando se inserta con éxito
    LaunchedEffect(mensaje) {
        mensaje?.let { msg ->
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar(msg)
                viewModel.limpiarMensaje()
            }
        }
    }

    Scaffold(scaffoldState = scaffoldState) { padding ->
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // Nombre
            OutlinedTextField(
                value = viewModel.nombreNegocio,
                onValueChange = { viewModel.nombreNegocio = it },
                label = { Text("Nombre del negocio") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Dirección
            OutlinedTextField(
                value = viewModel.direccionNegocio,
                onValueChange = { viewModel.direccionNegocio = it },
                label = { Text("Dirección") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Estado Dropdown
            DropdownSelector(
                label = "Estado",
                items = estados,
                selectedItem = viewModel.selectedEstado,
                onItemSelected = { estado ->
                    viewModel.selectedEstado = estado
                    viewModel.cargarMunicipios(estado.id)
                },
                itemLabel = { it.nombre }

            )

            Spacer(modifier = Modifier.height(8.dp))

            // Municipio Dropdown
            DropdownSelector(
                label = "Municipio",
                items = municipios,
                selectedItem = viewModel.selectedMunicipio,
                onItemSelected = { municipio ->
                    viewModel.selectedMunicipio = municipio
                },
                itemLabel = { it.nombre }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Botón agregar
            Button(
                onClick = { viewModel.insertarNegocio() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agregar negocio")
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun <T> DropdownSelector(
    label: String,
    items: List<T>,
    selectedItem: T?,
    onItemSelected: (T) -> Unit,
    itemLabel: (T) -> String
) where T : Any {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = (selectedItem?.let { itemLabel(it) } ?: "Seleccionar $label"),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach { item ->
                DropdownMenuItem(onClick = {
                    onItemSelected(item)
                    expanded = false
                }) {
                    Text(itemLabel(item))
                }
            }
        }
    }
}

