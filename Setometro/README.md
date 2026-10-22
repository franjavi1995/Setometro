# Setómetro

Aplicación Android nativa en Kotlin y Jetpack Compose para consultar condiciones meteorológicas en ubicaciones guardadas y, cuando haya datos micológicos fiables, contextualizarlas por especie y hábitat.

## Principio de datos

La app no debe fabricar datos ni presentar valores de ejemplo como reales. Si una consulta falla o una fuente no ofrece un dato, se muestra esa ausencia y el motivo. El tiempo favorable no confirma que haya setas.

## Estado actual del MVP v2

- **Tiempo por ubicación guardada:** consulta Open-Meteo al abrir el detalle de una ubicación, usando sus coordenadas, condiciones actuales y una serie diaria de los 15 días anteriores.
- **Naturaleza de los datos:** Open-Meteo devuelve datos de modelos meteorológicos para una cuadrícula asociada a las coordenadas solicitadas. No son mediciones de una estación situada exactamente en el punto; la pantalla diferencia las coordenadas solicitadas de las coordenadas de modelo y muestra elevación, zona horaria, fecha y atribución.
- **Sin datos falsos:** se eliminaron la generación meteorológica aleatoria, las cifras de probabilidad de muestra y los fallbacks meteorológicos del detalle. Los errores de API se muestran en pantalla.
- **Puntos de consulta:** el mapa permite mantener pulsado un punto o consultar la ubicación actual del dispositivo. La pantalla consulta Open-Meteo sin guardar nada automáticamente; el botón «Guardar punto» permite ponerle nombre y añadirlo a las ubicaciones monitorizadas. Se muestra el origen y, para GPS, la precisión comunicada por Android.
- **Ubicaciones:** se pueden guardar puntos manualmente o desde una consulta del mapa/GPS. No hay un inventario oficial/verificado de cotos de toda España. El mapa muestra ubicaciones locales guardadas; no representa presencia o ausencia de setas.
- **Predicción micológica:** no se calcula ni se muestra porcentaje de presencia. No existe aún un modelo validado por especie y región ni datos de hábitat suficientemente verificados.
- **Conocimiento micológico:** [MAPA_CONOCIMIENTO_MICOLOGICO.md](./MAPA_CONOCIMIENTO_MICOLOGICO.md) recoge la evidencia localizada, sus límites y los datos que faltan por especie. Los estudios disponibles no respaldan aún reglas numéricas nacionales ni una clasificación de puntos.
- **Hábitat y cotos:** aún no hay un catálogo nacional verificado integrado. Se investigarán fuentes oficiales de cobertura forestal; Micocyl es un ejemplo regional de montes regulados, no un catálogo nacional ni una API confirmada. Como alternativa, se podrá preguntar qué árboles observa el usuario y etiquetar la respuesta como no verificada.
- **Orden de Mis Cotos:** pendiente de una evaluación micológica comparable por especie. No se ordenarán puntos por lluvia ni por una puntuación inventada.
- **GPS y selección de punto en mapa:** implementados bajo demanda; el permiso solo se solicita al usar GPS. Feedback de campo local está pendiente; sincronizar con un servidor doméstico queda para el futuro.
- **Exportación CSV:** fuera del alcance del MVP actual.

La app consulta la API Forecast de Open-Meteo (`/v1/forecast`) con `past_days=15`; no usa ya OpenWeatherMap. La respuesta de la API se valida y presenta en la pantalla, sin persistirla en Room ni ofrecer caché offline todavía.

Open-Meteo requiere atribución **CC BY 4.0** junto a los datos. Su API gratuita es solo para uso no comercial y tiene límites de llamadas; antes de monetizar o distribuir comercialmente, hay que contratar un plan con licencia adecuada o elegir otra fuente. Consulta los [términos](https://open-meteo.com/en/terms), la [licencia y atribución](https://open-meteo.com/en/licence) y la [documentación de la API](https://open-meteo.com/en/docs).

## Arquitectura

- Kotlin, Android y Jetpack Compose.
- MVVM, `StateFlow`, Coroutines y navegación Compose.
- Room para datos locales del usuario.
- Retrofit/OkHttp para Open-Meteo.
- Hilt para inyección de dependencias.
- OpenStreetMap/osmdroid para el mapa de ubicaciones guardadas.

El flujo esperado es `pantalla Compose → ViewModel → Repository → API o Room`. El detalle consulta el tiempo al abrirlo. Las actualizaciones periódicas de tiempo se han desactivado.

El código está en `app/src/main/java/com/setometro/app/`. La arquitectura, los comandos de desarrollo y los pendientes están en [GUIA_PROYECTO.md](./GUIA_PROYECTO.md); las pautas para cambios con Copilot están en [.github/copilot-instructions.md](./.github/copilot-instructions.md).

## Compilar y probar

Requisitos: JDK 17 o superior compatible con Android Gradle Plugin, Android SDK (API 34) y un emulador o dispositivo Android (minSdk 24).

Comandos habituales desde PowerShell en la raíz del proyecto:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

En Windows, el nombre actual de la carpeta contiene `ó`; Android Gradle Plugin puede bloquear builds y pruebas desde esa ruta. El Wrapper ya está reparado. Para trabajar desde una unidad temporal ASCII, elige una letra libre y ejecuta en PowerShell:

```powershell
subst V: "<ruta-completa-del-proyecto>"
Set-Location V:\
.\gradlew.bat testDebugUnitTest assembleDebug
```

Adapta la ruta y la letra a tu equipo. La compilación y las pruebas unitarias se verificaron así con Gradle 8.13. Como alternativa, mueve el proyecto a una ruta sin caracteres no ASCII.

El APK debug se genera en `app/build/outputs/apk/debug/app-debug.apk`. Las pruebas unitarias están en `app/src/test/`; las pruebas instrumentadas/UI aún están pendientes.

## Datos locales anteriores

La migración de la base de datos elimina registros meteorológicos y probabilidades heredados porque se generaban con valores ficticios, elimina el catálogo inicial sin fuentes verificadas y retira los puntos de demostración precargados conocidos. Conserva ubicaciones personalizadas que no coincidan con esos puntos de muestra.
