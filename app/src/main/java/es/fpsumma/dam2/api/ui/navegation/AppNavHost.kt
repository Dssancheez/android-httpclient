package es.fpsumma.dam2.api.ui.navegation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasRemoteRoute
// Importamos las nuevas rutas en lugar de las Screens antiguas
import es.fpsumma.dam2.api.ui.screen.tareas.DetalleTareaRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.ListadoTareasRoomRoute
import es.fpsumma.dam2.api.ui.screen.tareas.NuevaTareaRoomRoute
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun AppNavHost(navController: NavHostController, tareasViewModel: TareasViewModel, tareasRemoteViewModel: TareasRemoteViewModel) {
    NavHost(
        navController = navController,
        startDestination = Routes.TAREA_LISTADO
    ) {
        composable(Routes.TAREA_LISTADO) {
            ListadoTareasRoomRoute(navController, tareasViewModel)
        }

        composable(Routes.TAREA_LISTADO) {
            ListadoTareasRemoteRoute(navController, tareasRemoteViewModel)
        }

        composable(Routes.TAREA_ADD) {
            NuevaTareaRoomRoute(navController, tareasViewModel)
        }

        composable(
            route = Routes.TAREA_VIEW,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            DetalleTareaRoomRoute(
                id = id,
                navController = navController,
                vm = tareasViewModel
            )
        }
    }
}