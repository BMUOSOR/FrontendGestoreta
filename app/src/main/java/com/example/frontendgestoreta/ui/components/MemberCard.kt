package com.example.frontendgestoreta.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.data.models.MemberRequestDTO
import com.example.frontendgestoreta.R

// --- 1. Componente para la tarjeta de un Miembro Actual ---
@Composable
fun MemberCard(
    member: MemberDTO,
    onButtonClick: () -> Unit, // botón en lugar de clic en toda la card
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = member.nombre ?: "Sin nombre",
                    fontSize = 18.sp,
                    color = colorResource(R.color.black)
                )
                Text(
                    text = member.apellidos ?: "Sin apellidos",
                    fontSize = 18.sp,
                    color = colorResource(R.color.black)
                )
            }

            IconButton(onClick = onButtonClick) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_info) ,
                    contentDescription = "Ver detalles"
                )
            }
        }
    }
}

// --- 2. Componente para la tarjeta de una Solicitud Pendiente ---
@Composable
fun MemberRequestCard(
    request: MemberRequestDTO,
    onInfoClick: () -> Unit,
    onAcceptClick: () -> Unit,
    onRejectClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.white)),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = request.contenido ?: "Sin contenido",
                    fontSize = 18.sp,
                    color = colorResource(R.color.black)
                )
                Text(
                    text = request.motivo ?: "Sin motivo",
                    fontSize = 18.sp,
                    color = colorResource(R.color.black)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End // todo a la derecha
            ) {
                IconButton(onClick = onInfoClick) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_info),
                        contentDescription = "Ver detalles"
                    )
                }
                Spacer(modifier = Modifier.width(4.dp)) // espacio entre botones
                IconButton(onClick = onRejectClick) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_reject),
                        contentDescription = "Rechazar"
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(onClick = onAcceptClick) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_accept),
                        contentDescription = "Aceptar",
                        tint = colorResource(R.color.indigo)
                    )
                }
            }
        }
    }
}