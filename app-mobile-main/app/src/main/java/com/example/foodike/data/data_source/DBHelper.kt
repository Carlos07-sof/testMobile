package com.example.foodike.data.data_source

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context) : SQLiteOpenHelper(context, "foodike.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        // Tabla Estados
        db.execSQL(
            "CREATE TABLE estados (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL)"
        )

        // Tabla Municipios
        db.execSQL(
            "CREATE TABLE municipios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "id_estado INTEGER," +
                    "FOREIGN KEY(id_estado) REFERENCES estados(id_estado))"
        )

        // Tabla Negocios
        db.execSQL(
            "CREATE TABLE negocios (" +
                    "id_negocio INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "direccion TEXT," +
                    "logo BLOB," +
                    "id_estado INTEGER," +
                    "id_municipio INTEGER," +
                    "FOREIGN KEY(id_estado) REFERENCES estados(id_estado)," +
                    "FOREIGN KEY(id_municipio) REFERENCES municipios(id_municipio))"
        )

        // --- Insertar datos iniciales ---
        // Estados
        db.execSQL("INSERT INTO estados (nombre) VALUES ('Jalisco')")
        db.execSQL("INSERT INTO estados (nombre) VALUES ('Nuevo León')")
        db.execSQL("INSERT INTO estados (nombre) VALUES ('CDMX')")

        // Municipios relacionados
        db.execSQL("INSERT INTO municipios (nombre, id_estado) VALUES ('Guadalajara', 1)")
        db.execSQL("INSERT INTO municipios (nombre, id_estado) VALUES ('Zapopan', 1)")
        db.execSQL("INSERT INTO municipios (nombre, id_estado) VALUES ('Monterrey', 2)")
        db.execSQL("INSERT INTO municipios (nombre, id_estado) VALUES ('San Nicolás', 2)")
        db.execSQL("INSERT INTO municipios (nombre, id_estado) VALUES ('Coyoacán', 3)")
        db.execSQL("INSERT INTO municipios (nombre, id_estado) VALUES ('Benito Juárez', 3)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS negocios")
        db.execSQL("DROP TABLE IF EXISTS municipios")
        db.execSQL("DROP TABLE IF EXISTS estados")
        onCreate(db)
    }

    // --- CRUD Negocios ---
    fun insertarNegocio(nombre: String, direccion: String, logo: ByteArray?, idEstado: Int, idMunicipio: Int): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("direccion", direccion)
            put("logo", logo)
            put("id_estado", idEstado)
            put("id_municipio", idMunicipio)
        }
        return db.insert("negocios", null, values)
    }

    fun actualizarNegocio(idNegocio: Int, nombre: String, direccion: String, logo: ByteArray?, idEstado: Int, idMunicipio: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nombre", nombre)
            put("direccion", direccion)
            put("logo", logo)
            put("id_estado", idEstado)
            put("id_municipio", idMunicipio)
        }
        return db.update("negocios", values, "id_negocio=?", arrayOf(idNegocio.toString()))
    }

    fun obtenerNegocios(): Cursor {
        val db = readableDatabase
        return db.rawQuery(
            "SELECT n.id_negocio, n.nombre, n.direccion, e.nombre AS estado, m.nombre AS municipio " +
                    "FROM negocios n " +
                    "JOIN estados e ON n.id_estado = e.id_estado " +
                    "JOIN municipios m ON n.id_municipio = m.id_municipio",
            null
        )
    }

    fun eliminarNegocio(idNegocio: Int): Int {
        val db = writableDatabase
        return db.delete("negocios", "id_negocio=?", arrayOf(idNegocio.toString()))
    }

    // --- Consultas auxiliares ---
    fun obtenerEstados(): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM estados", null)
    }

    fun obtenerMunicipiosPorEstado(idEstado: Int): Cursor {
        val db = readableDatabase
        return db.rawQuery("SELECT * FROM municipios WHERE id_estado=?", arrayOf(idEstado.toString()))
    }
}
