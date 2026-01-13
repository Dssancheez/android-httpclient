package es.fpsumma.dam2.api.ui.screen.tareas

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import es.fpsumma.dam2.api.model.Tarea

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleTareaContent(
    tarea: Tarea?,
    onBack: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(tarea) {
        tarea?.let {
            titulo = it.titulo
            descripcion = it.descripcion
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Tarea") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        if (tarea == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator()
                    Text("Cargando...", modifier = Modifier.padding(top = 8.dp))
                }
            }
        } else {
            Column(modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize()) {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = { onSave(titulo, descripcion) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Actualizar Tarea")
                }
            }
        }
    }
}
