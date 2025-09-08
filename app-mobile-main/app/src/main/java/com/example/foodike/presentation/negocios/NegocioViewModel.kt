package com.example.foodike.presentation.negocios

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodike.domain.model.LoadState
import com.example.foodike.domain.repository.NegocioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.foodike.domain.model.Estado
import com.example.foodike.domain.model.Municipio
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ---------------------------- ViewModel ----------------------------
@HiltViewModel
class NegocioViewModel @Inject constructor(
    private val repository: NegocioRepository
) : ViewModel() {

    var nombreNegocio by mutableStateOf("")
    var direccionNegocio by mutableStateOf("")
    var selectedEstado by mutableStateOf<Estado?>(null)
    var selectedMunicipio by mutableStateOf<Municipio?>(null)

    private val _estados = MutableStateFlow<List<Estado>>(emptyList())
    val estados: StateFlow<List<Estado>> = _estados

    private val _municipios = MutableStateFlow<List<Municipio>>(emptyList())
    val municipios: StateFlow<List<Municipio>> = _municipios

    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje

    init {
        cargarEstados()
    }

    fun cargarEstados() {
        viewModelScope.launch {
            try {
                _estados.value = repository.obtenerEstados()
            } catch (e: Exception) {
                _mensaje.value = e.localizedMessage ?: "Error al cargar estados"
            }
        }
    }

    fun cargarMunicipios(estadoId: Int) {
        viewModelScope.launch {
            try {
                _municipios.value = repository.obtenerMunicipiosPorEstado(estadoId)
                selectedMunicipio = null
            } catch (e: Exception) {
                _mensaje.value = e.localizedMessage ?: "Error al cargar municipios"
            }
        }
    }

    fun insertarNegocio() {
        // Validaciones
        if (nombreNegocio.isBlank()) {
            _mensaje.value = "Debe ingresar un nombre"
            return
        }
        if (direccionNegocio.isBlank()) {
            _mensaje.value = "Debe ingresar una dirección"
            return
        }
        val estadoId = selectedEstado?.id ?: run {
            _mensaje.value = "Debe seleccionar un estado"
            return
        }
        val municipioId = selectedMunicipio?.id ?: run {
            _mensaje.value = "Debe seleccionar un municipio"
            return
        }

        viewModelScope.launch {
            try {
                val id = repository.insertarNegocio(nombreNegocio, direccionNegocio, null, estadoId, municipioId)
                _mensaje.value = "Negocio agregado con ID: $id"
                limpiarCampos()
            } catch (e: Exception) {
                _mensaje.value = e.localizedMessage ?: "Error al agregar negocio"
            }
        }
    }

    fun limpiarCampos() {
        nombreNegocio = ""
        direccionNegocio = ""
        selectedEstado = null
        selectedMunicipio = null
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }
}
