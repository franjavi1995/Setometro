package com.setometro.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.setometro.ui.screens.*
import com.setometro.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope

@Composable
fun SetometroNavHost(navController: NavHostController, mainViewModel: MainViewModel = viewModel()) {
    NavHost(navController, startDestination = "dashboard") {
        composable("dashboard") { DashboardScreen() }
        composable("seleccion_cotos") { SeleccionCotosScreen(navController) }
        composable("detalle_coto") { DetalleCotoScreen() }
        composable("setas") { SetasScreen() }
        composable("configuracion") { ConfiguracionScreen() }
        composable("nuevo_coto") {
            NuevoCotoScreen(
                onCotoCreado = { coto ->
                    mainViewModel.insertarCoto(coto) {
                        navController.popBackStack()
                    }
                },
                onCancelar = { navController.popBackStack() }
            )
        }
    }
}
