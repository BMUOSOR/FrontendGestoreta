package com.example.frontendgestoreta.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.fromHtml
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO
import java.time.LocalDate
import java.time.OffsetDateTime

@Composable
fun NewsListItem(
    event: EventDTO,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable { isExpanded = !isExpanded },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Header: título + resumen corto + imagen
            NewsListItemHeader(
                event = event,
                showSummary = !isExpanded
            )

            // Contenido expandido con animación suave
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HtmlTextContainer(text = event.descripcion.toString()) { annotatedText ->
                        Text(
                            text = annotatedText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Footer
            NewsListItemFooter(rotationAngle = rotationAngle)
        }
    }
}

@Composable
private fun NewsListItemHeader(
    event: EventDTO,
    showSummary: Boolean = true,
) {
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
                text = event.titulo.toString(),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )

            if (showSummary) {
                Spacer(modifier = Modifier.height(4.dp))
                HtmlTextContainer(text = event.descripcion.toString()) {
                    Text(
                        text = it,
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
private fun NewsListItemImage(
    event: EventDTO,
    modifier: Modifier = Modifier,
) {
    AsyncImage(
        model = event.maxPersonas, //esto tendrá que ser imageUrl
        contentDescription = "Imagen de la noticia",
        modifier = modifier,
        placeholder = painterResource(id = R.drawable.ic_newspaper),
        error = painterResource(id = R.drawable.ic_newspaper),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun HtmlTextContainer(text: String, content: @Composable (AnnotatedString) -> Unit) {
    val annotatedString = remember(text) {
        AnnotatedString.fromHtml(htmlString = text)
    }
    SelectionContainer { content(annotatedString) }
}

@Composable
private fun NewsListItemFooter(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
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

@Preview(showBackground = true)
@Composable
fun NewsListItemPreview() {
    val sampleEvent = EventDTO(
        idEvento = 1L,
        createdAt = OffsetDateTime.now(),
        fecha = LocalDate.now().plusDays(10),
        titulo = "Junta General Extraordinaria",
        descripcion = "Se convoca a todos los miembros a la junta general que tendrá lugar en el casal. Se tratarán temas importantes sobre los próximos eventos de las fallas de Valencia.",
        maxPersonas = 75L
    )
    NewsListItem(
        event = sampleEvent,
        modifier = Modifier.padding(16.dp)
    )
}
