package com.example.frontendgestoreta.ui.screens_user

import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventDTO
import com.example.frontendgestoreta.data.models.MemberDTO
import com.example.frontendgestoreta.ui.components.DatePickerField
import com.example.frontendgestoreta.viewModel.AuthViewModel
import com.example.frontendgestoreta.viewModel.EventViewModel
import com.example.frontendgestoreta.viewModel.MemberViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.time.LocalDate

private fun byteArrayToFile(
    context: Context,
    byteArray: ByteArray,
    fileName: String = "temp_image.jpg"
): File {

    val file = File(context.cacheDir, fileName)

    file.outputStream().use { output ->
        output.write(byteArray)
    }

    return file
}

@Composable
fun ModifyUserScreen(
    onBack: () -> Unit,
    member: MemberDTO,
    viewModel: MemberViewModel = viewModel(),
    authViewModel: AuthViewModel
) {

    // Estados para los campos del formulario
    var nombre by remember { mutableStateOf(member.nombre ?: "") }
    var apellidos by remember { mutableStateOf(member.apellidos ?: "") }
    var fecha by remember { mutableStateOf(member.fechaNac ?: LocalDate.now()) }
    val (showInfoDialog, setShowInfoDialog) = remember { mutableStateOf(false) }
    val imageByteArray by authViewModel.pfp.collectAsState()
    val scrollState = rememberScrollState()
    // -----------------------------
    // 1) Image Picker Launcher
    // -----------------------------
    val context = LocalContext.current

    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                val input = context.contentResolver.openInputStream(uri)
                val bytes = input?.readBytes()
                if(bytes != null) {
                    authViewModel.setPFP(bytes)
                }
            }
        }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start
    ) {



        Spacer(modifier = Modifier.height(8.dp))

        // Campo Nombre
        Text(
            text = "Nombre",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingresa tu nombre") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Apellidos
        Text(
            text = "Apellidos",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = apellidos,
            onValueChange = { apellidos = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Ingresa tu nombre") },
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        //Campo Fecha Nacimiento
        Text(
            text = "Fecha de nacimiento",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        Box (    modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center) {

            // Campo Temporal para ver la fecha de color negro
            Box() {
                OutlinedTextField(
                    value = fecha.toString(),
                    onValueChange = { },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Ingresa tu nacimiento") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                    ),
                    readOnly = true,
                )
            }

            Box() {
                DatePickerField(
                    label = "Fecha de nacimiento",
                    date = fecha,
                    onDateSelected = { selected -> fecha = selected },
                )
            }

        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Foto de perfil",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .size(200.dp)
                .align(Alignment.CenterHorizontally)
                .clickable { imagePickerLauncher.launch("image/jpeg") }  // <-- ABRIR GALERÍA
        ) {
            Log.d("AuthViewModel", "${imageByteArray.size}")
            if (imageByteArray != null && imageByteArray.size > 1) {
                Image(
                    bitmap = imageByteArray.decodeToImageBitmap(),
                    contentDescription = "Imagen de perfil de $nombre $apellidos",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .align(Alignment.Center)
                )
            } else {
                // Imagen por defecto si no hay nada
                Image(
                    painter = painterResource(R.drawable.upload),
                    contentDescription = "Suba una imagen",
                    modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(24.dp)).align(
                        Alignment.Center)
                )
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Botones de acción
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = { onBack() }
            ) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    val editedMember = member.copy(
                        nombre = nombre,
                        apellidos = apellidos,
                        fechaNac = fecha,
                    )
                    Log.d("ModifyUserScreen", "Miembro editado: " + editedMember.apellidos)
                    Log.d("ModifyUserScreen", "Miembro editado: " + editedMember.fechaNac)
                    viewModel.updateUsuario(editedMember);
                    if(imageByteArray.size > 1) {
                        val file = byteArrayToFile(context,imageByteArray)
                        val requestFile = file
                            .asRequestBody("image/jpeg".toMediaTypeOrNull())
                        val part = MultipartBody.Part.createFormData(
                            "file",
                            file.name,
                            requestFile)
                        authViewModel.updateProfilePicture(imageByteArray,part)

                    }
                    setShowInfoDialog(true)
                },
                enabled = nombre.isNotBlank() && apellidos.isNotBlank()
            ) {
                Text("Guardar Cambios")
            }
            if (showInfoDialog) {
                AlertDialog(
                    onDismissRequest = { setShowInfoDialog(false) },
                    containerColor = colorResource(R.color.white),
                    title = { Text("Usuario modificado", color = colorResource(R.color.black), fontFamily = RalewayFont, fontWeight = FontWeight.SemiBold) },
                    text = { Text("Se ha actualizado la información del usuario", color = colorResource(R.color.black), fontFamily = RalewayFont) },
                    confirmButton = {
                        TextButton(onClick = {
                            setShowInfoDialog(false)
                            onBack()
                        }) {
                            Text("Aceptar", color = colorResource(R.color.indigo), fontFamily = RalewayFont)
                        }
                    }
                )
            }
        }


    }
}