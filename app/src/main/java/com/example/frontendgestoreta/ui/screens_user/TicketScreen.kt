package com.example.frontendgestoreta.ui.screens_user

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontendgestoreta.R
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketScreen(
    userName: String,
    onDismiss: () -> Unit
) {
    // Generar año aleatorio entre 1940 y 2021
    val randomYear = remember { Random.nextInt(1940, 2022) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFFD0D3FF), // Color lila de fondo de la imagen
        dragHandle = { BottomSheetDefaults.DragHandle(color = Color.White) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 50.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icono de estrella superior
            Icon(
                painter = painterResource(id = R.drawable.ic_ayuntamento_event), // Asegúrate de tener este SVG
                contentDescription = null,
                modifier = Modifier.size(40.dp),
                tint = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "¡Hola!", fontSize = 24.sp, color = Color.Black)
            Text(
                text = userName,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Text(
                text = "Fallera desde $randomYear",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjeta Blanca (El Ticket)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(vertical = 32.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Código de Miembro",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Falla del Pilar - Valencia",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = userName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Línea divisoria decorativa
                    Divider(
                        modifier = Modifier.padding(horizontal = 20.dp),
                        thickness = 1.dp,
                        color = Color.LightGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Código de barras
                    Image(
                        painter = painterResource(id = R.drawable.img_codigo_barras),
                        contentDescription = "Código de barras",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp),
                        contentScale = ContentScale.FillBounds
                    )
                }
            }
        }
    }
}