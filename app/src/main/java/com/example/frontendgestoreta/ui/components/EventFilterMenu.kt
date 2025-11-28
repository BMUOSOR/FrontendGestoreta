package com.example.frontendgestoreta.ui.components

import android.graphics.drawable.Icon
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button

import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.viewModel.EventViewModel
import java.time.LocalDate
import java.time.LocalTime
import java.util.Calendar
import java.util.Locale

@Composable
fun EventFilterMenu(
    filter : EventFilterDTO,
    onFilterChange: (EventFilterDTO) -> Unit,
    onApplyFilters: (EventFilterDTO) -> Unit,
    onClearFilter: () -> Unit,
    modifier : Modifier = Modifier,
    viewModel : EventViewModel = viewModel()

) {

    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(if (expanded) 180f else 0f, label = "")
    val fallas by viewModel.fallas.collectAsState()
    val tags by viewModel.tags.collectAsState()
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { expanded = !expanded }
    ) {
        Column(Modifier.padding(16.dp)) {

            // HEADER
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Filtros", style = MaterialTheme.typography.titleMedium)

            }

            AnimatedVisibility(expanded) {
                Column(Modifier.padding(top = 16.dp)) {

                    // PUBLIC SWITCH
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Evento público")
                        Switch(
                            checked = filter.public ?: true,
                            onCheckedChange = { newValue ->
                                onFilterChange(filter.copy(public = newValue))
                            }
                        )
                    }

                    Spacer(Modifier.height(16.dp))

                    // DATE FILTERS
                    DatePickerField(
                        label = "Fecha desde",
                        date = filter.afterDate,
                        onDateSelected = { selected ->
                            onFilterChange(filter.copy(afterDate = selected))
                        },

                    )

                    Spacer(Modifier.height(12.dp))

                    DatePickerField(
                        label = "Fecha hasta",
                        date = filter.beforeDate,
                        onDateSelected = {
                            onFilterChange(filter.copy(beforeDate = it))
                        }
                    )

                    Spacer(Modifier.height(16.dp))

                    // TIME FILTERS
                    TimePickerField(
                        label = "Hora desde",
                        time = filter.afterTime,
                        onTimeSelected = { onFilterChange(filter.copy(afterTime = it)) }
                    )

                    Spacer(Modifier.height(12.dp))

                    TimePickerField(
                        label = "Hora hasta",
                        time = filter.beforeTime,
                        onTimeSelected = { onFilterChange(filter.copy(beforeTime = it)) }
                    )

                    Spacer(Modifier.height(16.dp))

                    // PEOPLE RANGE
                    OutlinedTextField(
                        value = filter.minPeopleNumber?.toString() ?: "",
                        onValueChange = {
                            val num = it.toLongOrNull()
                            onFilterChange(filter.copy(minPeopleNumber = num))
                        },
                        label = { Text("Mínimo de personas") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = filter.maxPeopleNumber?.toString() ?: "",
                        onValueChange = {
                            val num = it.toLongOrNull()
                            onFilterChange(filter.copy(maxPeopleNumber = num))
                        },
                        label = { Text("Máximo de personas") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(16.dp))

                    // LOCATION
                    OutlinedTextField(
                        value = filter.location.orEmpty(),
                        onValueChange = { onFilterChange(filter.copy(location = it)) },
                        label = { Text("Ubicación") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))

                    // TITLE
                    OutlinedTextField(
                        value = filter.title.orEmpty(),
                        onValueChange = { onFilterChange(filter.copy(title = it)) },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(Modifier.height(12.dp))



                    // FALLAS
                    FallaSelector(
                        fallas = fallas,
                        selectedFallaId = filter.fallaId,
                        onFallaSelected = { id ->
                            onFilterChange(filter.copy(fallaId = id))
                        }
                    )

                    Spacer(Modifier.height(12.dp))


                    // TAG
                    TagSelector(
                        tags = tags,
                        selectedTagId = filter.tagId,
                        onTagSelected = { id ->
                            onFilterChange(filter.copy(tagId = id))
                        }
                    )

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        OutlinedButton(
                            onClick = onClearFilter
                        ) {
                            Text("Limpiar")
                        }

                        Button(
                            onClick = { onApplyFilters(filter) }
                        ) {
                            Text("Aplicar")
                        }
                    }

                }
            }
        }
        Log.d("EventFilterMenu","Antes de: " + filter.beforeDate + " Después de: " + filter.afterDate)
        Log.d("EventFilterMenu","Formato fecha: " + filter.beforeDate)
    }
}