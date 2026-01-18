package es.fpsumma.dam2.api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.fpsumma.dam2.api.data.remote.RetrofitClient
import es.fpsumma.dam2.api.model.Tarea
import es.fpsumma.dam2.api.ui.screen.tareas.TareasUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel "remoto": obtiene datos desde una API (Retrofit) y expone un estado
 * que la UI (Compose) puede observar.
 *
 * La UI NO llama a Retrofit directamente: solo observa `state`.
 */
class TareasRemoteViewModel : ViewModel() {

    fun deleteTarea(id: Int) = viewModelScope.launch {
        try {
            val response = RetrofitClient.tareaAPI.deleteTarea(id)

            if (response.isSuccessful) {
                loadTareas()
            }
        } catch (e: Exception) {
        }
    }

    // Cliente Retrofit ya configurado (baseUrl + converter).
    // Aquí obtenemos la interfaz con los endpoints (GET/POST/PUT/DELETE, etc.)
    private val api = RetrofitClient.tareaAPI

    /**
     * Estado interno (mutable) del ViewModel.
     * Lo mantenemos privado para que SOLO el ViewModel pueda modificarlo.
     */
    private val _state = MutableStateFlow(TareasUIState())

    /**
     * Estado público (solo lectura). La UI se suscribe a este StateFlow:
     * - con collectAsState() en Compose
     * - o con collect en otras capas
     */
    val state: StateFlow<TareasUIState> = _state

    // Variable privada mutable para guardar la tarea individual
    private val _selected = MutableStateFlow<Tarea?>(null)

    // Variable pública inmutable que observará la UI
    val selected: StateFlow<Tarea?> = _selected

    /**
     * Carga el listado de tareas desde la API.
     * - Pone loading=true
     * - Llama al endpoint
     * - Si va bien: convierte DTO -> modelo de app (Tarea) y lo guarda en el estado
     * - Si falla: guarda el error en el estado
     */
    fun loadTareas() = viewModelScope.launch {
        // 1) Antes de llamar a la red, avisamos a la UI de que estamos cargando.
        //    También limpiamos errores previos.
        _state.update { current ->
            current.copy(loading = true, error = null)
        }

        /**
         * runCatching hace de "try/catch" funcional:
         * - Lo que devuelva el bloque irá a onSuccess(...)
         * - Si el bloque lanza una excepción, irá a onFailure(...)
         */
        runCatching {
            // 2) Llamada HTTP (suspend) al endpoint: GET /api/tareas
            val res = api.listar()

            // 3) Si el HTTP no es 2xx, lo convertimos en excepción para ir a onFailure(...)
            if (!res.isSuccessful) error("HTTP ${res.code()}")

            // 4) Si el body es null, devolvemos lista vacía (para evitar NPE)
            res.body() ?: emptyList()
        }.onSuccess { listaDto ->
            // 5) Si todo salió bien, aquí tenemos la lista de DTOs que viene de la API.
            //    Convertimos DTO -> Modelo de la app.
            //    (La UI trabaja con Tarea, no con TareaDTO)
            val tareas = listaDto.map { dto ->
                Tarea(
                    id = dto.id,
                    titulo = dto.titulo,
                    descripcion = dto.descripcion
                )
            }

            // 6) Actualizamos el estado:
            //    - tareas con la lista convertida
            //    - loading=false porque ya acabó la carga
            _state.update { current ->
                current.copy(tareas = tareas, loading = false)
            }
        }.onFailure { e ->
            // 7) Si ha fallado (sin internet, timeout, 500, etc.), guardamos un mensaje de error
            //    y paramos el loading.
            _state.update { current ->
                current.copy(
                    error = e.message ?: "Error cargando tareas",
                    loading = false
                )
            }
        }
    }



    fun loadTarea(id: Int) = viewModelScope.launch {
        runCatching {
            val res = api.detalle(id)
            if (!res.isSuccessful) error("HTTP ${res.code()}")
            res.body() ?: error("Sin body")
        }.onSuccess { dto ->
            // Convertimos el DTO a Tarea y lo guardamos en _selected
            _selected.value = Tarea(dto.id, dto.titulo, dto.descripcion)
        }.onFailure { e ->
            _state.update { it.copy(error = e.message ?: "Error cargando detalle") }
        }
    }


    fun addTarea(titulo: String, descripcion: String, onResult: (Boolean) -> Unit) = viewModelScope.launch {
        runCatching {
            val request = es.fpsumma.dam2.api.data.remote.dto.TareaCreateRequestDTO(titulo, descripcion)

            val res = api.crear(request) // Llama al método POST de tu API
            if (!res.isSuccessful) error("Error creando tarea")
            res.body()
        }.onSuccess {
            // Si sale bien, recargamos la lista para ver la nueva tarea
            loadTareas()
            // Avisamos a la pantalla de que ha ido bien (true) para que vuelva atrás
            onResult(true)
        }.onFailure { e ->
            // Si falla, guardamos el error y avisamos (false)
            _state.update { it.copy(error = e.message ?: "Error al crear") }
            onResult(false)
        }
    }


    fun updateTarea(id: Int, titulo: String, descripcion: String, onResult: (Boolean) -> Unit) = viewModelScope.launch {
        runCatching {
            val request = es.fpsumma.dam2.api.data.remote.dto.TareaUpdateRequestDTO(titulo, descripcion)

            val res = api.actualizar(id, request) // Llama al método PUT de tu API
            if (!res.isSuccessful) error("Error actualizando")
            res.body()
        }.onSuccess {
            loadTareas() // Recargamos la lista
            onResult(true)
        }.onFailure { e ->
            _state.update { it.copy(error = e.message ?: "Error al actualizar") }
            onResult(false)
        }
    }

}