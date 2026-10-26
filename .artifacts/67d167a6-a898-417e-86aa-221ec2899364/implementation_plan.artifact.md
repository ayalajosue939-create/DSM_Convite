# Implementation Plan - Convite: Social & Family Events Planner

Convite is a native Android application built with Kotlin, Jetpack Compose, and Firebase (Auth & Realtime Database). It allows users to create, organize, and track social and family events (birthdays, Quinceañeras, weddings, baptisms, graduations, etc.) with custom branding inspired by Latin American festive stationery (Berenjena/berry `#A32D5E`, Verde azulado `#1B5E56`, Marigold `#E8A33D`, and Coral `#F2765F`).

## User Review Required

> [!IMPORTANT]
> - **Jetpack Compose UI:** We will implement the entire UI using Jetpack Compose with Material 3, custom typography (Serif titles and functional serif body), and precise colors matching the design mockups.
> - **Firebase Integration:** We will use Firebase Authentication (Email/Password) and Firebase Realtime Database to store and sync events across devices in real-time.
> - **Navigation:** We will implement bottom navigation with 4 tabs: Inicio (Home), Calendario (Calendar), Recordatorios (Reminders), and Perfil (Profile), plus auxiliary screens for Login, Register, Create Event, and Event Detail.

## Proposed Changes

### Build Configuration & Dependencies
#### [MODIFY] [libs.versions.toml](file:///C:/Users/Tito/AndroidStudioProjects/Convite/gradle/libs.versions.toml)
- Add Compose BOM, Compose Material3, Navigation Compose, Lifecycle ViewModel Compose, and Coil (for image loading).

#### [MODIFY] [build.gradle.kts](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/build.gradle.kts)
- Enable Compose build features (`compose = true`).
- Add dependencies for Compose, Navigation, Firebase, and Coil.

### Data & Domain Layer
#### [NEW] [EventModel.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/data/EventModel.kt)
- Data class for events (`id`, `userId`, `title`, `date`, `time`, `location`, `description`, `category`, `reminderTime`, `imageUrl`, `reminderActive`).

#### [NEW] [FirebaseRepository.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/data/FirebaseRepository.kt)
- Realtime Database client for creating, reading, updating, and deleting events per user.

### UI Theme & Typography
#### [NEW] [Theme.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/theme/Theme.kt)
- Define Convite color palette (`Berry`, `Teal`, `Marigold`, `Coral`, background cream/light, surface cards).
- Configure typography (`Inria Serif` / Serif titles and clean body text).

### Authentication Screens
#### [NEW] [AuthScreen.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/auth/AuthScreen.kt)
- Login and Register screens styled with Convite's festive identity (`#A32D5E` headers, clean text fields, Firebase Auth integration).

### Main Application Screens & Navigation
#### [NEW] [NavGraph.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/navigation/NavGraph.kt)
- Navigation graph connecting Auth, Home, Create Event, Event Detail, Calendar, Reminders, and Profile.

#### [NEW] [HomeScreen.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/home/HomeScreen.kt)
- "Próximos eventos" screen with search bar, grouped sections ("Esta semana", "Próximamente"), circular day/month badges, category tags, and FAB for new event.

#### [NEW] [CreateEventScreen.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/create/CreateEventScreen.kt)
- Event creation form with title, date, time, location, description, category chips (`Cumpleaños`, `XV años`, `Boda`, `Bautizo`, `Graduación`), reminder configuration, image attachment, and "Guardar Evento" button.

#### [NEW] [EventDetailScreen.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/detail/EventDetailScreen.kt)
- Teal header (`#1B5E56`), full event details, reminder active toggle, edit and delete actions.

#### [NEW] [RemindersScreen.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/reminders/RemindersScreen.kt)
- Reminders panel grouped by date ("Hoy", "Esta semana", "Próximo mes") with Marigold header (`#E8A33D`).

#### [NEW] [CalendarScreen.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/calendar/CalendarScreen.kt)
- Calendar view of events.

#### [NEW] [ProfileScreen.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/ui/profile/ProfileScreen.kt)
- User profile info and sign out button.

#### [MODIFY] [MainActivity.kt](file:///C:/Users/Tito/AndroidStudioProjects/Convite/app/src/main/java/com/udb/convite/MainActivity.kt)
- Set up Compose content and NavGraph initialization.

## Verification Plan

### Automated Tests
- Build project successfully using Gradle (`app:assembleDebug`).
- Run unit tests.

### Manual Verification
- Deploy app to Android device/emulator.
- Test Firebase authentication (register new account, login).
- Create a new event with category, date, time, location, description, and reminder.
- Verify event appears on Home screen grouped by date with circular date badge and category tag.
- Open event detail screen, verify Teal header and reminder toggle.
- Check Reminders screen grouped by "Hoy", "Esta semana", etc.
- Test Edit and Delete functionality.
