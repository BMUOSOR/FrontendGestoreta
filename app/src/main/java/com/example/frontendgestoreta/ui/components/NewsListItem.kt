package com.example.frontendgestoreta.ui.components

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
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.OffsetDateTime

@Composable
fun NewsListItem(
    event: EventDTO,
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    var isExpanded by remember { mutableStateOf(false) }
    var tapCount by remember { mutableStateOf(0) }

    val rotationAngle by animateFloatAsState(targetValue = if (isExpanded) 180f else 0f)

    LaunchedEffect(tapCount) {
        if (tapCount > 0) {
            delay(500)
            if (tapCount == 1) {
                tapCount = 0
            }
        }
    }

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        tapCount++

                        when (tapCount) {
                            1 -> {
                                isExpanded = !isExpanded
                            }
                            2 -> {
                                val route = AppScreens.EventDetail.createRoute(event)
                                navController.navigate(route)
                                isExpanded = false
                                tapCount = 0
                            }
                        }
                    }
                )
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            NewsListItemHeader(event = event, showSummary = !isExpanded)

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    HtmlTextContainer(text = event.descripcion.orEmpty()) { annotatedText ->
                        Text(
                            text = annotatedText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            NewsListItemFooter(rotationAngle = rotationAngle)
        }
    }
}

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
    AsyncImage(
        model = null, // Cambiar por event.imageUrl cuando exista
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

// PREVIEW FUNCIONANDO
@Preview(showBackground = true)
@Composable
fun NewsListItemPreview() {
    val fakeNavController = rememberNavController()
    val sampleEvent = EventDTO(
        idEvento = 1L,
        createdAt = OffsetDateTime.now(),
        fecha = LocalDate.now().plusDays(10),
        titulo = "Junta General Extraordinaria",
        descripcion = "Se convoca a todos los miembros a la junta general...",
        maxPersonas = 75L
    )

    MaterialTheme {
        NewsListItem(
            event = sampleEvent,
            navController = fakeNavController,
            modifier = Modifier.padding(16.dp)
        )
    }
}
