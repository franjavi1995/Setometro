package com.setometro.app.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.setometro.app.ui.screens.*

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : Screen("dashboard", "Mis Cotos", Icons.Default.Home)
    object Cotos : Screen("cotos", "Cotos", Icons.Default.LocationOn)
    object Mapa : Screen("mapa", "Mapa", Icons.Default.Place)
    object Setas : Screen("setas", "Setas", Icons.Default.Star)
    object Configuracion : Screen("configuracion", "Config", Icons.Default.Settings)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetometroNavigation() {
    val navController = rememberNavController()
    val bottomNavItems = listOf(
        Screen.Dashboard,
        Screen.Cotos,
        Screen.Mapa,
        Screen.Setas,
        Screen.Configuracion
    )
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                bottomNavItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToDetalle = { cotoId ->
                        navController.navigate("detalle_coto/$cotoId")
                    }
                )
            }
            
            composable(Screen.Cotos.route) {
                CotosScreen(
                    onNavigateToDetalle = { cotoId ->
                        navController.navigate("detalle_coto/$cotoId")
                    },
                    onNavigateToAddCoto = {
                        navController.navigate("add_coto")
                    }
                )
            }
            
                composable(Screen.Mapa.route) {
                    MapaScreenWithViewModel(
                        onNavigateToDetalle = { cotoId ->
                            navController.navigate("detalle_coto/$cotoId")
                        },
                        onNavigateToConsultaPunto = { latitude, longitude, source, accuracy ->
                            val accuracyArgument = accuracy?.toString() ?: "none"
                            navController.navigate(
                                "consulta_punto/$latitude/$longitude/$source/$accuracyArgument"
                            )
                        }
                    )
                }

                composable(Screen.Setas.route) {
                    SetasScreen()
                }
            
            composable(Screen.Configuracion.route) {
                ConfiguracionScreen()
            }
            
            composable("detalle_coto/{cotoId}") { backStackEntry ->
                val cotoId = backStackEntry.arguments?.getString("cotoId")?.toIntOrNull() ?: 0
                DetalleCotoScreen(
                    cotoId = cotoId,
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }

            composable(
                route = "consulta_punto/{latitude}/{longitude}/{source}/{accuracy}",
                arguments = listOf(
                    navArgument("latitude") { type = NavType.StringType },
                    navArgument("longitude") { type = NavType.StringType },
                    navArgument("source") { type = NavType.StringType },
                    navArgument("accuracy") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val latitude = backStackEntry.arguments?.getString("latitude")?.toDoubleOrNull()
                val longitude = backStackEntry.arguments?.getString("longitude")?.toDoubleOrNull()
                val source = backStackEntry.arguments?.getString("source") ?: "MAP"
                val accuracy = backStackEntry.arguments?.getString("accuracy")
                    ?.takeUnless { it == "none" }
                    ?.toFloatOrNull()

                if (latitude != null && longitude != null) {
                    ConsultaPuntoScreen(
                        latitude = latitude,
                        longitude = longitude,
                        source = source,
                        accuracyMeters = accuracy,
                        onNavigateBack = { navController.popBackStack() }
                    )
                } else {
                    DetalleCotoErrorScreen(
                        error = "Las coordenadas seleccionadas no son válidas.",
                        onRetry = { navController.popBackStack() }
                    )
                }
            }
            
            composable("add_coto") {
                AddCotoScreen(
                    onNavigateBack = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
