package com.udb.convite.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.udb.convite.data.EventModel
import com.udb.convite.data.FirebaseRepository
import com.udb.convite.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (String) -> Unit,
    onNavigateToCalendar: () -> Unit,
    onNavigateToReminders: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val repository = remember { FirebaseRepository() }
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        repository.getEvents().collect { list ->
            events = list
        }
    }

    val filteredEvents = events.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true) ||
                it.location.contains(searchQuery, ignoreCase = true)
    }

    val estaSemana = filteredEvents.filter { it.groupTag == "Esta semana" || it.groupTag == "Hoy" }
    val proximamente = filteredEvents.filter { it.groupTag != "Esta semana" && it.groupTag != "Hoy" }

    val userEmail = FirebaseAuth.getInstance().currentUser?.email ?: "Usuario"
    val userName = userEmail.substringBefore("@")

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = TextDark
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendario") },
                    label = { Text("Calendario") },
                    selected = false,
                    onClick = onNavigateToCalendar
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Recordatorios") },
                    label = { Text("Recordatorios") },
                    selected = false,
                    onClick = onNavigateToReminders
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = onNavigateToProfile
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToCreate,
                containerColor = CoralAccent,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Evento")
            }
        },
        containerColor = WarmBackground
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            BerryPrimary,
                            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Hola, ${userName.replaceFirstChar { it.uppercase() }}",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Tus próximos eventos",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Buscar evento", color = Color.White.copy(alpha = 0.7f)) },
                            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.6f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            ),
                            shape = RoundedCornerShape(16.dp),
                            singleLine = true
                        )
                    }
                }
            }

            if (estaSemana.isNotEmpty() || filteredEvents.isEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Esta semana",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextDark,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (estaSemana.isEmpty() && filteredEvents.isEmpty() && events.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No hay eventos aún. ¡Toca el botón + para crear uno!",
                                color = TextMuted,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else {
                    items(estaSemana) { event ->
                        EventCard(event = event, onClick = { onNavigateToDetail(event.id) })
                    }
                }
            }

            if (proximamente.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Próximamente",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = TextDark,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(proximamente) { event ->
                    EventCard(event = event, onClick = { onNavigateToDetail(event.id) })
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun EventCard(event: EventModel, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .border(1.5.dp, MarigoldAccent, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    val dateParts = event.date.split("/")
                    val dayNum = dateParts.getOrNull(0) ?: "24"
                    val monthNum = dateParts.getOrNull(1) ?: "08"
                    val monthStr = when (monthNum) {
                        "01" -> "ENE" "02" -> "FEB" "03" -> "MAR" "04" -> "ABR"
                        "05" -> "MAY" "06" -> "JUN" "07" -> "JUL" "08" -> "AGO"
                        "09" -> "SEP" "10" -> "OCT" "11" -> "NOV" "12" -> "DIC"
                        else -> "AGO"
                    }
                    Text(
                        text = dayNum,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = BerryPrimary
                    )
                    Text(
                        text = monthStr,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MarigoldAccent
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = event.title.ifEmpty { "Evento sin título" },
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = event.location.ifEmpty { "Ubicación por definir" },
                    fontSize = 13.sp,
                    color = TextMuted
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = event.category,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MarigoldAccent
                )
            }
        }
    }
}
