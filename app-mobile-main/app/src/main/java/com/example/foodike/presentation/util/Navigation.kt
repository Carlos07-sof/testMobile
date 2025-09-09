package com.example.foodike.presentation.util

import Imagen
import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.net.Uri
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.foodike.presentation.Archivos.Vista.Archivos
import com.example.foodike.presentation.Archivos.Vista.Documentos
import com.example.foodike.presentation.Archivos.Vista.Permission
import com.example.foodike.presentation.cart.Cart
import com.example.foodike.presentation.common.SplashViewModel
import com.example.foodike.presentation.components.SplashScreen
import com.example.foodike.presentation.details.RestaurantDetail
import com.example.foodike.presentation.history.History
import com.example.foodike.presentation.home.Home
import com.example.foodike.presentation.home.components.FoodikeBottomNavigation
import com.example.foodike.presentation.login.LoginScreen
import com.example.foodike.presentation.negocios.NegociosScreen
import com.example.foodike.presentation.profile.Profile
import com.example.foodike.presentation.scanner.BluetoothScreen
import com.example.foodike.presentation.tasks.Tareas
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_JPEG
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.SCANNER_MODE_FULL
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions.RESULT_FORMAT_PDF
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalPagerApi::class)
@Composable
fun NavigationGraph(
    navController: NavHostController,
    startDestination: String,
    scrollState: LazyListState
) {

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.SplashScreen.route,
        ) {
            SplashScreen(navController = navController)
        }
        composable(
            route = Screen.LoginScreen.route,
        ) {
            LoginScreen(navController = navController)
        }

        composable(
            route = Screen.Home.route
        ) {
            Home(navController = navController, scrollState = scrollState)
        }
        composable(
            route = Screen.History.route
        ) {
            History(navHostController = navController)
        }
        composable(
            route = Screen.Cart.route
        ) {
            Cart(navController = navController)
        }
        composable(
            route = Screen.Profile.route
        ) {
            Profile(navController = navController)
        }
        composable(
            route = Screen.Tareas.route
        ){
            Tareas(navController = navController)
        }
        composable(
            route = Screen.Archivos.route
        ){
            Archivos(navController = navController)
        }
        composable(
            route = Screen.Permission.route
        ){
            Permission(navController = navController)
        }
        composable(
            route = Screen.Imagen.route
        ){
            Imagen()
        }
        composable(
            route = Screen.Documentos.route
        ){
            Documentos(navController = navController)
        }

        composable(
            route = Screen.Negocios.route
        ){
            NegociosScreen(navController = navController)
        }

        composable(
            route = Screen.Escaner.route
        ){
            BluetoothScreen()
        }



