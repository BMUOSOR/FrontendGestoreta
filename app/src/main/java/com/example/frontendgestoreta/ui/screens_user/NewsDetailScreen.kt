package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.ui.components.HtmlTextContainer
import java.time.format.DateTimeFormatter
import org.json.JSONObject

fun String.toMap(): Map<String, String>? {
    return try {
        JSONObject(this).let { json ->
            json.keys().asSequence().associateWith { json.getString(it) }
        }
    } catch (e: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsDetailScreen(
    event: EventDTO,
    onInscribirseClick: () -> Unit,
    onBack: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_arrow_back),
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // Card principal del evento
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {

                    // Título
                    Text(
                        text = event.titulo.orEmpty(),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            lineHeight = 30.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Divider(color = MaterialTheme.colorScheme.outlineVariant)

                    // Información del evento
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        event.fecha?.let {
                            InfoRow(label = "Fecha", value = it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), icon = "📅")
                        }
                        event.ubicacion?.toMap()?.let { ubicacionMap ->
                            val direccion = ubicacionMap["direccion"] ?: ""
                            val ciudad = ubicacionMap["ciudad"] ?: ""
                            val provincia = ubicacionMap["provincia"] ?: ""

                            val partes = listOfNotNull(
                                direccion.takeIf { it.isNotBlank() },
                                ciudad.takeIf { it.isNotBlank() },
                                provincia.takeIf { it.isNotBlank() }
                            )

                            if (partes.isNotEmpty()) {
                                val textoUbicacion = partes.joinToString(" - ") // o " • " o " - "

                                InfoRow(
                                    label = "Lugar",
                                    value = textoUbicacion,
                                    icon = "\uD83D\uDCCD"
                                )
                            }
                        }
                        event.maxPersonas?.let {
                            InfoRow(label = "Plazas máximas", value = "$it", icon = "👥")
                        }
                    }

                    Divider(color = MaterialTheme.colorScheme.outlineVariant)

                    // Descripción
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "Descripción",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold
                            ),
                            color = MaterialTheme.colorScheme.primary
                        )

                        HtmlTextContainer(text = event.descripcion.orEmpty()) { annotatedText ->
                            Text(
                                text = annotatedText,
                                style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 22.sp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Botón de inscripción
                    Button(
                        onClick = onInscribirseClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                    ) {
                        Text(
                            "Apuntarme al evento",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

// Subcomponente para líneas de información con icono
@Composable
fun InfoRow(label: String, value: String, icon: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Text(
            text = icon,
            fontSize = 18.sp
        )
        Text(
            text = "$label:",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
