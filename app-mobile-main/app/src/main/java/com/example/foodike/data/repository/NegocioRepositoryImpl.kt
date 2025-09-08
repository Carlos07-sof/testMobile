package com.example.foodike.data.repository

import com.example.foodike.data.data_source.DBHelper
import com.example.foodike.domain.model.Estado
import com.example.foodike.domain.model.Municipio
import com.example.foodike.domain.repository.NegocioRepository

class NegocioRepositoryImpl(private val dbHelper: DBHelper) : NegocioRepository {
    override fun insertarNegocio(nombre: String, direccion: String, logo: ByteArray?, idEstado: Int, idMunicipio: Int): Long {
        return dbHelper.insertarNegocio(nombre, direccion, logo, idEstado, idMunicipio)
    }

    override fun obtenerEstados(): List<Estado> {
        val cursor = dbHelper.obtenerEstados()
        val estados = mutableListOf<Estado>()
        while (cursor.moveToNext()) {
            estados.add(Estado(cursor.getInt(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("nombre"))))
        }
        cursor.close()
        return estados
    }

    override fun obtenerMunicipiosPorEstado(idEstado: Int): List<Municipio> {
        val cursor = dbHelper.obtenerMunicipiosPorEstado(idEstado)
        val municipios = mutableListOf<Municipio>()
        while (cursor.moveToNext()) {
            municipios.add(Municipio(cursor.getInt(cursor.getColumnIndexOrThrow("id")), cursor.getString(cursor.getColumnIndexOrThrow("nombre")), idEstado))
        }
        cursor.close()
        return municipios
    }
}
