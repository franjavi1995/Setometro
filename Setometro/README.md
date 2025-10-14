# Setómetro

Setómetro es una app Android en Kotlin con Jetpack Compose para monitorizar condiciones meteorológicas y estimar la probabilidad de aparición de setas en cotos micológicos de España.

## 📁 Estructura principal del proyecto

```
Setometro/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/setometro/
│   │   │   │   ├── data/                # Entidades Room, DAOs, repositorio, API Open-Meteo
│   │   │   │   ├── domain/              # Lógica de probabilidad y modelos de especies
│   │   │   │   ├── ui/
│   │   │   │   │   ├── components/      # Componentes reutilizables (WeatherCard, etc)
│   │   │   │   │   ├── screens/         # Pantallas principales (Dashboard, Selección, Detalle, Setas, Configuración, NuevoCoto)
│   │   │   │   │   ├── theme/           # Colores, tipografía, estilos
│   │   │   │   │   └── SetometroNavHost.kt # Navegación Compose
│   │   │   │   ├── viewmodel/           # MainViewModel y lógica de UI
│   │   │   ├── res/
│   │   │   │   ├── values/              # strings.xml, colors.xml, themes.xml
│   │   │   │   └── ...
│   │   │   ├── assets/
│   │   │   │   └── cotos.json           # Ejemplo de cotos micológicos
├── build.gradle
├── README.md
└── ...
```

## ⚙️ Esquema de funcionamiento

- El usuario selecciona cotos desde un mapa o lista, o añade nuevos manualmente.
- La app descarga automáticamente los datos meteorológicos (lluvia y temperatura) de cada coto desde la API de Open-Meteo.
- Los datos se almacenan en la base de datos local (Room).
- Se calcula la probabilidad de aparición de setas según las condiciones y el tipo de bosque.
- El usuario puede ver gráficos, exportar registros a CSV, importar datos y forzar actualización manual.

## 📲 Instalación en dispositivo Android

1. Abre el proyecto en **Android Studio**.
2. Conecta tu móvil Android por USB y activa la **depuración USB**.
3. Haz clic en el botón “Run” (▶️) para compilar e instalar la app en tu dispositivo.
4. Alternativamente, genera un APK desde “Build > Build APK(s)” y transfiérelo a tu móvil para instalarlo manualmente.

## 🐞 Depuración y desarrollo

- Puedes depurar la app usando un **emulador Android** (AVD) o un dispositivo físico.
- Pon breakpoints, inspecciona variables y usa el logcat de Android Studio para ver mensajes y errores.
- Los cambios en el código pueden probarse rápidamente con “Run” o “Debug” en Android Studio.

## 📝 Archivos principales

- `MainActivity.kt`: Entrada principal de la app.
- `data/Entities.kt`, `Database.kt`, `WeatherRepository.kt`, `OpenMeteoApi.kt`: Modelos, base de datos y acceso a datos.
- `domain/ProbabilidadSetas.kt`: Lógica de probabilidad de aparición de setas.
- `ui/screens/`: Pantallas principales de la app.
- `ui/components/WeatherCard.kt`: Componente visual de condiciones meteorológicas.
- `ui/theme/`: Colores, tipografía y estilos visuales.
- `viewmodel/MainViewModel.kt`: Lógica de presentación y gestión de estado.
- `assets/cotos.json`: Ejemplo de cotos micológicos.

## 🚀 Notas

- Todos los datos se almacenan localmente, no requiere servidor externo.
- Puedes personalizar los cotos, especies y lógica de probabilidad según tus necesidades.
- Para publicar en Google Play, deberás firmar el APK con una clave de producción.

---

¿Dudas o sugerencias? ¡Abre un issue en el repositorio!