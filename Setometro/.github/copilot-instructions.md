# Instrucciones de desarrollo para Setómetro

Estas instrucciones se aplican al trabajo sobre este proyecto. La fuente de verdad es el código y las pruebas actuales; no se debe asumir que una función descrita en documentación ya está implementada.

## Principio del producto: datos honestos

- Nunca generar, interpolar ni presentar como reales datos meteorológicos, de hábitat, cotos, hallazgos o probabilidades que no vengan de una fuente identificada. No usar datos sintéticos ni valores de ejemplo como fallback en la aplicación. Los fixtures y fakes se permiten exclusivamente en pruebas y deben estar claramente nombrados como tales.
- Si una fuente no devuelve datos, si falla la red o si una variable no está disponible para el punto/periodo, conservar y mostrar el motivo. No convertir errores, listas vacías o datos obsoletos en un resultado con apariencia de éxito.
- Cada dato remoto debe conservar suficiente procedencia para explicar su fuente, periodo/fecha y, si la fuente lo informa, estación, resolución o precisión. Distinguir explícitamente observación, histórico, pronóstico y dato guardado en caché, indicando la antigüedad de este último.
- Analizar las coordenadas concretas elegidas por la persona (ubicación actual, punto del mapa o coto), sin sustituirlas por promedios de cotos o valores de otra región. Solicitar ubicación solo cuando haga falta y ofrecer una alternativa manual.
- No confundir clima favorable con presencia de setas. Mostrar por separado condiciones meteorológicas, compatibilidad del hábitat y observaciones registradas. La falta de observaciones no demuestra ausencia; lluvia, temperatura o altitud por sí solas no validan un hábitat.
- No mostrar porcentajes de presencia ni clasificar una zona como setera si no existe un modelo validado y evidencia adecuada para la especie y zona. Cuando falte evidencia, expresar “sin datos” o “no se puede evaluar” y explicar por qué.
- Validar licencias, cobertura, resolución y limitaciones de cualquier API o conjunto de datos antes de incorporarlo. No exponer claves, tokens ni credenciales en el código, logs, capturas o documentación.

## Arquitectura y cambios

- Mantener el patrón existente: pantalla Compose → ViewModel/estado de UI → Repository → fuente de datos local o remota. Las reglas de negocio no pertenecen a los composables ni a los clientes HTTP.
- Usar flujo unidireccional de datos: la UI emite acciones, el ViewModel coordina y expone estado observable e inmutable; Compose representa ese estado. Modelar explícitamente carga, contenido, vacío y error cuando aplique.
- Reutilizar las capas, dependencias, convenciones y estructura actuales. Evitar añadir módulos, abstracciones, dependencias o configurabilidad sin una necesidad concreta.
- Mantener Retrofit/servicios para transporte de red y repositorios para coordinar API, persistencia y reglas de caché. Validar y mapear las respuestas antes de guardarlas; no guardar registros derivados de información ausente como si fueran observaciones.
- Persistir datos locales con Room mediante entidades y DAO existentes. Ante cambios de esquema, incrementar la versión y proporcionar migraciones que preserven los datos; no introducir migraciones destructivas para solucionar un cambio de modelo.
- Mantener las operaciones de red y base de datos fuera del hilo principal. Respetar ciclo de vida y cancelación de coroutines.
- Informar fallos de manera explícita y accionable. No capturar excepciones para ocultarlas, devolver resultados de éxito vacíos ni sustituir datos reales por valores hardcodeados.
- Para ubicación, pedir permisos en contexto, manejar denegación y ubicación imprecisa, y dejar claro qué coordenadas se están analizando.
- Mantener accesibilidad, textos comprensibles en español y estados vacíos que expliquen el siguiente paso posible.

## Pruebas y verificación

- Añadir pruebas para cada cambio de comportamiento. Probar especialmente reglas de evaluación con casos límite y datos faltantes, errores de API, caché obsoleta, selección de coordenadas y persistencia.
- Las pruebas de negocio deben ser deterministas y usar fixtures/fakes explícitos; nunca requieren credenciales ni llamadas reales a internet.
- Cubrir UI/navegación de Compose cuando el comportamiento visible o los permisos cambien. Probar los estados de carga, éxito, vacío, error y ausencia de datos.
- Ejecutar la prueba más específica pertinente y compilar la variante afectada. Ejecutar pruebas instrumentadas si se modifica la integración Android, permisos, Room o UI.
- No afirmar que una compilación o prueba pasó si no se ejecutó. Informar claramente de los comandos ejecutados y los bloqueos del entorno.

## Ciclo obligatorio de autoauditoría antes de entregar

No dar por terminada ni entregar una tarea inmediatamente después de editar. Completar siempre este ciclo, adaptando la profundidad al alcance:

