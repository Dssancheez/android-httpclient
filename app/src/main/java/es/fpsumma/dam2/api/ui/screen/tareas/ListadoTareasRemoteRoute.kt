package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import es.fpsumma.dam2.api.ui.navegation.Routes
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@Composable
fun ListadoTareasRemoteRoute(
    navController: NavController,
    vm: TareasRemoteViewModel,
    modifier: Modifier = Modifier,
) {
    val state by vm.state.collectAsState()

    LaunchedEffect(Unit) {
        vm.loadTareas()
    }

    ListadoTareasContent(
        state = state,
        onBack = { navController.popBackStack() },
        onAdd = { navController.navigate(Routes.TAREA_ADD) },
        onOpenDetalle = { id ->
            navController.navigate(Routes.tareaView(id))
        },
        onDelete = {  },
        modifier = modifier
    )
}