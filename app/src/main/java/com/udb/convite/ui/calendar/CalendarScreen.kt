package com.udb.convite.ui.calendar

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.udb.convite.data.EventModel
import com.udb.convite.data.FirebaseRepository
import com.udb.convite.ui.theme.*
import java.util.Calendar

@Composable
fun CalendarScreen(
    onNavigateHome: () -> Unit,
    onNavigateReminders: () -> Unit,
    onNavigateProfile: () -> Unit,
    onNavigateToDetail: (String) -> Unit
) {
    val repository = remember { FirebaseRepository() }
    var events by remember { mutableStateOf<List<EventModel>>(emptyList()) }

    val currentCalendar = remember { Calendar.getInstance() }
    var selectedDay by remember { mutableStateOf(currentCalendar.get(Calendar.DAY_OF_MONTH)) }
    var selectedMonth by remember { mutableStateOf(currentCalendar.get(Calendar.MONTH)) }
    var selectedYear by remember { mutableStateOf(currentCalendar.get(Calendar.YEAR)) }

    LaunchedEffect(Unit) {
        repository.getEvents().collect { list ->
            events = list
        }
    }

    val monthNames = listOf("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre")
    val monthName = monthNames.getOrElse(selectedMonth) { "Agosto" }

    val tempCal = Calendar.getInstance().apply {
        set(Calendar.YEAR, selectedYear)
        set(Calendar.MONTH, selectedMonth)
        set(Calendar.DAY_OF_MONTH, 1)
    }
    val maxDays = tempCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    val startDayOfWeek = tempCal.get(Calendar.DAY_OF_WEEK) - 1

    val formattedSelectedDate = String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)
    val dayEvents = events.filter { it.date == formattedSelectedDate }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = TextDark
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") },
                    label = { Text("Inicio") },
                    selected = false,
                    onClick = onNavigateHome
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Calendario") },
                    label = { Text("Calendario") },
                    selected = true,
                    onClick = {}
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Recordatorios") },
                    label = { Text("Recordatorios") },
                    selected = false,
                    onClick = onNavigateReminders
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
                    label = { Text("Perfil") },
                    selected = false,
                    onClick = onNavigateProfile
                )
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
                            TealSecondary,
                            shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            text = "Calendario",
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Organiza tus fechas especiales",
                            fontSize = 15.sp,
                            color = Color.White.copy(alpha = 0.9f)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CardSurface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = {
                                if (selectedMonth == 0) {
                                    selectedMonth = 11
                                    selectedYear -= 1
                                } else {
                                    selectedMonth -= 1
                                }
                                selectedDay = 1
                            }) {
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Mes anterior", tint = TextDark)
                            }

                            Text(
                                text = "$monthName $selectedYear",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = TextDark
                            )

                            IconButton(onClick = {
                                if (selectedMonth == 11) {
                                    selectedMonth = 0
                                    selectedYear += 1
                                } else {
                                    selectedMonth += 1
                                }
                                selectedDay = 1
                            }) {
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Mes siguiente", tint = TextDark)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                            listOf("D", "L", "M", "M", "J", "V", "S").forEach { dayLabel ->
                                Text(
                                    text = dayLabel,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = TextMuted,
                                    modifier = Modifier.width(36.dp),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val totalCells = startDayOfWeek + maxDays
                        val totalRows = (totalCells + 6) / 7

                        for (row in 0 until totalRows) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                for (col in 0 until 7) {
                                    val cellIndex = row * 7 + col
                                    val dayNum = cellIndex - startDayOfWeek + 1
                                    val isCurrentMonthDay = dayNum in 1..maxDays

                                    if (isCurrentMonthDay) {
                                        val cellDateStr = String.format("%02d/%02d/%d", dayNum, selectedMonth + 1, selectedYear)
                                        val hasEvent = events.any { it.date == cellDateStr }
                                        val isSelected = selectedDay == dayNum

                                        Box(
                                            modifier = Modifier
                                                .size(38.dp)
                                                .clip(CircleShape)
                                                .background(
                                                    when {
                                                        isSelected -> BerryPrimary
                                                        hasEvent -> MarigoldAccent.copy(alpha = 0.25f)
                                                        else -> Color.Transparent
                                                    }
                                                )
                                                .clickable { selectedDay = dayNum },
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text(
                                                    text = dayNum.toString(),
                                                    fontWeight = if (isSelected || hasEvent) FontWeight.Bold else FontWeight.Normal,
                                                    fontSize = 14.sp,
                                                    color = if (isSelected) Color.White else TextDark
                                                )
                                                if (hasEvent && !isSelected) {
                                                    Box(
                                                        modifier = Modifier
                                                            .size(4.dp)
                                                            .background(BerryPrimary, CircleShape)
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        Spacer(modifier = Modifier.size(38.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Eventos para el $formattedSelectedDate",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (dayEvents.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No hay eventos programados para este día.",
                            color = TextMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            } else {
                items(dayEvents) { event ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 6.dp)
                            .clickable { onNavigateToDetail(event.id) },
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = CardSurface)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = event.title,
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    color = TextDark
                                )
                                Text(
                                    text = event.time,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BerryPrimary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = event.location.ifEmpty { "Ubicación por definir" },
                                fontSize = 13.sp,
                                color = TextMuted
                            )
                            if (event.description.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = event.description,
                                    fontSize = 12.sp,
                                    color = TextMuted,
                                    maxLines = 2
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
