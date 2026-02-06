# CheckIt

Aplicación Android de gestión de tareas desarrollada con Kotlin y Jetpack Compose.

## Descripción

CheckIt es una aplicación de tareas que permite crear, organizar y gestionar tus tareas diarias de forma sencilla. Incluye sistema de prioridades, fechas de vencimiento y opciones de ordenamiento.

## Características

- Crear tareas con título, descripción, fecha y prioridad
- Tres niveles de prioridad: Baja, Media y Alta (con indicador visual por colores)
- Marcar tareas como completadas
- Ordenar tareas por fecha o prioridad
- Descripción expandible en cada tarea
- Persistencia local con Room Database
- Interfaz moderna con Material Design 3

## Tecnologías

- **Kotlin** - Lenguaje principal
- **Jetpack Compose** - UI declarativa
- **Room** - Base de datos local
- **Navigation Compose** - Navegación entre pantallas
- **Coroutines + Flow** - Programación asíncrona
- **Material Design 3** - Componentes de UI

## Requisitos

- Android Studio (Arctic Fox o superior)
- JDK 11+
- SDK Android API 26+ (Android 8.0)

## Instalación

1. Clona el repositorio:
```bash
git clone https://github.com/AlexRoa6/CheckIt.git
```

2. Abre el proyecto en Android Studio

3. (Opcional) Configura las credenciales de AdMob en `local.properties`:
```properties
ADMOB_APP_ID=tu-app-id
ADMOB_BANNER_ID=tu-banner-id
ADMOB_INTERSTITIAL_ID=tu-interstitial-id
```

4. Sincroniza Gradle y ejecuta en un emulador o dispositivo

## Estructura del Proyecto

```
app/src/main/java/com/alexrdev/checkit/
├── data/
│   ├── AppDataBase.kt       # Configuración Room
│   ├── TaskDao.kt           # Operaciones de base de datos
│   └── TaskRepository.kt    # Repositorio
├── model/
│   ├── Task.kt              # Entidad y enums
│   └── Converters.kt        # Conversores de tipos
├── view/
│   ├── HomeView.kt          # Pantalla principal
│   └── NewtaskFormView.kt   # Formulario de tarea
├── viewModel/
│   ├── HomeViewModel.kt     # Lógica de pantalla principal
│   └── NewTaskViewModel.kt  # Lógica del formulario
└── ui/theme/                # Tema y estilos
```

## Arquitectura

La aplicación sigue el patrón **MVVM** (Model-View-ViewModel):

```
UI (Composables) → ViewModel (StateFlow) → Repository → Room Database
```

## API Level

- **Mínimo**: API 26 (Android 8.0)
- **Target**: API 36 (Android 15)

## Autor

Alex Roa - [@AlexRoa6](https://github.com/AlexRoa6)

