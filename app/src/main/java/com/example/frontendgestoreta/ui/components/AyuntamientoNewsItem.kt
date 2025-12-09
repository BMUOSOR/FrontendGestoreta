package com.example.frontendgestoreta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontendgestoreta.data.models.EventDTO
import java.time.format.DateTimeFormatter
import com.example.frontendgestoreta.R

@Composable
fun AyuntamientoNewsItem(
    event: EventDTO,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val cardBackgroundColor = Color(0xFF1A1C20)
    val textColor = Color.White
    val accentColor = Color(0xFFA0A0FF)

    Card(
        modifier = modifier
            .width(280.dp)
            .clickable { onClick() }
            .height(380.dp),

        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp), // Padding general unificado
        ) {

            // --- BLOQUE SUPERIOR (Iconos + Fecha + Título) ---

            // 1. CABECERA GRÁFICA
            // Quitamos el Box con height(60.dp) fijo para eliminar hueco sobrante
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp) // Espacio controlado hacia la fecha
            ) {
                repeat(3) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_ayuntamento_event),
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(36.dp) // Iconos ligeramente más compactos
                    )
                }
            }

            // 2. AUTOR Y FECHA
            Text(
                text = "Ayuntamiento de Valencia",
                color = textColor,
                fontSize = 12.sp,
                lineHeight = 14.sp
            )
            Text(
                text = event.fecha?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: "Fecha pte.",
                color = textColor.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Light
            )

            Spacer(modifier = Modifier.height(12.dp)) // Espacio justo entre fecha y título

            // 3. TÍTULO GRANDE
            Text(
                text = event.titulo ?: "Noticia oficial",
                color = textColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 24.sp, // LineHeight ajustado para que el texto sea compacto
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))

            Spacer(modifier = Modifier.weight(1f))

            // --- IMAGEN ASOCIADA AL EVENTO ---
            val drawableId = event.imagen?.toIntOrNull()

            if (drawableId != null) {
                Image(
                    painter = painterResource(id = drawableId),
                    contentDescription = "Imagen del evento",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .width(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            // 4. PIE DE TARJETA
            Column {
                HorizontalDivider(
                    modifier = Modifier.padding(bottom = 12.dp), // Menos espacio alrededor de la línea
                    thickness = 1.dp,
                    color = Color.Gray.copy(alpha = 0.5f)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = event.tag?.name?.lowercase() ?: "general",
                        color = textColor.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )

                    Text(
                        text = "oficial",
                        color = textColor.copy(alpha = 0.7f),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

// (La función GraphicIcon ya no se usa, la puedes borrar si quieres)