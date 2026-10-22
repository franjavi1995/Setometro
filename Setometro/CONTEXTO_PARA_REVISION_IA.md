# Contexto de Setómetro para revisión por otra IA

**Corte del proyecto:** 22 de octubre de 2026
**Aplicación:** Android nativa, Kotlin, Jetpack Compose
**Repositorio remoto:** <https://github.com/franjavi1995/Setometro>
**Historial de Git:** no se incluye en el ZIP; consultar el repositorio remoto para commits y ramas.

Este documento acompaña al código para que una revisión externa entienda el objetivo, el estado real, las restricciones del producto y las preguntas aún abiertas. La guía técnica está en [GUIA_PROYECTO.md](./GUIA_PROYECTO.md), el estado resumido en [README.md](./README.md) y la evidencia micológica y sus fuentes en [MAPA_CONOCIMIENTO_MICOLOGICO.md](./MAPA_CONOCIMIENTO_MICOLOGICO.md).

## Qué se quiere construir

Setómetro debe ayudar a una persona sin conocimientos micológicos a entender si las condiciones recientes **podrían ser compatibles** con la fructificación de una especie en el lugar que ella elija. El objetivo no es ser una app meteorológica genérica ni un escaparate visual: se pretende construir una herramienta útil, transparente y progresivamente validada con fuentes y observaciones de campo. La mejora visual también interesa, pero es secundaria y no debe desplazar el trabajo necesario para que la función micológica sea fiable.

La app todavía **no evalúa la probabilidad de encontrar setas**. Actualmente obtiene tiempo por coordenadas y permite registrar observaciones. La futura evaluación necesitará combinar de forma explicable datos meteorológicos/hídricos, especie, árboles hospedadores o vegetación, estación, suelo, región y límites de cada fuente. Una evaluación por especie debe preceder a cualquier ranking de ubicaciones.

## Prioridad absoluta: investigación micológica y estimaciones fiables

La investigación para convertir conocimiento micológico y ecológico en una evaluación útil por especie y lugar es **la prioridad principal del proyecto y el foco número uno de la revisión externa**. La finalidad de Setómetro es traducir evidencia fiable en una estimación comprensible de cuánto concuerdan las condiciones observadas con las asociadas a la fructificación de una especie concreta. Si esta función no es científicamente defendible y validable, las mejoras de diseño, navegación, mapas, cotos, exportación y demás extras son totalmente secundarias.

La IA revisora debe dedicar su mayor esfuerzo a investigar y responder:

- Qué estudios de campo, series temporales, modelos de producción y fuentes micológicas de calidad existen **para cada especie**, dando prioridad a *Boletus edulis*, *Boletus pinophilus* y las demás especies incluidas en la app.
- Qué encontraron exactamente: variables meteorológicas e hídricas, humedad y temperatura del suelo, vegetación y árboles hospedadores, suelo, estación, retardos entre lluvia y fructificación, duración de ventanas y relaciones con la producción. Distinguir resultados cuantificados de descripciones cualitativas; separar crecimiento del micelio, iniciación y desarrollo de carpóforos, producción y hallazgo por una persona.
- En qué localidades, regiones, tipos de bosque, periodos y métodos se basan los resultados; qué incertidumbre, sesgos, tamaño de muestra y límites de extrapolación tienen. No convertir resultados de un pinar o región en una regla para toda España.
- Qué datos observables podemos obtener de forma realista para un punto en España y con qué resolución, profundidad, cobertura, antigüedad y licencia. Comparar esos datos con las variables que los estudios realmente usaron; no asumir que lluvia/temperatura del aire sustituyen a humedad del suelo, hábitat o presencia del hospedador.
- Si los artículos y datos permiten definir una evaluación por especie y región. Proponer cómo expresar factores favorables, desfavorables y desconocidos, y cuándo abstenerse por falta de cobertura. No llamar «probabilidad» a una puntuación o heurística sin calibración estadística.
- Qué datos de observaciones de campo hacen falta para probar y calibrar las estimaciones (incluyendo esfuerzo y fecha de búsqueda, especie confirmada, lugar/hábitat y resultados positivos y negativos), cómo evitar sesgos y qué validación independiente permitiría afirmar que las estimaciones son fiables.

