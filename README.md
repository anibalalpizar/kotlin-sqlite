# TLC Go-Anywhere App

Android application using Jetpack Compose and following MVVM architecture pattern.

## Features

- Add new vehicles with details and images
- Persistent storage using Room Database
- Material 3 Design implementation
- Responsive UI with animations

## Architecture

The app follows the MVVM (Model-View-ViewModel) architecture pattern and Clean Architecture principles:

```
├── data
│   ├── AppDatabase.kt     # Database configuration
│   ├── VehiculoDao.kt     # Data Access Object
│   ├── VehiculoEntity.kt  # Database Entity
│   └── Vehiculo.kt        # Data model
├── ui
│   ├── theme             # Theme configuration
│   └── screens           # UI components
└── viewmodel
    └── VehiculoViewModel.kt # Business logic
```

### Components

- **Room Database**: Local storage implementation
  - Entity: VehiculoEntity
  - DAO: VehiculoDao
  - Database: AppDatabase

- **ViewModel**: Manages UI-related data and business logic
  - VehiculoViewModel: Handles vehicle data operations

- **Jetpack Compose**: Modern UI toolkit for building native Android UI

## Technical Stack

- Minimum SDK: 24
- Target SDK: 34
- Kotlin version: 1.8.10
- Gradle version: 8.11.1
- Android Studio: Meerkat | 2024.3.1 Patch 2
- Java version: 17.0.0.1

### Dependencies

- AndroidX Core KTX
- Lifecycle Runtime KTX
- Activity Compose
- Compose BOM
- Room Runtime & KTX
- Coil for image loading
- Navigation Compose

## Setup Instructions

1. Clone the repository: https://github.com/anibalalpizar/kotlin-sqlite
2. Open the project in Android Studio Meerkat | 2024.3.1 Patch 2
3. Make sure you have JDK 17.0.0.1 installed
4. Sync project with Gradle files
5. Run the app on an emulator or physical device
