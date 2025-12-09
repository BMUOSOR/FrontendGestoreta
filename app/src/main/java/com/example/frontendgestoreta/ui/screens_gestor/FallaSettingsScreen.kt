package com.example.frontendgestoreta.ui.screens_gestor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.data.models.FallaDTO
import com.example.frontendgestoreta.viewModel.FallaSettingsViewModel
import com.example.frontendgestoreta.viewModel.FallaSettingsUiState
import com.example.frontendgestoreta.viewModel.MemberViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FallaSettingsScreen(
    viewModel: FallaSettingsViewModel = viewModel()
) {
    // Recoge el estado del ViewModel
    val uiState by viewModel.uiState.collectAsState()

    // Muestra un Snackbar para el estado de guardado
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.saveSuccessful) {
        if (uiState.saveSuccessful) {
            snackbarHostState.showSnackbar("¡Falla actualizada con éxito!")
            viewModel.clearSaveStatus() // Limpiar el estado
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Ajustes de la Falla") })
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                uiState.isLoading && uiState.falla == null -> {
                    // Muestra un indicador de carga inicial
                    CircularProgressIndicator(Modifier.padding(24.dp))
                    Text("Cargando información...")
                }
                uiState.errorMessage != null -> {
                    // Muestra un mensaje de error
                    Text("Error: ${uiState.errorMessage}", color = MaterialTheme.colorScheme.error)
                }
                uiState.falla != null -> {
                    FallaDetailsForm(
                        falla = uiState.falla!!,
                        onFieldChange = { nombre, descripcion, direccion ->
                            viewModel.updateFallaField(
                                nombre = nombre,
                                descripcion = descripcion,
                                direccion = direccion
                            )
                        },
                        onSave = viewModel::saveFallaDetails,
                        isSaving = uiState.isLoading
                    )
                }
            }
        }
    }
}

@Composable
fun FallaDetailsForm(
    falla: FallaDTO,
    onFieldChange: (nombre: String, descripcion: String?, direccion: String?) -> Unit,
    onSave: () -> Unit,
    isSaving: Boolean
) {
    // Estados internos para la edición (mejor usar el estado del ViewModel directamente si es viable)
    // Para simplificar, aquí usamos el estado del ViewModel (falla) y lo pasamos al onChange

    // Nombre
    OutlinedTextField(
        value = falla.nombre,
        onValueChange = { onFieldChange(it, falla.descripcion, falla.direccion) },
        label = { Text("Nombre de la Falla") },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        enabled = !isSaving
    )

    // Descripción
    OutlinedTextField(
        value = falla.descripcion ?: "",
        onValueChange = { onFieldChange(falla.nombre, it, falla.direccion) },
        label = { Text("Descripción") },
        modifier = Modifier.fillMaxWidth().height(120.dp).padding(vertical = 8.dp),
        singleLine = false,
        enabled = !isSaving
    )

    // Dirección
    OutlinedTextField(
        value = falla.direccion ?: "",
        onValueChange = { onFieldChange(falla.nombre, falla.descripcion, it) },
        label = { Text("Dirección") },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        enabled = !isSaving
    )

    // Acta (asumiendo que es un campo de texto que el gestor puede editar)
    OutlinedTextField(
        value = falla.acta ?: "",
        onValueChange = { onFieldChange(falla.nombre, falla.descripcion, falla.direccion) },
        label = { Text("Acta (Resumen)") },
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        enabled = !isSaving
    )

    // Botón de Guardar
    Spacer(Modifier.height(16.dp))
    Button(
        onClick = onSave,
        enabled = !isSaving,
        modifier = Modifier.fillMaxWidth(0.6f).height(50.dp)
    ) {
        if (isSaving) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        } else {
            Text("Guardar Cambios")
        }
    }
}