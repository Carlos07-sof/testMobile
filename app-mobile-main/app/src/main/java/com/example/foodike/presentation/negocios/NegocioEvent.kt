package com.example.foodike.presentation.negocios

import com.example.foodike.domain.model.Estado
import com.example.foodike.domain.model.Municipio


sealed class NegocioEvent {
    object Loading : NegocioEvent()
    data class EstadosLoaded(val estados: List<Estado>) : NegocioEvent()
    data class MunicipiosLoaded(val municipios: List<Municipio>) : NegocioEvent()
    data class NegocioInserted(val id: Long) : NegocioEvent()
    data class Error(val message: String) : NegocioEvent()
}

