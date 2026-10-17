# Guía del proyecto Setómetro

Esta guía refleja el código actual del MVP de la versión 2. La raíz Gradle es esta carpeta; `app/build/` contiene artefactos generados, no fuentes.

## Objetivo y reglas del producto

Setómetro debe ayudar a consultar las condiciones meteorológicas del punto que indique la persona y, en el futuro, contextualizarlas con hábitat y conocimiento micológico fiable.

- No generar ni mostrar datos meteorológicos, cotos, hábitats, hallazgos o probabilidades inventados.
- Diferenciar condiciones meteorológicas, hábitat compatible y observaciones de presencia.
- Lluvia, temperatura y altitud no confirman que haya setas. La falta de observaciones tampoco significa que no las haya.
- Si una API no devuelve datos, indicar el error o la variable ausente. No convertir fallos en éxitos ni usar cifras de muestra.
- Una API meteorológica de cuadrícula no equivale a un sensor instalado en las coordenadas exactas. Mostrar la fuente, el periodo, las coordenadas solicitadas y las coordenadas/modelo resueltas.

## Estado funcional actual

| Área | Estado |
| --- | --- |
| Navegación Compose | Funcionan las pestañas principales. |
| Ubicaciones | Permite guardar ubicaciones manualmente o desde una consulta GPS/mapa, ver el origen/precisión de coordenadas y abrir detalles. No hay un catálogo oficial de cotos de toda España. El bosque indicado no se considera verificado. |
| Meteorología | El detalle consulta Open-Meteo usando las coordenadas guardadas: condiciones actuales y datos diarios de los 15 días anteriores. Los errores se presentan en pantalla. |
| Naturaleza de meteorología | Son datos de modelo para la cuadrícula que devuelve el proveedor, no mediciones de estación en el punto. La app muestra coordenadas del modelo, elevación modelada, zona horaria y hora de consulta. |
| Mapa y GPS | Muestra ubicaciones locales guardadas con marcadores neutros. Mantener pulsado un punto abre una consulta temporal de Open-Meteo; el botón GPS solicita permiso bajo demanda, informa la precisión del dispositivo y consulta sin guardar automáticamente. «Guardar punto» añade el lugar con nombre a las ubicaciones monitorizadas. |
| Especies y hábitat | El catálogo está vacío hasta reunir especies, hábitats y requisitos respaldados por fuentes verificables. |
| Probabilidad de presencia | Desactivada: no hay un modelo calibrado y evidencia suficientes para dar porcentajes fiables. |
| Cache/offline | La respuesta meteorológica no se persiste ni hay caché offline todavía. |
| Feedback de campo | Pendiente; la propuesta es guardarlo localmente y distinguir «encontré», «no encontré» y «no busqué». |
| CSV | Aplazado. |

## API meteorológica

La integración anterior usaba OpenWeatherMap con un endpoint de pronóstico de cinco días y una clave incorporada a la app. El repositorio ignoraba el cuerpo de esa respuesta y generaba condiciones sintéticas; esos valores ya no se usan.

El MVP usa el endpoint `https://api.open-meteo.com/v1/forecast` con `past_days=15`, variables diarias de precipitación/temperatura/humedad y condiciones actuales. La respuesta se valida en [WeatherRepository.kt](./app/src/main/java/com/setometro/app/data/repository/WeatherRepository.kt) antes de presentarse. No se almacena en Room.

