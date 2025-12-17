package com.example.frontendgestoreta.ui.screens_gestor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.i18n.DateTimeFormatter
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.GestorDTO
import com.example.frontendgestoreta.viewModel.EventViewModel
import java.time.LocalDate
import coil.compose.rememberAsyncImagePainter
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import com.yalantis.ucrop.UCrop
import java.io.File
import java.io.FileOutputStream
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.frontendgestoreta.data.models.Tag
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditEventScreen(
    onBack: () -> Unit,
    viewModel: EventViewModel = viewModel(),
    userGestor: GestorDTO,
    event: EventDTO
) {
    var titulo by remember { mutableStateOf(""+event.titulo) }
    var tag: Tag? by remember { mutableStateOf(event.tag) }
    var descripcion by remember { mutableStateOf(""+event.descripcion) }
    var ubicacion by remember { mutableStateOf(""+event.ubicacion) }
    var maxPersonas by remember { mutableStateOf(""+event.maxPersonas) }
    var fecha by remember { mutableStateOf(event.fecha) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    var croppedImageUri by remember { mutableStateOf<Uri?>(Uri.parse(event.imagen!!)) }
    val context = LocalContext.current

    val cropLauncher = rememberLauncherForActivityResult(
        contract = object : ActivityResultContract<UCrop, Uri?>() {
            override fun createIntent(context: Context, input: UCrop): Intent {
                return input.getIntent(context)
            }
            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return if (resultCode == Activity.RESULT_OK && intent != null) {
                    UCrop.getOutput(intent)
                } else null
            }
        }
    ) { uri -> if (uri != null) croppedImageUri = uri }

    // Launcher para seleccionar imagen
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val destination = Uri.fromFile(
                File(context.cacheDir, "cropped_${System.currentTimeMillis()}.jpg")
            )

            val cropIntent = UCrop.of(uri, destination)
                .withAspectRatio(4f, 3f)
                .withOptions(
                    UCrop.Options().apply {
                        setToolbarTitle("Recortar imagen")
                        setToolbarColor(ContextCompat.getColor(context, R.color.white))
                        setStatusBarColor(ContextCompat.getColor(context, R.color.white))
                        setActiveControlsWidgetColor(ContextCompat.getColor(context, R.color.black))
                        setToolbarWidgetColor(ContextCompat.getColor(context, R.color.black))
                        setHideBottomControls(false)
                        setFreeStyleCropEnabled(false)
                        setCompressionQuality(90)
                    }
                )

            cropLauncher.launch(cropIntent)
        }
    }


    Scaffold(
        containerColor = colorResource(R.color.white), // Fondo blanco explícito
        topBar = {
            TopAppBar(
                title = { Text("Editar Evento",
                    fontWeight = FontWeight.Bold,
                    color = colorResource(R.color.black))
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(ImageVector.vectorResource(R.drawable.ic_arrow_back), contentDescription = "Volver",tint = colorResource(R.color.black))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.white) // fondo blanco
                )
            )
        },

    ) { innerPadding ->
        LazyColumn(

            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text("Imagen del evento", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp)
                        .border(2.dp, colorResource(R.color.black))
                        .clickable { galleryLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (croppedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(croppedImageUri),
                            contentDescription = "Imagen del evento",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Text("Seleccionar imagen", color = colorResource(R.color.black))
                    }
                }
            }

            item {
                Text("Título", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, colorResource(R.color.black), OutlinedTextFieldDefaults.shape),
                    placeholder = { Text("Ingresa el título del evento") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.black),
                        unfocusedTextColor = colorResource(R.color.black),
                        cursorColor = colorResource(R.color.black),
                        focusedBorderColor = colorResource(R.color.black),
                        unfocusedBorderColor = colorResource(R.color.black)
                    )
                )
            }

            item {
                Text("Categoría", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))

                var expanded by remember { mutableStateOf(false) }
                var selectedTag by remember { mutableStateOf(Tag.entries.first()) }
                tag = selectedTag

                // ExposedDropdownMenuBox requiere Material3
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = selectedTag?.name ?: "Selecciona una categoría",
                        onValueChange = {},
                        readOnly = true, // importante: solo lectura
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expanded = true }, // abrir menú al clickear
                        label = { Text("Categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = colorResource(R.color.black),
                            unfocusedTextColor = colorResource(R.color.black),
                            cursorColor = colorResource(R.color.black),
                            focusedBorderColor = colorResource(R.color.black),
                            unfocusedBorderColor = colorResource(R.color.black)
                        )
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        Tag.values().forEach { tagOption ->
                            DropdownMenuItem(
                                text = { Text(tagOption.name) },
                                onClick = {
                                    selectedTag = tagOption
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))
            }
            item {
                Text("Descripción", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .border(2.dp, colorResource(R.color.black), OutlinedTextFieldDefaults.shape),
                    placeholder = { Text("Describe el evento") },
                    singleLine = false,
                    maxLines = 4,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.black),
                        unfocusedTextColor = colorResource(R.color.black),
                        cursorColor = colorResource(R.color.black),
                        focusedBorderColor = colorResource(R.color.black),
                        unfocusedBorderColor = colorResource(R.color.black)
                    )
                )
            }

            item {
                Text("Ubicación", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = ubicacion,
                    onValueChange = { ubicacion = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, colorResource(R.color.black), OutlinedTextFieldDefaults.shape),
                    placeholder = { Text("¿Dónde será el evento?") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.black),
                        unfocusedTextColor = colorResource(R.color.black),
                        cursorColor = colorResource(R.color.black),
                        focusedBorderColor = colorResource(R.color.black),
                        unfocusedBorderColor = colorResource(R.color.black)
                    )
                )
            }

            item {
                Text("Fecha", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = fecha!!.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")), // Mostrar la fecha formateada
                    onValueChange = { /* Solo lectura */ },
                    readOnly = true, // Es de solo lectura
                    modifier = Modifier
                        .fillMaxWidth()
                        // Aplica el mismo estilo de borde que los demás campos
                        .border(2.dp, colorResource(R.color.black), OutlinedTextFieldDefaults.shape)
                        // Añade el clic para abrir el selector
                        .clickable { showDatePickerDialog = true },
                    label = { Text("Fecha del evento") },
                    // Aplica los mismos colores negros y estilos de borde
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.black),
                        unfocusedTextColor = colorResource(R.color.black),
                        cursorColor = colorResource(R.color.black),
                        focusedBorderColor = colorResource(R.color.black),
                        unfocusedBorderColor = colorResource(R.color.black)
                    ),
                    trailingIcon = {
                        // Icono para indicar que se puede abrir el selector
                        Icon(
                            ImageVector.vectorResource(R.drawable.ic_calendar), // Asumiendo que tienes un icono de calendario
                            contentDescription = "Seleccionar fecha",
                            tint = colorResource(R.color.black)
                        )
                    }
                )

                if (showDatePickerDialog) {
                    val datePickerState = rememberDatePickerState()

                    DatePickerDialog(
                        onDismissRequest = { showDatePickerDialog = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    val selectedDateMillis = datePickerState.selectedDateMillis
                                    if (selectedDateMillis != null) {
                                        fecha = LocalDate.ofEpochDay(selectedDateMillis / (1000 * 60 * 60 * 24))
                                    }
                                    showDatePickerDialog = false
                                }
                            ) { Text("Aceptar") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePickerDialog = false }) { Text("Cancelar") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
            }

            item {
                Text("Máximo de Personas", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(Modifier.height(4.dp))
                OutlinedTextField(
                    value = maxPersonas,
                    onValueChange = {
                        if (it.all { c -> c.isDigit() } || it.isEmpty()) maxPersonas = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(2.dp, colorResource(R.color.black), OutlinedTextFieldDefaults.shape),
                    placeholder = { Text("Ej: 50, 100, 200") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = colorResource(R.color.black),
                        unfocusedTextColor = colorResource(R.color.black),
                        cursorColor = colorResource(R.color.black),
                        focusedBorderColor = colorResource(R.color.black),
                        unfocusedBorderColor = colorResource(R.color.black)
                    )
                )
            }

            item {
                val isEnabled = titulo.isNotBlank() && descripcion.isNotBlank() && ubicacion.isNotBlank()
                        && fecha != null && maxPersonas.isNotBlank()

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = { onBack() }) { Text("Cancelar") }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val nuevoEvento = EventDTO(
                                idEvento = event.idEvento,
                                titulo = titulo,
                                descripcion = descripcion,
                                ubicacion = ubicacion,
                                idFalla = userGestor.falla,
                                fecha = fecha,
                                maxPersonas = maxPersonas.toLongOrNull(),
                                tag = tag,
                                imagen = null
                            )
                            viewModel.updateEvent(nuevoEvento) { createdEvent ->
                                if (createdEvent != null) {
                                    viewModel.viewModelScope.launch(Dispatchers.IO) {
                                        val tempFile = if (croppedImageUri.toString().startsWith("http")) {
                                            urlToTempFile(context, croppedImageUri.toString())
                                        } else {
                                            uriToTempFile(context, croppedImageUri!!)
                                        }

                                        if (tempFile != null) {
                                            viewModel.uploadImagenEvento(
                                                createdEvent.idEvento!!,
                                                Uri.fromFile(tempFile),
                                                tempFile.name
                                            ) { createdImageName ->
                                                if (createdImageName != null) {
                                                    Log.d("EditEvent", "Imagen subida con nombre: $createdImageName")
                                                    createdEvent.imagen = createdImageName
                                                    viewModel.updateEvent(createdEvent) { updatedEvent ->
                                                        if (updatedEvent != null) {
                                                            Log.d("EditEvent", "Evento actualizado con ID: ${updatedEvent.idEvento}")
                                                        }
                                                    }
                                                } else {
                                                    Log.e("EditEvent", "Error subiendo imagen")
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    Log.e("EditEvent", "Error creando evento")
                                }
                            }

                            Log.d("EditEventScreen", "Evento editado: " + nuevoEvento.titulo)
                            onBack()
                        },
                        enabled = isEnabled,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.black),           // color normal
                            contentColor = colorResource(R.color.white),             // texto normal
                            disabledContainerColor = colorResource(R.color.gray_light), // color cuando está deshabilitado
                            disabledContentColor = colorResource(R.color.white)     // texto cuando está deshabilitado
                        )
                    ) {
                        Text("Editar Evento")
                    }
                }
            }
        }
    }
}
fun uriToTempFile(context: Context, uri: Uri): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri) ?: return null
        val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        FileOutputStream(tempFile).use { output ->
            inputStream.copyTo(output)
        }
        tempFile
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}



suspend fun urlToTempFile(context: Context, urlString: String): File? {
    return withContext(Dispatchers.IO) {
        try {
            val url = java.net.URL(urlString)
            val connection = url.openConnection()
            connection.connect()
            val inputStream = connection.getInputStream()
            val tempFile = File(context.cacheDir, "upload_${System.currentTimeMillis()}.jpg")
            FileOutputStream(tempFile).use { output ->
                inputStream.copyTo(output)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

