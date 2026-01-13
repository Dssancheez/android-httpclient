package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun NuevaTareaRoomRoute(
    navController: NavController,
    vm: TareasViewModel
) {
    NuevaTareaContent(
        onBack = { navController.popBackStack() }, // Vuelve atrás
        onSave = { titulo, desc ->
            vm.addTarea(titulo, desc) // Guarda en la base de datos
            navController.popBackStack() // Vuelve al listado
        }
    )
}
