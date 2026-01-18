package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@Composable
fun DetalleTareaRemoteRoute(
    id: Int,
    vm: TareasRemoteViewModel,
    onBack: () -> Unit
) {
    LaunchedEffect(id) {
        vm.loadTarea(id)
    }

    val tarea by vm.selected.collectAsState()

    if (tarea != null) {
        DetalleTareaContent(
            tarea = tarea!!,
            onBack = onBack,
            onSave = { titulo, descripcion ->
                vm.updateTarea(id, titulo, descripcion) { exito ->
                    if (exito) onBack()
                }
            },

        )
    }
}