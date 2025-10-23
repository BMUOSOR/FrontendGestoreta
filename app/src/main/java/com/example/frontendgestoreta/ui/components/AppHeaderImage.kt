package com.example.frontendgestoreta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.R

@Composable
fun AppHeaderImage() {
    Box(
        modifier = Modifier
            .padding(top = 40.dp)
            .padding(bottom = 10.dp)
            .fillMaxWidth(),

        contentAlignment = Alignment.Center
    ) { Image(
        painter = painterResource(id = R.drawable.ic_gestoreta),
        contentDescription = "Gestoreta", // Descripción para accesibilidad
        modifier = Modifier
            .height(50.dp), // Dale una altura fija a la barra del título
        contentScale = ContentScale.Fit // Asegura que la imagen se vea completa sin deformarse
    )
    }
}