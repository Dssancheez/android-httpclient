package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.runtime.Composable
import es.fpsumma.dam2.api.viewmodel.TareasRemoteViewModel

@Composable
fun NuevaTareaRemoteRoute(
    vm: TareasRemoteViewModel,
    onBack: () -> Unit
) {
    NuevaTareaContent(
        onBack = onBack,
        onSave = { titulo, descripcion ->
            vm.addTarea(titulo, descripcion) { exito ->
                if (exito) onBack()
            }
        }
    )
}