# Walkthrough - Convite Development

Hemos desarrollado con éxito **Convite**, la aplicación móvil nativa para Android construida con Kotlin, Jetpack Compose y Firebase (Auth y Realtime Database).

## Cambios Realizados

### 🎨 Diseño y Tema Visual
- **Paleta de Colores:** Implementación exacta de la identidad visual de Convite:
  - Primario Berenjena / Berry (`#A32D5E`) para encabezados, marca y acciones principales.
  - Secundario Verde Azulado (`#1B5E56`) para la pantalla de detalle de eventos.
  - Acentos Marigold (`#E8A33D`) para recordatorios y distintivos de fecha, y Coral (`#F2765F`) para alertas y botones flotantes.
- **Tipografía y Estética:** Diseño editorial inspirado en la papelería festiva latinoamericana.

### 🔐 Autenticación de Firebase (`AuthScreen.kt`)
- Pantalla de inicio de sesión y registro adaptada al diseño de Convite con encabezado Berry, campos limpios y sincronización con Firebase Authentication.

### 🏠 Pantalla de Inicio / Próximos Eventos (`HomeScreen.kt`)
- Lista de eventos agrupados por fecha ("Esta semana", "Próximamente").
- Distintivo circular de día/mes con borde marigold y texto berry.
- Barra de búsqueda superior, botón flotante de acción (`+`) para crear eventos y barra de navegación inferior (Inicio, Calendario, Recordatorios, Perfil).

### 📝 Crear Evento (`CreateEventScreen.kt`)
- Formulario completo con título, fecha, hora, ubicación, descripción, selector de categorías mediante chips (`Cumpleaños`, `XV años`, `Boda`, `Bautizo`, `Graduación`), configuración de recordatorio y botón de guardado en Firebase Realtime Database.

### 🔎 Detalle del Evento (`EventDetailScreen.kt`)
- Encabezado Verde Azulado (`#1B5E56`), visualización completa de datos, tarjeta con interruptor para activar/desactivar recordatorios y botones de edición y eliminación.

### 🔔 Recordatorios (`RemindersScreen.kt`)
- Panel independiente agrupado por fecha con cabecera Marigold y avisos organizados.

### 📅 Calendario & Perfil (`CalendarScreen.kt`, `ProfileScreen.kt`)
- Vistas dedicadas de calendario y perfil con opción de cierre de sesión.

## Verificación y Pruebas
- **Compilación Exitosa:** El proyecto compila correctamente con Gradle (`app:assembleDebug`).
- **Sincronización en Tiempo Real:** Todos los eventos se sincronizan mediante Firebase Realtime Database.
