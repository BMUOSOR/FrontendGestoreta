package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.ui.components.HtmlTextContainer
import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.time.ZoneId
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import org.json.JSONObject

//Fuente Raleway
val RalewayFont = FontFamily(
    Font(R.font.raleway_light, FontWeight.Normal),
    Font(R.font.raleway_semibold, FontWeight.Bold)
)

@Composable
fun NewsDetailScreen(
    event: EventDTO,
    nombrePublicador: String,
    relatedEvents: List<EventDTO> = emptyList(),
    onInscribirseClick: () -> Unit,
    onBack: () -> Unit,
    onRelatedEventClick: (EventDTO) -> Unit
) {
    val context = LocalContext.current
    val headerHeight = 400.dp

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // --- IMAGEN DE CABECERA ---
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(event.imagen)
                .crossfade(true)
                .size(1000)
                .build(),
            placeholder = painterResource(id = R.drawable.img_evento_default),
            error = painterResource(id = R.drawable.img_evento_default),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .align(Alignment.TopCenter)
        )

        // --- BOTÓN FLOAT SOBRE LA IMAGEN ---
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 340.dp, end = 24.dp)
                .zIndex(2f)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 6.dp,
                modifier = Modifier.size(48.dp)
            ) {
                IconButton(onClick = { agregarAlCalendario(context, event) }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_calendar),
                        contentDescription = "Guardar",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // --- CONTENIDO SCROLLABLE ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 40.dp)
        ) {

            // Espacio inicial para que el contenido empiece debajo de la imagen
            item {
                Spacer(modifier = Modifier.height(headerHeight - 40.dp))
            }

            // CONTENEDOR BLANCO
            item {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                    color = Color.White
                ) {

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 32.dp)
                    ) {

                        // --- FILA SUPERIOR ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = event.tag.toString().uppercase(),
                                style = TextStyle(
                                    fontFamily = RalewayFont,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            )

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Publicado por",
                                    style = TextStyle(
                                        fontFamily = RalewayFont,
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                )
                                Text(
                                    text = nombrePublicador,
                                    style = TextStyle(
                                        fontFamily = RalewayFont,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 11.sp,
                                        color = Color.Black
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // --- TÍTULO ---
                        Text(
                            text = event.titulo ?: "",
                            style = TextStyle(
                                fontFamily = RalewayFont,
                                fontWeight = FontWeight.Bold,
                                fontSize = 26.sp,
                                lineHeight = 32.sp,
                                color = Color(0xFF1C1C1C)
                            )
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- CUERPO HTML ---
                        HtmlTextContainer(text = event.descripcion.orEmpty()) { annotatedText ->
                            Text(
                                text = annotatedText,
                                style = TextStyle(
                                    fontFamily = RalewayFont,
                                    fontSize = 16.sp,
                                    lineHeight = 24.sp,
                                    color = Color.DarkGray
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // --- BOTÓN INSCRIBIRSE ---
                        if (event.maxPersonas != 0L) {
                            Button(
                                onClick = onInscribirseClick,
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text("Inscribirse al evento", fontFamily = RalewayFont)
                            }
                        }

                        Spacer(modifier = Modifier.height(48.dp))

                        // --- SECCIÓN NOTICIAS RELACIONADAS ---
                        RelatedNewsSection(
                            events = relatedEvents,
                            onClick = onRelatedEventClick
                        )
                    }
                }
            }
        }

        // --- TOP BAR FIJA ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.Black
                )
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun RelatedNewsSection(
    events: List<EventDTO>,
    onClick: (EventDTO) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Noticias Relacionadas",
                style = TextStyle(
                    fontFamily = RalewayFont,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF333333)
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            HorizontalDivider(thickness = 1.dp, color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(16.dp))

        val list = if (events.isNotEmpty()) events else listOf(
            EventDTO(titulo = "València celebrará un espectáculo de fuegos artificiales navideño este año"),
            EventDTO(titulo = "El ayuntamiento avisa de cortes de tráfico temporales por pasacalles"),
            EventDTO(titulo = "Los artistas falleros empiezan a presentar sus bocetos para las próximas fallas")
        )

        list.forEach { e ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable { onClick(e) }
            ) {
                Text(
                    text = e.titulo ?: "",
                    style = TextStyle(
                        fontFamily = RalewayFont,
                        fontSize = 15.sp,
                        color = Color.Black
                    )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Saber más",
                    style = TextStyle(
                        fontFamily = RalewayFont,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.Underline
                    )
                )
            }
        }
    }
}

fun agregarAlCalendario(context: Context, event: EventDTO) {
    val ubicacionLimpia = try {
        val raw = event.ubicacion
        if (!raw.isNullOrBlank() && raw.trim().startsWith("{")) {
            val json = JSONObject(raw)
            val direccion = json.optString("direccion", "")
            val ciudad = json.optString("ciudad", "")
            listOf(direccion, ciudad).filter { it.isNotBlank() }.joinToString(", ")
        } else raw ?: "Valencia"
    } catch (e: Exception) {
        event.ubicacion ?: "Valencia"
    }

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, event.titulo ?: "Evento Fallero")
        putExtra(CalendarContract.Events.DESCRIPTION, event.descripcion ?: "")
        putExtra(CalendarContract.Events.EVENT_LOCATION, ubicacionLimpia)

        event.fecha?.let { date ->
            val startMillis = date.atStartOfDay(ZoneId.systemDefault())
                .toInstant().toEpochMilli()

            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
        }
    }

    context.startActivity(intent)
}