La API gratuita de Open-Meteo se limita a usos no comerciales y requiere atribución según CC BY 4.0. La app es personal/no comercial por ahora; reevaluar el proveedor y su licencia antes de monetizar o distribuir comercialmente. Referencias: [documentación](https://open-meteo.com/en/docs), [términos](https://open-meteo.com/en/terms), [atribución/licencia](https://open-meteo.com/en/licence).

## Arquitectura y código

El código Kotlin está bajo `app/src/main/java/com/setometro/app/`.

| Paquete | Responsabilidad |
| --- | --- |
| `ui/screens/` | Interfaz Compose y estados visibles. |
| `ui/viewmodel/` | Estado observable y acciones de pantalla. |
| `navigation/` | Rutas de navegación. |
| `data/api/`, `data/model/` | Retrofit y modelos de respuesta Open-Meteo. |
| `data/repository/` | Consulta, validación y mapeo de datos remotos/locales. |
| `data/entity/`, `data/dao/`, `data/database/` | Persistencia Room de ubicaciones y datos locales. |
| `data/preferences/` | Preferencias de usuario. |
| `di/` | Proveedores Hilt/Room/Retrofit. |

Flujo de datos: `pantalla Compose → ViewModel/StateFlow → Repository → API o Room`. Operaciones de red fuera de los composables. Las pantallas representan carga, éxito y error de forma explícita.

## Tareas de la versión 2

1. **Hecho — Auditar la API anterior:** OpenWeatherMap estaba configurada, pero sus datos no alimentaban la interfaz; lo mostrado era sintético.
2. **Hecho — Elegir y probar proveedor:** Open-Meteo, apropiada para el MVP personal y con atribución. Se validó en vivo un punto de Almendralejo; el proveedor devolvió tiempo actual y días anteriores junto a la ubicación/elevación del modelo.
3. **Hecho — Eliminar salidas ficticias:** retiradas la generación meteorológica aleatoria, los fallbacks de muestra y el cálculo de porcentajes no validado mostrado por la UI. La migración de Room limpia el clima/probabilidades heredados, elimina especies de muestra y puntos de demostración conocidos, conservando ubicaciones personalizadas.
4. **Hecho — Consultar puntos indicados:** el mapa permite seleccionar un punto con pulsación larga o solicitar ubicación GPS bajo demanda. Ambas opciones muestran la precisión/procedencia conocida, consultan la misma API real por coordenadas y no guardan automáticamente. «Guardar punto» añade un nombre y lo incorpora a las ubicaciones monitorizadas.
5. **Pendiente — Conocimiento micológico verificable:** construir catálogo y reglas por especie/región solo con fuentes expertas trazables y cobertura suficiente.
6. **Pendiente — Feedback local:** registrar especie, fecha, coordenada y «encontré/no encontré/no busqué».
7. **En curso — Validación completa:** pruebas unitarias y APK debug pasan; se comprobó GPS, consulta meteorológica real, guardado y migración Room en emulador. Falta cubrir de forma automatizada los nuevos flujos, los errores/offline y las rutas de permiso denegado.

## Preparar, compilar y probar

Requisitos del módulo `app`: JDK compatible con Android Gradle Plugin 8.13, Android SDK con API 34 y emulador o dispositivo (minSdk 24). El proyecto compila el bytecode con Java 11; Gradle se ejecuta con un JDK más nuevo. En el entorno revisado se compiló con el JBR 21 de Android Studio.

Comandos previstos desde PowerShell en la raíz del proyecto:

```powershell
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleDebug
.\gradlew.bat installDebug
```

Las pruebas unitarias actuales están en `app/src/test/`; ejecutar `testDebugUnitTest`. Las pruebas instrumentadas/UI no están implementadas aún. El APK debug se crea en `app/build/outputs/apk/debug/app-debug.apk`.

**Windows y rutas con caracteres no ASCII:** el Wrapper ya está regenerado para Gradle 8.13. El Android Gradle Plugin puede bloquear builds y pruebas si la ruta contiene `ó`; no desactivar ese control con una propiedad experimental. Se verificaron `testDebugUnitTest` y `assembleDebug` desde una unidad temporal ASCII:

```powershell
subst V: "<ruta-completa-del-proyecto>"
Set-Location V:\
.\gradlew.bat testDebugUnitTest assembleDebug
```

Adapta la ruta y la letra libre a tu equipo, o mueve el proyecto a una ruta ASCII. El APK debug se crea en `app/build/outputs/apk/debug/app-debug.apk`.

### Comprobación manual de meteorología y puntos

1. En el mapa, mantener pulsado un lugar y comprobar que se consulta sin crear una ubicación automáticamente.
2. Usar «Usar mi ubicación», conceder el permiso y comprobar que aparecen las coordenadas y precisión informadas por Android.
3. Verificar condiciones actuales, serie diaria, atribución Open-Meteo y diferencia entre coordenada solicitada y coordenada del modelo.
4. Usar «Guardar punto», asignar nombre y comprobar que aparece en ubicaciones; su detalle debe indicar el origen y, si se informó, la precisión.
5. Probar sin red, con permiso denegado y con ubicación desactivada: la app debe explicar cada fallo, sin reutilizar valores inventados.
6. Confirmar que los marcadores solo representan ubicaciones locales guardadas, nunca predicción o ausencia de setas.