//        composable(
//            route = Screen.Onboarding.route,
//        ) {
//            OnBoarding(navController = navController)
//        }
        composable(
            route = Screen.RestaurantDetails.route,
        ) {
            RestaurantDetail(
                navController = navController
            )
        }
    }

}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun SetupNavigation(startDestination: String) {

    val navController = rememberNavController()


    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current

    val scrollState = rememberLazyListState()
    val state by remember { derivedStateOf { scrollState.firstVisibleItemIndex == 0 } }

    val options = GmsDocumentScannerOptions.Builder()
        .setGalleryImportAllowed(false)
        .setPageLimit(5)
        .setResultFormats(RESULT_FORMAT_JPEG, RESULT_FORMAT_PDF)
        .setScannerMode(SCANNER_MODE_FULL)
        .build()

    val scanner = GmsDocumentScanning.getClient(options)

    Scaffold(
        bottomBar = {
            if ((currentRoute == Screen.Home.route || currentRoute == Screen.History.route) && state) {

                Column(
                    modifier = Modifier.padding(115.dp, 25.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var imagenUris by remember {
                        mutableStateOf<List<Uri>>(emptyList())
                    }
                    val scannerLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartIntentSenderForResult(),
                        onResult = { result ->
                            if (result.resultCode == RESULT_OK) {
                                val scanResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                                imagenUris = scanResult?.pages?.map { it.imageUri } ?: emptyList()

                                scanResult?.pdf?.let { pdf ->
                                    // Obtener la fecha actual
                                    val dateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                                    val currentDate = dateFormat.format(Date())

                                    // Nombre del archivo con la fecha
                                    val fileName = "scan_$currentDate.pdf"

                                    // Directorio donde guardar el archivo
                                    val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
                                    val pdfFile = File(storageDir, fileName)

                                    try {
                                        val fos = FileOutputStream(pdfFile)
                                        context.contentResolver.openInputStream(pdf.uri)?.use { inputStream ->
                                            inputStream.copyTo(fos)
                                        }
                                        fos.close()

                                        println("Archivo guardado en: ${pdfFile.absolutePath}")
                                    } catch (e: Exception) {
                                        println("Error al guardar el archivo: ${e.message}")
                                    }
                                }
                            }
                        }
                    )
//                    val scannerLauncher = rememberLauncherForActivityResult(
//                        contract = ActivityResultContracts.StartIntentSenderForResult(),
//                        onResult = {
//                            if (it.resultCode == RESULT_OK){
//                                val result = GmsDocumentScanningResult.fromActivityResultIntent(it.data)
//                                imagenUris = result?.pages?.map { it.imageUri } ?: emptyList()
//
//                                result?.pdf?.let { pdf ->
//                                    val fos = FileOutputStream(File(context.filesDir,"scan.pdf"))
//                                    context.contentResolver.openInputStream(pdf.uri)?.use {
//                                        it.copyTo(fos)
//                                    }
//                                }
//
//
//                            }
//                        }
//                    )
                    Box(
                        contentAlignment = Alignment.Center
                    ) {
                        imagenUris.forEach { uri ->
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.FillWidth,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        BottomBar(navController = navController)
                        Column {
                            FloatingActionButton(
                                onClick = {

//                                    scanner.getStartScanIntent(context as Activity)
//                                        .addOnSuccessListener {
//                                            scannerLauncher.launch(
//                                                IntentSenderRequest.Builder(it).build()
//                                            )
//                                        }
//                                        .addOnFailureListener {
//                                            println("Hubo un error: ${it.message}")
//                                        }
                                },
                                backgroundColor = MaterialTheme.colors.primary
                            ) {
                                Icon(Icons.Outlined.ShoppingCart, "Cart")
                            }
                            Spacer(modifier = Modifier.height(26.dp))
                        }
                    }
                }
            }
        }
    ) {
        NavigationGraph(
            navController = navController,
            scrollState = scrollState,
            startDestination = startDestination
        )
        // SplashScreen(navController = navController)
    }
}



@Composable
fun BottomBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    FoodikeBottomNavigation(
        backgroundColor = Color(0xFFE1E1E1)

    ) {
        BottomNavigationItem(
            icon =
            {
                Icon(
                    imageVector = Icons.Filled.Home,
                    contentDescription = stringResource(com.example.foodike.R.string.home),
                )
            },

            selectedContentColor = Color.Black,
            unselectedContentColor = Color.White,
            alwaysShowLabel = false,
            selected = currentRoute == Screen.Home.route,

            onClick = {
                navController.navigate(Screen.Home.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            }
        )

        Row() {
            Spacer(modifier = Modifier.width(56.dp))
        }


        BottomNavigationItem(
            icon =
            {
                Icon(
                    painter = painterResource(com.example.foodike.R.drawable.ic_baseline_assignment_24),
                    contentDescription = stringResource(com.example.foodike.R.string.history),
                )
            },

            selectedContentColor = Color.Black,
            unselectedContentColor = Color.White,
            alwaysShowLabel = false,
            selected = currentRoute == Screen.History.route,
            onClick = {
                navController.navigate(Screen.History.route) {
                    // Pop up to the start destination of the graph to
                    // avoid building up a large stack of destinations
                    // on the back stack as users select items
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    // Avoid multiple copies of the same destination when
                    // reselecting the same item
                    launchSingleTop = true
                    // Restore state when reselecting a previously selected item
                    restoreState = true
                }
            }
        )

    }
    @Composable
    fun SplashScreen(navController: NavHostController) {

        val viewModel: SplashViewModel = viewModel()

        val isLoading by viewModel.isLoading.collectAsState()
        val startDestination by viewModel.startDestination

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center)
            ) {
                CircularProgressIndicator()
            }
        } else {
            LaunchedEffect(startDestination) {
                navController.navigate(startDestination) {
                    popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                }
            }
        }
    }
}
