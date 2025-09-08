package com.example.foodike.presentation.Archivos.Componentes

import android.content.Context
import android.provider.MediaStore

fun getPhotoCount(context: Context): Int {
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

    val cursor = context.contentResolver.query(uri, projection, null, null, null)
    val count = cursor?.count ?: 0
    cursor?.close()
    return count
}
