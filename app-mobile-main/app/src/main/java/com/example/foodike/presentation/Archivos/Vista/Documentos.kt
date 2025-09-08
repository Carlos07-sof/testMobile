package com.example.foodike.presentation.Archivos.Vista

import android.content.ContentResolver
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.foodike.presentation.util.Screen
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Documentos(navController: NavController) {
    val resolver: ContentResolver = LocalContext.current.contentResolver
    val documents = getDocuments(resolver)
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text(text = "Documentos") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Archivos.route) {
                            popUpTo(Screen.Archivos.route) { inclusive = true }
                        }
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBackIosNew,
                            contentDescription = "Regresar"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
            )
        }
    ) { paddingValues ->

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            if (documents.isNotEmpty()) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(documents) { document ->
                        Text(text = document)
                    }
                }
            } else {
                Text(text = "No se encontraron documentos")
            }
        }
    }

}

@Composable
fun DocumentItem(document: String) {
    val context = LocalContext.current

    // Obtén la ruta completa del archivo (asegúrate de usar el directorio correcto)
    val filePath = "/storage/emulated/0/Documents/$document"  // Ajusta esta ruta según sea necesario
    val file = File(filePath)

    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Icon(
            imageVector = Icons.Filled.InsertDriveFile,
            contentDescription = "Documento",
            modifier = Modifier.padding(end = 16.dp),
            tint = Color.Gray
        )

        // Nombre del documento
        Text(text = document, modifier = Modifier.weight(1f))

        // Ícono para abrir el documento
        IconButton(onClick = {
            openDocument(context, file)
        }) {
            Icon(
                imageVector = Icons.Filled.InsertDriveFile, // O usa otro ícono si prefieres
                contentDescription = "Abrir PDF",
                tint = Color.Blue
            )
        }
    }
}

// Función para abrir el documento PDF usando una ruta del sistema
fun openDocument(context: android.content.Context, file: File) {
    if (file.exists()) {
        val uri: Uri = Uri.fromFile(file)
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            flags = Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_GRANT_READ_URI_PERMISSION
        }

        // Intentamos abrir el archivo con cualquier aplicación disponible
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No se puede abrir el archivo PDF", Toast.LENGTH_SHORT).show()
        }
    } else {
        Toast.makeText(context, "El archivo no existe", Toast.LENGTH_SHORT).show()
    }
}

fun getDocuments(contentResolver: ContentResolver): List<String> {
    val documents = mutableListOf<String>()

    val uri: Uri = MediaStore.Files.getContentUri("external")
    val projection = arrayOf(MediaStore.Files.FileColumns.DISPLAY_NAME)
    val selection = "${MediaStore.Files.FileColumns.MIME_TYPE} = ?"
    val selectionArgs = arrayOf("application/pdf")

    val cursor: Cursor? = contentResolver.query(
        uri,
        projection,
        selection,
        selectionArgs,
        null
    )

    cursor?.use {
        val nameColumn = it.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)

        if (it.moveToFirst()) {
            do {
                val name = it.getString(nameColumn)
                documents.add(name)
            } while (it.moveToNext())
        }
    }

    return documents
}