El entregable prioritario de la IA debe ser un **informe de evidencia y una propuesta verificable de evaluación micológica**, con referencias primarias rastreables y una matriz por especie que indique variable, resultado/cita, ámbito, disponibilidad del dato en Setómetro, incertidumbre y uso permitido. Debe señalar explícitamente lo que no pudo verificar y distinguir «no investigado/no encontrado» de «no existe». No basta con resumir fichas divulgativas ni con enumerar variables plausibles.

No inventar valores, umbrales, retardos, pesos, porcentajes, datos ni reglas para rellenar vacíos. Si la evidencia disponible no permite una estimación fiable para una especie, región o punto, recomendar qué investigación o datos faltan y que la app muestre esa limitación; no fabricar un modelo para aparentar que la función está resuelta. Solo después de resolver o acotar esta prioridad deben proponerse mejoras secundarias de interfaz o extras.

## Reglas de producto no negociables

1. No inventar datos, resultados, umbrales, cotos, especies, árboles, probabilidades ni observaciones.
2. Si no hay datos, mostrarlo y explicar por qué; no usar valores de ejemplo ni fallbacks con apariencia de datos reales.
3. No llamar “probabilidad de encontrar” a una heurística sin calibración y validación.
4. No inferir presencia o ausencia a partir de lluvia, altitud, tipo de bosque declarado ni una búsqueda sin resultados.
5. Diferenciar con precisión:
   - tiempo modelado y medición in situ;
   - hábitat/árbol declarado por el usuario y hábitat verificado por una fuente;
   - coto regulado y lugar con posible hábitat;
   - “encontré”, “busqué y no encontré” y “no busqué”.
6. Mantener las afirmaciones micológicas ligadas a fuente, ámbito geográfico, periodo, método e incertidumbre. Una ficha descriptiva no equivale a un estudio experimental.
7. Proteger las coordenadas y observaciones. El feedback actual permanece local; no hay sincronización remota.
8. No ordenar “Mis Cotos” por lluvia aislada ni mezclar puntuaciones/especies no comparables.
9. Toda característica nueva debe tener estados de carga, error, ausencia de datos y comportamiento sin conexión claramente definidos.

## Funcionalidad implementada

- **Ubicaciones locales:** añadir/guardar puntos y abrir su detalle. No hay catálogo nacional verificado de cotos.
- **Alta sin coordenadas manuales:** el usuario puede buscar un nombre o tocar el mapa; selecciona/ajusta allí el punto. Las coordenadas provienen del punto elegido en el mapa, no de campos de latitud/longitud.
- **Búsqueda geográfica:** Nominatim de OpenStreetMap bajo demanda, limitada a España, mínimo de tres caracteres, máximo de 8 resultados y al menos un segundo entre solicitudes. El texto de búsqueda se envía al servicio al pulsar Buscar; la ubicación actual no se transmite. Los resultados no prueban que el lugar sea un coto legal, vigente o micológicamente apto. Una consulta sin resultados no demuestra que el lugar no exista.
- **Selección de árboles:** selección múltiple con opción “No lo sé” y “Otro”. Se almacena en el campo heredado `coto.tipoBosque` como texto declarado por el usuario; aún no es una entidad normalizada ni dato cartográfico verificado.
- **Mapa principal y GPS:** mapa OSM de puntos locales; seleccionar punto temporal o usar GPS bajo demanda consulta el tiempo sin guardarlo automáticamente.
- **Meteorología:** Open-Meteo Forecast API (`https://api.open-meteo.com/v1/forecast`), actuales y datos diarios modelados de 15 días anteriores. Muestra error si falla, las coordenadas de modelo y que no es una estación en el punto. No hay persistencia/caché meteorológica offline.
- **Feedback local:** por ubicación, guarda especie indicada o no identificada, fecha/hora, estado (`ENCONTRE`, `BUSQUE_NO_ENCONTRE`, `NO_BUSQUE`), número opcional y nota. Está en Room local; no se sincroniza. Si se elimina la ubicación, la observación conserva su instantánea del nombre/coordenadas y pierde solo su FK.
- **Libro micológico:** la pestaña Setas presenta fichas estáticas curadas para nueve especies. Cada ficha separa evidencia encontrada, factores que podrían interesar a una futura evaluación, incógnitas, alcance geográfico y enlaces a fuentes. Las fichas descriptivas y artículos científicos se etiquetan de modo distinto. No son “condiciones ideales” cuantificadas ni reglas de decisión activas.
- **No implementado:** modelo/indicador por especie; ranking por condiciones; catálogo oficial de cotos; cobertura fiable de árboles/hábitat por coordenada; historial de suelo de 60 días dentro de Android; sincronización homelab; exportación CSV.

