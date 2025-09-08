package com.example.foodike.domain.repository

import com.example.foodike.data.repository.Results
import com.example.foodike.domain.model.Estado
import com.example.foodike.domain.model.Municipio
import com.example.foodike.domain.model.Producto

interface NegocioRepository {
    fun insertarNegocio(nombre: String, direccion: String, logo: ByteArray?, idEstado: Int, idMunicipio: Int): Long
    fun obtenerEstados(): List<Estado>
    fun obtenerMunicipiosPorEstado(idEstado: Int): List<Municipio>
}