package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import es.fpsumma.dam2.api.model.Tarea
import es.fpsumma.dam2.api.viewmodel.TareasViewModel

@Composable
fun DetalleTareaRoomRoute(
    id: Int,
    navController: NavController,
    vm: TareasViewModel
) {
    val tareaEntity by vm.getTarea(id).collectAsState(initial = null)
    val tarea = tareaEntity?.let { Tarea(it.id, it.titulo, it.descripcion) }

    DetalleTareaContent(
        tarea = tarea,
        onBack = { navController.popBackStack() },
        onSave = { nuevoTit, nuevaDesc ->
            vm.updateTarea(id, nuevoTit, nuevaDesc)
            navController.popBackStack()
        }
    )
}