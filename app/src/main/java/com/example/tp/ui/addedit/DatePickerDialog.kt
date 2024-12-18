package com.example.tp.ui.addedit


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate?.let {
            it.atStartOfDay()
                .atZone(java.time.ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli()
        }
    )

    Button(onClick = { showDatePicker = true }) {
        Text(selectedDate?.toString() ?: "Seleccionar fecha de vencimiento")
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let {
                            val selectedLocalDate = LocalDate.ofEpochDay(
                                it / (24 * 60 * 60 * 1000)
                            )
                            onDateSelected(selectedLocalDate)
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showDatePicker = false
                        // onDateSelected(null)
                    }
                ) {
                    Text("Cancelar")
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                title = {
                    Text(
                        text = "Selecciona la fecha de vencimiento",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            )
        }
    }
}