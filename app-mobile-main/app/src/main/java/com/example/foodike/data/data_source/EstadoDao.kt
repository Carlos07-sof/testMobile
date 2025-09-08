package com.example.foodike.data.data_source

import android.content.Context
import com.example.foodike.domain.model.Estado
import com.example.foodike.domain.model.Municipio

class EstadoDao(context: Context) {
    private val dbHelper = DBHelper(context)

    fun getEstados(): List<Estado> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre FROM estados", null)
        val lista = mutableListOf<Estado>()

        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Estado(
                        id = cursor.getInt(0),
                        nombre = cursor.getString(1)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }

    fun getMunicipiosByEstado(estadoId: Int): List<Municipio> {
        val db = dbHelper.readableDatabase
        val cursor = db.rawQuery("SELECT id, nombre, estado_id FROM municipios WHERE estado_id = ?", arrayOf(estadoId.toString()))
        val lista = mutableListOf<Municipio>()

        if (cursor.moveToFirst()) {
            do {
                lista.add(
                    Municipio(
                        id = cursor.getInt(0),
                        nombre = cursor.getString(1),
                        estadoId = cursor.getInt(2)
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return lista
    }
}
