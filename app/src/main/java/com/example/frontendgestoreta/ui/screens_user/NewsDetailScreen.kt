package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import org.json.JSONObject

//Fuente Raleway
val RalewayFont = FontFamily(
    Font(R.font.raleway_light, FontWeight.Normal), // Asegúrate de que este recurso exista o usa R.font.tu_fuente
    Font(R.font.raleway_semibold, FontWeight.Bold)
)

@Composable
fun NewsDetailScreen(
    event: EventDTO,
    nombrePublicador: String,
    relatedEvents: List<EventDTO> = emptyList(), // Lista para noticias relacionadas
    onInscribirseClick: () -> Unit,
    onBack: () -> Unit,
    onRelatedEventClick: (EventDTO) -> Unit // Click en noticia relacionada
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val headerHeight = 400.dp //altura imagen cabecera

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        // --- 1. IMAGEN DE FONDO
        Image(
            painter = painterResource(id = R.drawable.img_evento_default), // Tu imagen
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
                .align(Alignment.TopCenter)
        )

        // Botón flotante sobre la imagen (Icono calendario/guardar como en la foto)
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 340.dp, end = 24.dp) // Posicionado justo donde empieza la curva blanca
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
                        painter = painterResource(id = R.drawable.ic_calendar), // Icono calendario
                        contentDescription = "Guardar",
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        // --- 2. CONTENIDO SCROLLABLE
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {

            Spacer(modifier = Modifier.height(headerHeight - 40.dp))

            // Contenedor Blanco con esquinas redondeadas
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = Color.White,
                shadowElevation = 0.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                ) {

                    // Fila superior: Categoría y Autor
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tag
                        Text(
                            text = "PROCLAMACIÓN FM 2026", // O event.categoria
                            style = TextStyle(
                                fontFamily = RalewayFont,
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        )

                        // Publicado por (Derecha)
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Publicado por",
                                style = TextStyle(
                                    fontFamily = RalewayFont,
                                    fontSize = 10.sp,
                                    color = Color.Gray
                                )
                            )
                            val idFalla = event.idFalla;
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

                    // TÍTULO GRANDE
                    Text(
                        text = event.titulo ?: "Anunciada la presentación oficial de las Falleras Mayores",
                        style = TextStyle(
                            fontFamily = RalewayFont,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            lineHeight = 32.sp,
                            color = Color(0xFF1C1C1C)
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // CUERPO DE LA NOTICIA
                    HtmlTextContainer(text = event.descripcion.orEmpty()) { annotatedText ->
                        Text(
                            text = annotatedText,
                            style = TextStyle(
                                fontFamily = RalewayFont,
                                fontWeight = FontWeight.Normal,
                                fontSize = 16.sp,
                                lineHeight = 24.sp,
                                color = Color.DarkGray
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // BOTÓN INSCRIBIRSE
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
                    RelatedNewsSection(events = relatedEvents, onClick = onRelatedEventClick)

                    // Espacio extra al final para scroll cómodo
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }

        // --- 3. BARRA SUPERIOR DE NAVEGACIÓN (FIXED) ---
        // Icono Back y Logo. Se quedan fijos arriba mientras haces scroll.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 48.dp, start = 24.dp, end = 24.dp), // Ajustar según insets del sistema
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Botón Atrás
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.8f), CircleShape) // Fondo semitransparente para ver la flecha
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.Black
                )
            }

            // Logo de la marca (opcional, como en la foto)
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground), // Tu logo aquí
                contentDescription = "Logo",
                tint = Color.Unspecified, // Para mantener colores originales si es SVG/PNG
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

// --- COMPONENTE NOTICIAS RELACIONADAS ---
@Composable
fun RelatedNewsSection(
    events: List<EventDTO>,
    onClick: (EventDTO) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {

        // Cabecera con línea inferior
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
            // Línea divisoria gris oscura/negra
            HorizontalDivider(
                thickness = 1.dp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Si no hay eventos, mostramos datos dummy para visualizar el diseño como la foto
        val displayList = if(events.isNotEmpty()) events else listOf(
            EventDTO(titulo = "València celebrará un espectáculo de fuegos artificiales navideño este año"),
            EventDTO(titulo = "El ayuntamiento avisa de cortes de tráfico temporales por pasacalles"),
            EventDTO(titulo = "Los artistas falleros empiezan a presentar sus bocetos para las próximas fallas")
        )

        displayList.forEach { relatedEvent ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .clickable { onClick(relatedEvent) }
            ) {
                Text(
                    text = relatedEvent.titulo ?: "",
                    style = TextStyle(
                        fontFamily = RalewayFont,
                        fontWeight = FontWeight.Normal,
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
                        textDecoration = TextDecoration.Underline // Subrayado como en la foto
                    )
                )
            }
        }
    }
}

fun agregarAlCalendario(context: Context, event: EventDTO) {
    val ubicacionLimpia = try {
        val rawUbicacion = event.ubicacion

        // Verificamos si parece un JSON (empieza por llave '{')
        if (!rawUbicacion.isNullOrBlank() && rawUbicacion.trim().startsWith("{")) {
            val jsonObj = JSONObject(rawUbicacion)

            // Extraemos los campos con seguridad (si no existen, devuelve cadena vacía)
            val direccion = jsonObj.optString("direccion", "")
            val ciudad = jsonObj.optString("ciudad", "")

            // Unimos las partes que tengan texto con una coma
            listOf(direccion, ciudad)
                .filter { it.isNotBlank() } // Filtra los vacíos
                .joinToString(", ") // Une con coma y espacio

        } else {
            // Si no es JSON o es nulo, usamos el valor original o un default
            rawUbicacion ?: "Valencia"
        }
    } catch (e: Exception) {
        // Si falla al leer el JSON, mostramos el original para no romper la app
        event.ubicacion ?: "Valencia"
    }
    //Creamos INTENT
    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI

        putExtra(CalendarContract.Events.TITLE, event.titulo ?: "Evento Fallero")
        putExtra(CalendarContract.Events.DESCRIPTION, event.descripcion ?: "")

        putExtra(CalendarContract.Events.EVENT_LOCATION, ubicacionLimpia)

        // Gestionar la fecha
        event.fecha?.let { localDate ->
            val startMillis = localDate.atStartOfDay(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()

            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
            putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true)
        }
    }

    context.startActivity(intent)
}
