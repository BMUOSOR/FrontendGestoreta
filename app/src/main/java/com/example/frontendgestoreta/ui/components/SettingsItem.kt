package com.example.frontendgestoreta.ui.components

import com.example.frontendgestoreta.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsItem(
    modifier: Modifier = Modifier,
    leftImagePainter: Painter,
    title: String,
    description: String? = null,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Contenedor izquierdo: Imagen + Textos
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                // Imagen izquierda
                Image(
                    painter = leftImagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(16.dp))

                // Columna con título y descripción
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Medium
                        ),
                        color = Color.Black
                    )

                    if (!description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            androidx.compose.material3.Icon(
                painter = painterResource(id = R.drawable.ic_arrow_forward), // Asegúrate de tener este recurso
                contentDescription = "Flecha",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview
@Composable
fun SettingsItemPreview() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            SettingsItem(
                leftImagePainter = painterResource(id = R.drawable.ic_g_cuenta),
                title = "Título del botón",
                description = "Esta es una descripción opcional del botón",
                onClick = { /* Acción al hacer clic */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsItem(
                leftImagePainter = painterResource(id = R.drawable.ic_casales),
                title = "Botón sin descripción",
                onClick = { /* Acción al hacer clic */ }
            )
        }
    }
}