1. **Contrastar el resultado con la petición:** enumerar mentalmente los requisitos concretos y confirmar dónde quedó implementado cada uno. Comprobar tanto el flujo principal como estados vacíos, errores y datos ausentes que afecte el cambio.
2. **Releer el cambio completo:** inspeccionar el diff final y el contexto de cada archivo modificado. Buscar errores de lógica/tipos, código muerto, duplicación, texto contradictorio, regresiones en comportamiento existente, cambios ajenos, secretos y efectos sobre privacidad/licencias.
3. **Validar con evidencia:** ejecutar las pruebas más específicas y el build pertinente. Para cambios visuales, abrir la pantalla en emulador/dispositivo y revisar la interfaz renderizada (incluyendo contenido expandido, scroll y estados relevantes), no asumir que compilar equivale a que se vea bien. Para persistencia, permisos, navegación o red, verificar además el flujo de ejecución afectado cuando el entorno lo permita.
4. **Corregir y repetir:** si una comprobación falla o revela un defecto, arreglarlo y volver a ejecutar la validación afectada. Revisar otra vez el diff después de las correcciones. Si alguna comprobación necesaria no puede hacerse, no presentar el resultado como plenamente verificado: especificar qué quedó sin comprobar y por qué.
5. **Cerrar con un resumen honesto:** indicar qué se cambió, qué pruebas/build/validaciones visuales se ejecutaron y sus resultados; separar lo verificado de lo pendiente. No atribuir al emulador, a una prueba o a una fuente un resultado que no se observó.

## Referencia de prácticas Android

Estas reglas son una síntesis original adaptada a Setómetro, no una copia de instrucciones de terceros. Se inspiran en los patrones que documenta y ejemplifica **Now in Android**, un proyecto Android de Google escrito en Kotlin y Jetpack Compose, con más de 21.000 estrellas en GitHub al consultar la referencia:

- Repositorio: [android/nowinandroid](https://github.com/android/nowinandroid)
- Arquitectura y comandos descritos por el proyecto: [AGENTS.md](https://github.com/android/nowinandroid/blob/main/AGENTS.md)
- Implementación y documentación del proyecto: [README.md](https://github.com/android/nowinandroid/blob/main/README.md)

La referencia muestra, entre otros, UI Compose, estado unidireccional con Coroutines/Flow y ViewModels, repositorios, Room para datos locales, Retrofit/OkHttp para red, Hilt y WorkManager. Setómetro debe adoptar los principios útiles sin copiar la escala modular o complejidad del ejemplo cuando no la necesite.

## Contexto de este repositorio

- Aplicación Android nativa en Kotlin y Jetpack Compose; módulo Gradle principal `app`.
- Código fuente: `app/src/main/java/com/setometro/app/`.
- Capas existentes: `ui/screens`, `ui/viewmodel`, `data/repository`, `data/dao`, `data/entity`, `data/api`, `data/calculator`, `data/preferences`, `di` y `worker`.
- El detalle consulta Open-Meteo Forecast por las coordenadas de la ubicación guardada y solicita condiciones actuales más 15 días previos. Los datos son salidas de modelo para una cuadrícula asociada a las coordenadas, no mediciones de sensor en el punto; mantener visible esa limitación, fuente, fecha y coordenadas de modelo.
- Open-Meteo Free se usa solo para este MVP personal/no comercial y requiere atribución CC BY 4.0. Reevaluar proveedor/licencia antes de cualquier monetización o distribución comercial.
- La base v4 limpia mediante migración el historial meteorológico y las probabilidades sintéticas heredadas, el catálogo de ejemplo sin fuentes y los puntos de demostración conocidos. Las coordenadas guardan procedencia y precisión GPS opcional; las ubicaciones antiguas se migran como entrada manual. No volver a sembrar datos de muestra.
- El mapa solo representa ubicaciones guardadas por el usuario; no es un mapa de cotos oficiales ni de presencia de setas.
- No hay porcentajes de probabilidad de presencia en la interfaz. No volver a mostrarlos hasta contar con un modelo validado y datos micológicos suficientes por especie/región.
- Mantener el [mapa de conocimiento micológico](../MAPA_CONOCIMIENTO_MICOLOGICO.md) actualizado al añadir especies o reglas: citar fuente, región, periodo, fase biológica, incertidumbre y límites; no completar huecos con calendarios populares o umbrales inventados.
- No ordenar ubicaciones como “más favorables” con lluvia o una puntuación arbitraria. La ordenación de “Mis Cotos” depende de una evaluación comparable, por especie y explicable sustentada en hábitat y meteorología.
- El feedback de campo debe separar “encontré”, “busqué y no encontré” y “no busqué”. La primera versión se almacena localmente; no enviar coordenadas ni observaciones a un servidor sin consentimiento explícito.
- El Gradle Wrapper 8.13 ya está reparado y versionado. En Windows, la ruta actual contiene `ó`, lo que puede activar el bloqueo del Android Gradle Plugin; compilar desde una ruta ASCII temporal o mover el proyecto a una ruta ASCII. No ocultar el bloqueo desactivando la comprobación experimental.