## Datos y límites de fuentes

### Meteorología

Open-Meteo devuelve datos de modelos para una cuadrícula cercana, no mediciones tomadas exactamente en el punto. El código actual solicita temperatura, humedad relativa y precipitación actual y series diarias de precipitación, temperatura y humedad relativa con `past_days=15`. La API respondió en pruebas de desarrollo con coordenadas/modelo y elevación. Se probaron directamente variables de humedad de suelo por capas y temperatura superficial con 60 días, pero todavía no se integran en Android ni se ha validado su utilidad micológica.

La API gratuita de Open-Meteo está sujeta a términos de uso no comercial, límites y atribución CC BY 4.0. Reevaluar términos/proveedor antes de publicar, monetizar o distribuir con otro propósito.

### Búsqueda y mapa

La búsqueda es Nominatim sobre la base colaborativa OSM: no equivale a inventario forestal o registro administrativo de cotos. Se ha comprobado que una búsqueda general de localidad puede dar resultado y una búsqueda de “coto micológico” puede devolver una lista vacía. Revisar la política vigente de Nominatim antes de aumentar el uso o distribuir ampliamente la app. Las teselas se sirven desde OpenStreetMap y se muestra atribución a sus colaboradores.

### Conocimiento micológico

Leer el mapa de conocimiento completo. Hay estudios de producción en pinares españoles para *Boletus edulis* y *Lactarius deliciosus*, más fichas descriptivas de sociedades micológicas para varias especies. Eso no permite deducir probabilidades o umbrales nacionales para cualquier coordenada. En especial, las fuentes por *Macrolepiota procera*, *Boletus pinophilus*, *Amanita caesarea* y las fichas complementarias son principalmente cualitativas.

## Arquitectura y archivos importantes

- `app/src/main/java/com/setometro/app/ui/screens/`: pantallas Compose (incluye `AddCotoScreen.kt`, `DetalleCotoScreen.kt`, `MapaScreen.kt`).
- `ui/viewmodel/`: estado y acciones de pantalla.
- `data/api/`: Retrofit para Open-Meteo y Nominatim.
- `data/repository/`: validación y mapeo de respuestas API/Room.
- `data/entity/`, `data/dao/`, `data/database/`: Room; base actual versión 5. Las migraciones son explícitas, no introducir destructive migration.
- `di/NetworkModule.kt`, `di/DatabaseModule.kt`: configuración Hilt de red y Room.
- `app/src/test/`: tests unitarios actuales. Hay test de `WeatherRepository`; la cobertura de Nominatim, UI y migraciones Room debe ampliarse.
- `.github/copilot-instructions.md`: salvaguardas de desarrollo del repositorio.

Flujo habitual: `Compose screen → ViewModel/StateFlow → Repository → API o DAO/Room`. No hacer llamadas de red ni persistencia directamente desde composables.

## Estado de validación al preparar este paquete

- `testDebugUnitTest`: correcto en la última validación.
- `assembleDebug`: correcto; el APK de desarrollo se instaló y arrancó en el emulador.
- La búsqueda de Almendralejo se realizó desde la pantalla de alta en el emulador y devolvió un resultado. No se guardó un punto de prueba.
- No se ha hecho aún un recorrido UI completo guardando y reabriendo un nuevo punto con árboles.
- No hay pruebas instrumentadas/UI de los flujos nuevos, ni test automatizado de migración.
- El proyecto local original está en una ruta Windows con `ó`. En el entorno de desarrollo hizo falta mapearlo temporalmente a una letra ASCII (`subst W:`) para ejecutar Gradle. El archivo `local.properties`, que contiene rutas locales del Android SDK, se excluye del paquete.

## Tareas ordenadas por dependencias

