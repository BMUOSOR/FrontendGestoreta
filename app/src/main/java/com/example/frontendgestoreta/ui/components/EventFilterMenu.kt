package com.example.frontendgestoreta.ui.components

import android.graphics.drawable.Icon
import android.util.Log
import androidx.annotation.ColorRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults

import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontendgestoreta.R
import com.example.frontendgestoreta.data.models.EventFilterDTO
import com.example.frontendgestoreta.data.models.Tag
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
    modifier : Modifier = Modifier.background(colorResource(R.color.white)),
    viewModel : EventViewModel = viewModel()

) {

    var selectedIndex by remember { mutableStateOf(-1) } // ninguno seleccionado
    var expanded by remember { mutableStateOf(false) }
    val rotationAngle by animateFloatAsState(if (expanded) 180f else 0f, label = "")
    val fallas by viewModel.fallas.collectAsState()
    val tags by viewModel.tags.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(
                color = colorResource(R.color.white),
                shape = RoundedCornerShape(12.dp)
            )
            .border(2.dp, colorResource(R.color.black), RoundedCornerShape(12.dp))
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(
            Modifier.fillMaxWidth().background(colorResource(R.color.white)).border(2.dp,colorResource(R.color.white)),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Filtros", style = MaterialTheme.typography.titleMedium, color = colorResource(R.color.black))
        }

        AnimatedVisibility(expanded) {
            Column(Modifier.padding(top = 16.dp).background(colorResource(R.color.white))) {
                Column(Modifier.padding(top = 16.dp).background(colorResource(R.color.white))) {

                    // PUBLIC SWITCH
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Evento público", color= colorResource(R.color.black))
                        Switch(
                            checked = filter.public ?: true,
                            onCheckedChange = { newValue ->
                                onFilterChange(filter.copy(public = newValue))
                            },
                            colors= SwitchDefaults.colors(
                                checkedTrackColor = colorResource(R.color.purple_200)
                            )
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

                    Spacer(Modifier.height(16.dp))


                    // FALLAS
                    FallaSelector(
                        fallas = fallas,
                        selectedFallaId = filter.fallaId,
                        onFallaSelected = { id ->
                            onFilterChange(filter.copy(fallaId = id))
                        }
                    )

                    Spacer(Modifier.height(12.dp))
                    Text("Tipo", color =colorResource(R.color.black))
                    Spacer(Modifier.height(12.dp))

                    // TAG
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                            .background(colorResource(R.color.white)),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = {
                                onFilterChange(filter.copy(tagId=3))
                                Log.d("FilterEvents", "${filter.tagId}")
                                selectedIndex = 0
                            }

                        ) {
                            Icon(

                                painter = painterResource(id = R.drawable.ic_verbenas),
                                contentDescription = "Verbenas",
                                tint = if (selectedIndex == 0) Color(R.color.purple_200) else Color(R.color.black)
                            )
                        }

                        IconButton(onClick = { onFilterChange(filter.copy(tagId=1))
                            selectedIndex = 1
                        }) {
                            Icon(

                                painter = painterResource(id = R.drawable.ic_cultura),
                                contentDescription = "Cultura",
                                tint = if (selectedIndex == 1) Color(R.color.purple_200) else Color(R.color.black)

                            )
                        }

                        IconButton(onClick = { onFilterChange(filter.copy(tagId=2))
                            selectedIndex = 2
                        }) {
                            Icon(

                                painter = painterResource(id = R.drawable.ic_pasacalles),
                                contentDescription = "Pasacalles",
                                tint = if (selectedIndex == 2) Color(R.color.purple_200) else Color(R.color.black)

                            )
                        }

                        IconButton(onClick = { onFilterChange(filter.copy(tagId=4))
                            selectedIndex = 3
                        }) {
                            Icon(

                                painter = painterResource(id = R.drawable.ic_pirotecnia),
                                contentDescription = "Pirotecnia",
                                tint = if (selectedIndex == 3) Color(R.color.purple_200) else Color(R.color.black)

                            )
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = onClearFilter,
                        ) {
                            Text("Limpiar", color = colorResource(R.color.black),
                                style = TextStyle(color = colorResource(R.color.black), textDecoration = TextDecoration.Underline))
                        }

                        Button(
                            onClick = {
                                if(filter.tagId!= null) {
                                    Log.d("FilterEvents", "${filter.tagId}")
                                }
                                onApplyFilters(filter)
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = colorResource(R.color.purple_200),
                            ),
                        ) {
                            Text("Aplicar", color = colorResource(R.color.black))
                        }
                    }

                }
            }

        }
        Log.d("EventFilterMenu","Antes de: " + filter.beforeDate + " Después de: " + filter.afterDate)
        Log.d("EventFilterMenu","Formato fecha: " + filter.beforeDate)
    }
}