package com.udb.convite.ui.create

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udb.convite.data.EventModel
import com.udb.convite.data.FirebaseRepository
import com.udb.convite.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(onBack: () -> Unit) {
    val repository = remember { FirebaseRepository() }
    val scope = rememberCoroutineScope()

    var title by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("24/08/2026") }
    var time by remember { mutableStateOf("4:00pm") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Cumpleaños") }
    var reminderTime by remember { mutableStateOf("1 día antes | 9:00am") }
    var groupTag by remember { mutableStateOf("Esta semana") }
    var isSaving by remember { mutableStateOf(false) }

    val categories = listOf("Cumpleaños", "xv años", "Boda", "Bautizo", "Graduación", "Otro")

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nuevo evento",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = TextDark)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = WarmBackground)
            )
        },
        containerColor = WarmBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Text("Título del evento", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Ej: Cumpleaños de Sofía") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Fecha", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = date,
                        onValueChange = { date = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text("Hora", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = time,
                        onValueChange = { time = it },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Ubicación", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                placeholder = { Text("Dirección del lugar") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Descripción", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                placeholder = { Text("Detalles del evento") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Categoría", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { cat ->
                    FilterChip(
                        selected = category == cat,
                        onClick = { category = cat },
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BerryPrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Recordatorio", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
            Spacer(modifier = Modifier.height(6.dp))
            OutlinedTextField(
                value = reminderTime,
                onValueChange = { reminderTime = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (title.isBlank()) return@Button
                    isSaving = true
                    scope.launch {
                        try {
                            repository.addEvent(
                                EventModel(
                                    title = title,
                                    date = date,
                                    time = time,
                                    location = location,
                                    description = description,
                                    category = category,
                                    reminderTime = reminderTime,
                                    groupTag = groupTag,
                                    reminderActive = true
                                )
                            )
                            onBack()
                        } catch (_: Exception) {
                            isSaving = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BerryPrimary),
                shape = RoundedCornerShape(14.dp),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text(
                        text = "Guardar Evento",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}
