package com.udb.convite.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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

@Composable
fun EventDetailScreen(
    eventId: String,
    onBack: () -> Unit
) {
    val repository = remember { FirebaseRepository() }
    val scope = rememberCoroutineScope()
    var event by remember { mutableStateOf<EventModel?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(eventId) {
        repository.getEvents().collect { list ->
            event = list.find { it.id == eventId }
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BerryPrimary)
        }
        return
    }

    val currentEvent = event
    if (currentEvent == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Evento no encontrado", color = TextDark)
        }
        return
    }

    var reminderActive by remember { mutableStateOf(currentEvent.reminderActive) }

    Scaffold(
        containerColor = WarmBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        TealSecondary,
                        shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                    )
                    .padding(24.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar", tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = currentEvent.category.lowercase(),
                        fontSize = 15.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currentEvent.title,
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = BerryPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = currentEvent.date,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark
                        )
                        Text(
                            text = currentEvent.time,
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = BerryPrimary, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = currentEvent.location.ifEmpty { "Ubicación por definir" },
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark
                        )
                        Text(
                            text = "Ubicación del evento",
                            fontSize = 14.sp,
                            color = TextMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (currentEvent.description.isNotEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF7F2EC))
                    ) {
                        Text(
                            text = currentEvent.description,
                            fontSize = 14.sp,
                            color = TextDark,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFDF0ED))
                ) {
                    Row(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (reminderActive) "Recordatorio activo" else "Recordatorio inactivo",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = CoralAccent
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = currentEvent.reminderTime,
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                        }
                        Switch(
                            checked = reminderActive,
                            onCheckedChange = { newVal ->
                                reminderActive = newVal
                                scope.launch {
                                    repository.updateEvent(currentEvent.copy(reminderActive = newVal))
                                }
                            },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = CoralAccent
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            scope.launch {
                                repository.deleteEvent(currentEvent.id)
                                onBack()
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CoralAccent)
                    ) {
                        Text("Eliminar", fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = onBack,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEFECE6), contentColor = TextDark)
                    ) {
                        Text("Cerrar", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(40.dp))
            }
        }
    }
}
