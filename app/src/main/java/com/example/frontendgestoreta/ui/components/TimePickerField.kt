package com.example.frontendgestoreta.ui.components

import android.app.TimePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.frontendgestoreta.R
import java.time.LocalTime

@Composable
fun TimePickerField(
    label: String,
    time: LocalTime?,
    onTimeSelected: (LocalTime?) -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = time?.toString() ?: "",
        onValueChange = {},
        label = { Text(label, color = colorResource(R.color.black)) },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { showDialog = true }
            .border(2.dp,colorResource(R.color.black), RoundedCornerShape(12.dp)),
        enabled = false,
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = colorResource(R.color.white),
            unfocusedContainerColor = colorResource(R.color.white)
        )
    )

    if (showDialog) {
        val initialHour = time?.hour ?: 12
        val initialMinute = time?.minute ?: 0

        TimePickerDialog(
            context,
            { _, h, m ->
                onTimeSelected(LocalTime.of(h, m))
                showDialog = false
            },
            initialHour,
            initialMinute,
            true
        ).show()
    }
}