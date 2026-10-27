# Convite

Aplicación Android para organizar celebraciones y eventos sociales (cumpleaños, XV años, bodas, bautizos, graduaciones y más). Permite crear eventos con fecha, hora, ubicación, descripción y categoría, consultarlos en una lista y en un calendario, y definir recordatorios.

Proyecto de cátedra de **Desarrollo de Software para Móviles** — Universidad Don Bosco.

## Integrantes

| Nombre | Carnet |
| --- | --- |
| Carlos Josué Argueta Ayala | AA251811 |
| Adriana Eunice Flores Hernández | FH190090 |
| Francisco Andrés Alas Vásquez | AV250078 |
| Emanuel Ronaldo Monroy Paz | MP252018 |

## Documentación

- **Manual de usuario:** [Carpeta de manuales en Google Drive](https://drive.google.com/drive/folders/1WgiBvtBehI2rpKbYU1YEaaSXqofYF4cU?usp=sharing)
- **Manual técnico:** [Carpeta de manuales en Google Drive](https://drive.google.com/drive/folders/1WgiBvtBehI2rpKbYU1YEaaSXqofYF4cU?usp=sharing)
- **Diseños (mockups):** [Convite-DSM en Figma](https://www.figma.com/design/DjwJhFJElbU8qvQ3D30yAq/Convite-DSM?node-id=0-1&t=69rzXHfCkO9D2GMZ-1)

## Funcionalidades

- Registro e inicio de sesión con correo y contraseña (Firebase Authentication).
- Creación de eventos con título, fecha, hora, ubicación, descripción y categoría.
- Categorías: Cumpleaños, XV años, Boda, Bautizo, Graduación y Otro.
- Recordatorios de selección múltiple (5, 10, 15 y 30 minutos, 1 hora y 1 día antes), con interruptor para activarlos o desactivarlos.
- Lista de próximos eventos agrupada en "Esta semana" y "Próximamente", con búsqueda por título, categoría o ubicación.
- Calendario mensual con los eventos de cada día.
- Pantalla consolidada de recordatorios.
- Detalle y eliminación de eventos.
- Perfil de usuario y cierre de sesión.

Funciones no incluidas en la versión 1.0: edición completa de eventos, adjuntar imágenes, notificaciones en el dispositivo, inicio de sesión con Google y recuperación de contraseña.

## Tecnologías

- Kotlin y Jetpack Compose (Material 3)
- Navigation Compose
- Firebase Authentication y Firebase Realtime Database
- Coil (carga de imágenes)
- SQLite (base de datos local)
- Android Studio, Gradle (Kotlin DSL)

| Dato | Valor |
| --- | --- |
| applicationId | `com.udb.convite` |
| minSdk | 26 (Android 8.0) |
| targetSdk / compileSdk | 37 |
| Versión | 1.0 |

## Estructura del proyecto

```
app/src/main/java/com/udb/convite/
├── MainActivity.kt        # Punto de entrada; decide si ir a acceso o inicio
├── data/                  # Modelo de evento, repositorio de Firebase y base local SQLite
└── ui/
    ├── auth/              # Inicio de sesión y registro
    ├── home/              # Pantalla principal y búsqueda
    ├── create/            # Formulario de nuevo evento
    ├── detail/            # Detalle del evento
    ├── calendar/          # Calendario mensual
    ├── reminders/         # Lista de recordatorios
    ├── profile/           # Perfil y cierre de sesión
    ├── navigation/        # Grafo de navegación
    └── theme/             # Colores y tema
```

## Cómo ejecutar el proyecto

**Requisitos:** Android Studio reciente (compatible con Kotlin 2.0 y AGP 9.x), JDK 11 y un emulador o dispositivo con Android 8.0 (API 26) o superior.

1. Clona el repositorio y ábrelo en Android Studio (**Open** y selecciona la carpeta raíz `DSM_Convite`).
2. Configura Firebase:
   - Crea un proyecto en la [consola de Firebase](https://console.firebase.google.com/).
   - Agrega una app Android con el paquete `com.udb.convite`.
   - Descarga `google-services.json` y colócalo en `app/google-services.json`.
   - Habilita **Authentication** con el proveedor Correo electrónico/Contraseña.
   - Habilita **Realtime Database** y define las reglas `".read": "auth != null"` y `".write": "auth != null"`.
3. Sincroniza Gradle (**Sync Project with Gradle Files**).
4. Ejecuta la app con **Run 'app'** (Shift + F10) y registra un usuario para empezar.

## Estructura de datos

Los eventos se guardan en Realtime Database bajo el nodo `/events`. Cada evento incluye: `id`, `userId`, `title`, `date` (DD/MM/AAAA), `time`, `location`, `description`, `category`, `reminderTime`, `reminders`, `reminderActive`, `groupTag` e `imageUrl` (reservado).
