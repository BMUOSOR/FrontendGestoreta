package com.example.frontendgestoreta.ui.screens_gestor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.navigation.AppScreens
import com.google.gson.Gson // IMPORTANTE
import kotlinx.coroutines.delay
import java.net.URLEncoder // IMPORTANTE
import java.nio.charset.StandardCharsets // IMPORTANTE
import java.time.LocalDate
import java.time.OffsetDateTime
import android.util.Base64
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import coil.request.ImageRequest

@Composable
fun NewsGestorListItem(
    event: EventDTO,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Función para manejar la navegación segura


    // Diseño vertical: Columna (Imagen -> Título -> Saber Más)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick() } // Toda la tarjeta es clicable
    ) {
        // 1. Imagen redondeada
        val imageUrl = event.imagen
        val cleanUrl = imageUrl?.trim()?.replace(" ", "%20")

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(cleanUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Imagen del evento",
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp) // Altura fija como en la foto
                .clip(RoundedCornerShape(12.dp)), // Bordes redondeados de la imagen
            placeholder = painterResource(id = R.drawable.ic_newspaper), // Asegúrate de tener este recurso o usa uno default
            error = painterResource(id = R.drawable.ic_newspaper),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        // 2. Título
        Text(
            text = event.titulo ?: "Sin título",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium, // Semi-negrita
                fontSize = 13.sp,
                lineHeight = 18.sp
            ),
            color = Color.Black,
            maxLines = 3, // Máximo 3 líneas para que no rompa el grid
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

    }
}

// ... Resto del código (NewsListItemHeader, NewsListItemImage, etc.) se mantiene igual
@Composable
private fun NewsListItemHeader(event: EventDTO, showSummary: Boolean = true) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        NewsListItemImage(
            event = event,
            modifier = Modifier
                .size(64.dp)
                .clip(MaterialTheme.shapes.medium)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = event.titulo.orEmpty(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (showSummary) {
                Spacer(modifier = Modifier.height(4.dp))
                HtmlTextContainer(text = event.descripcion.orEmpty()) { text ->
                    Text(
                        text = text,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun NewsListItemImage(event: EventDTO, modifier: Modifier = Modifier) {
    val imageUrl = event.imagen
    val cleanUrl = imageUrl?.replace(" ", "%20")
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(event.imagen)
            .crossfade(true)
            .size(1000)
            .build(),
        contentDescription = "Imagen del evento",
        modifier = modifier,
        placeholder = painterResource(id = R.drawable.ic_newspaper),
        error = painterResource(id = R.drawable.ic_newspaper),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun HtmlTextContainer(text: String, content: @Composable (AnnotatedString) -> Unit) {
    val annotatedString = remember(text) { AnnotatedString.fromHtml(htmlString = text) }
    SelectionContainer { content(annotatedString) }
}

@Composable
private fun NewsListItemFooter(rotationAngle: Float, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow),
            contentDescription = "Mostrar más",
            modifier = Modifier
                .size(24.dp)
                .rotate(rotationAngle),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}