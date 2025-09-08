package com.example.foodike.presentation.Archivos.Componentes

import android.content.Context
import android.provider.MediaStore

fun getFileCount(context: Context): Int {
    val projection = arrayOf(MediaStore.Files.FileColumns._ID)
    val uri = MediaStore.Files.getContentUri("external")

    val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} IN (?, ?, ?, ?)"
    val selectionArgs = arrayOf(
        "application/pdf",
        "application/msword",
        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
        "text/plain"
    )

    val cursor = context.contentResolver.query(uri, projection, selection, selectionArgs, null)
    val count = cursor?.count ?: 0
    cursor?.close()
    return count
}