1. Completar y auditar bibliografía por especie, registrando citas completas, región, hospedador, fase biológica, periodo, método, efecto e incertidumbre.
2. Auditar fuentes de clima/suelo a escala puntual: cobertura, resolución, historial, licencia, antigüedad y limitaciones. Decidir si integrar suelo/60 días.
3. Investigar datos oficiales autonómicos/locales de cotos, permisos y vigencia; no presentar listados parciales como nacionales.
4. Mejorar el modelo de hábitat: normalizar árboles declarados y evaluar fuentes oficiales (SIOSE/MFE/IFN u otras) con fecha/resolución/licencia. Conservar siempre procedencia y confianza.
5. Diseñar una evaluación inicial **por especie y región** solo cuando la evidencia y variables permitan una orientación defendible. Explicar factores favorables/desfavorables/desconocidos, fuente, periodo, cobertura y datos faltantes; no extrapolar sin justificación.
6. Usar el feedback de campo para validación con esfuerzo de búsqueda, estación, especie y región. No tratar “busqué y no encontré” como ausencia cierta.
7. Después de validar una evaluación comparable, ordenar ubicaciones solo entre puntos con misma especie/criterio y datos vigentes. Mostrar datos insuficientes explícitamente.
8. Homelab y CSV quedan como futuras opciones, no requisitos del MVP.

## Qué pedir a la IA revisora

Revisar el ZIP completo y responder **primero con análisis, no modificar ni reescribir código todavía**, salvo que se le pida expresamente:

1. Resumir qué hace realmente el código y contrastarlo con este documento; señalar cualquier discrepancia, en especial que la función principal —estimar por especie si las condiciones del punto son compatibles con fructificación— aún no está implementada.
2. **Prioridad máxima: investigar la evidencia micológica y ecológica necesaria para esa función.** Entregar el informe y la matriz descritos en «Prioridad absoluta», con fuentes primarias, resultados concretos y límites por especie/región. Explicar qué estimaciones son defendibles hoy, cuáles no, qué variables/datos faltan y cómo se validaría la fiabilidad. No empezar por el rediseño visual ni por extras.
3. Detectar errores funcionales, de arquitectura, privacidad, accesibilidad, UX, resiliencia y calidad de datos. Distinguir bugs demostrables de recomendaciones/opiniones; la funcionalidad micológica y la calidad de sus datos tienen precedencia sobre asuntos secundarios.
4. Comprobar la adecuación de las integraciones y licencias actuales (Open-Meteo, Nominatim/OSM), consultando fuentes oficiales vigentes y citándolas; indicar riesgos de uso/distribución.
5. Evaluar si la selección de punto, el guardado de árboles y la búsqueda OSM son claros y correctos; proponer pruebas que demuestren el flujo end-to-end.
6. Revisar Room v5/migraciones, integridad del feedback local, comportamiento al borrar una ubicación y cobertura de tests.
7. Proponer una hoja de ruta pequeña y ordenada por dependencias. Para cada mejora: problema, impacto, evidencia, coste/riesgo, archivos probables y criterios verificables de aceptación. La investigación, implementación y validación de la evaluación micológica deben ocupar el primer lugar; interfaz, mapas, cotos, CSV y otros extras son secundarios mientras la estimación no sea fiable.
8. Decir qué NO se debe implementar aún por falta de evidencia/datos. No proponer scores o porcentajes artificiales ni asumir un catálogo nacional de cotos.
9. Auditar también el diseño visual de toda la app y proponer una mejora integral: jerarquía, navegación, consistencia, identidad visual, legibilidad, accesibilidad, pantallas de mapa/alta/detalle/libro y estados vacíos/carga/error/sin conexión. Es una línea secundaria: no debe presentarse como solución al objetivo central ni desplazar la investigación. Mantener visibles fuentes, incertidumbre y ausencia de datos; no usar colores, gráficos ni etiquetas que aparenten una probabilidad o certeza que el modelo no tiene. No realizar un rediseño masivo sin aprobación del usuario.

Al recibir recomendaciones, el propietario del proyecto decidirá cuáles se implementan. No introducir cambios masivos, migraciones destructivas, telemetría, envío de coordenadas/feedback ni nuevas dependencias sin explicar necesidad y consecuencias.
