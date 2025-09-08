package com.example.foodike.data.data_source

import android.content.Context

fun getUsuarios(context: Context): List<Pair<String, String>> {
    val db = DBHelper(context).readableDatabase
    val cursor = db.rawQuery("SELECT nombre, email FROM usuarios", null)
    val lista = mutableListOf<Pair<String, String>>()

    if (cursor.moveToFirst()) {
        do {
            val nombre = cursor.getString(0)
            val email = cursor.getString(1)
            lista.add(nombre to email)
        } while (cursor.moveToNext())
    }

    cursor.close()
    db.close()
    return lista
}
