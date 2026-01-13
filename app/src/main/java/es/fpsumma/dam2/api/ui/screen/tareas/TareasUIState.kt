package es.fpsumma.dam2.api.ui.screen.tareas

import es.fpsumma.dam2.api.model.Tarea

data class TareasUIState(
    val tareas: List<Tarea> = emptyList(), // listado de tareas
    val loading: Boolean = false, // representa si las tareas se están cargando o no
    val error: String? = null // Indica si ha ocurrido algún error que deba informarse al usuario
)