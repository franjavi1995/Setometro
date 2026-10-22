# Mapa de conocimiento micológico y requisitos de datos

**Estado:** documento de investigación inicial.  
**Fecha de revisión:** 22 de octubre de 2026.  
**Objetivo de producto:** que Setómetro traduzca datos fiables de un punto y conocimiento micológico trazable a una orientación comprensible sobre las condiciones de fructificación de especies silvestres, sin exigir al usuario que conozca sus requisitos.

## Reglas de evidencia

Este documento separa lo demostrado de lo pendiente. No convierte calendarios populares, observaciones aisladas ni resultados de una comarca en umbrales para toda España.

- Una asociación entre variables y cosecha no demuestra por sí sola causalidad ni presencia futura en una coordenada.
- Los hongos ectomicorrícicos dependen de sus árboles hospedadores y de condiciones del suelo y del bosque. Una serie de lluvia y temperatura del aire no describe por sí sola ese hábitat.
- El micelio, la iniciación del primordio, el desarrollo del cuerpo fructífero y la fecha en que una persona encuentra una seta son fenómenos relacionados, pero no intercambiables.
- «No encontré», «no busqué» y «busqué y no encontré» deben ser observaciones diferentes. Ninguna observación negativa aislada prueba ausencia.
- Cuando aquí se dice **no localizado**, significa que la búsqueda documentada no encontró una fuente suficiente para sostener la afirmación; no demuestra que la bibliografía no exista.
- Los requisitos numéricos, ventanas temporales o clases de aptitud solo podrán publicarse con la cita, el área, el periodo, el método y la incertidumbre de la fuente que los sustenta.

## Resumen por especie

| Especie | Evidencia que se puede afirmar con las fuentes revisadas | Lo que aún no se puede convertir en regla de la aplicación |
|---|---|---|
| *Macrolepiota procera* (apagador/parasol) | Una ficha de la Sociedad Micológica Barakaldo la describe en bosques y claros y sitúa la fructificación de finales de verano a mediados de otoño. No asigna un árbol concreto. | Es una descripción micológica, no un estudio fenológico de parcelas/años. No cuantifica lluvia, temperatura, humedad, suelo ni altitud, ni delimita la ventana por regiones españolas. |
| *Boletus pinophilus* (sin. usado en algunas fuentes: *B. pinicola*) | Una ficha de la Sociedad Micológica Barakaldo describe fructificación desde primavera a mediados de otoño, en bosques de coníferas (*Pinus*, *Abies*) y frondosas (*Fagus*, *Castanea*, *Quercus*), suelos ácidos y zonas de media montaña/montaña. | La ficha no aporta localidades, altitud numérica ni una serie de muestreo. No transferirle los resultados de *B. edulis* ni inferir requisitos cuantitativos/regionales. |
| *Boletus edulis* (hongo/calabaza) | Hay estudios españoles en pinares que investigan su producción de carpóforos, el micelio, la gestión forestal y el clima. En series largas de pinares mediterráneos del centro peninsular se modeló el rendimiento junto con productividad forestal y clima; la humedad del suelo estimada por teledetección resultó informativa, y el estudio incluye *B. edulis*. | La evidencia citada es de pinares y sitios estudiados, no un umbral de milímetros, temperatura o altitud para España. Tampoco permite declarar presente una seta en el punto seleccionado. El detalle de hospedadores por especie/región y fenología aplicable al MVP debe verificarse en las fuentes completas antes de codificarlo. |
| *Lactarius deliciosus* (níscalo/rovellón) | Un estudio de pinares de *Pinus pinaster* del centro de España encontró asociaciones entre precipitación de finales de verano/comienzos de otoño y producción/emergencia del conjunto de setas comestibles estudiado; también informó de relación positiva entre acidez del suelo y rendimiento de *L. deliciosus* en ese ámbito. Otro estudio español examinó dinámicas estacionales del micelio de *L. deliciosus* y *B. edulis* en pinares. | «Finales de verano/comienzos de otoño» no define un calendario universal, una dosis de lluvia ni el retardo exacto de fructificación. El resultado de acidez no aporta aquí un valor de pH transferible a otros suelos. No convertirlo en una puntuación por sí solo. |
| *Amanita caesarea* (oronja/amanita de los césares) | Una ficha de la Sociedad Micológica Barakaldo la califica como termófila, ligada a bosques de frondosas y principalmente a encinas, robles y castaños; describe su aparición en verano y principios de otoño. | La ficha no fija temperatura, lluvia, altitud, localidad de muestreo ni una fenología regional cuantificada. Los descriptores no justifican un umbral térmico en la app. |

Las fichas de la Sociedad Micológica Barakaldo son referencias micológicas descriptivas útiles para plantear hipótesis de hábitat/estación. No son series experimentales; no publican en las páginas citadas un muestreo que permita convertir esos descriptores en umbrales para toda España.

### Especies populares con ficha descriptiva localizada y evidencia aún insuficiente para reglas

- ***Cantharellus cibarius* (rebozuelo):** la ficha de la Sociedad Micológica Barakaldo indica primavera, verano y otoño; bosques de coníferas o frondosas y suelos ácidos. La ficha no delimita estación por región ni acredita una serie local de fructificación.
- ***Hygrophorus marzuolus* (marzuelo):** la ficha indica de mediados de invierno a mediados de primavera, con máximo descrito en febrero-abril; bosques de coníferas, frondosas y mixtos, suelos ácidos y/o arcillosos. Enumera varias regiones de la península, pero no es un mapa de registros ni una estimación de abundancia.
- ***Amanita ponderosa* (gurumelo):** una ficha firmada de amanitacesarea.com la asocia con *Quercus* y jaras en bosque mediterráneo y describe preferencia primaveral. Se conserva como fuente micológica no institucional y descriptiva, de menor nivel de evidencia; no usarla para reglas sin corroboración independiente.
- ***Pleurotus eryngii* (seta de cardo):** una ficha firmada de amanitacesarea.com describe crecimiento ligado a raíces/restos de cardos, sobre todo *Eryngium campestre*, praderas o claros y predominio otoñal con posible primavera. No es una asociación con un árbol; la ficha no cuantifica condiciones.

Se propone investigar después ***Tuber melanosporum*** (trufa negra). Para esta especie se encontró una fuente sobre comunidades asociadas a plantaciones truferas de robles del norte de España. Ese trabajo no valida por sí solo una regla de fructificación meteorológica ni una distribución silvestre generalizable.

**Fuente de menor nivel, pendiente de corroborar:** Jorge Jiménez Santos, fichas de [*Amanita ponderosa*](https://www.amanitacesarea.com/amanita-ponderosa.html) y [*Pleurotus eryngii*](https://www.amanitacesarea.com/pleurotus-eryngii.html). Son fichas divulgativas firmadas, no trabajos de muestreo ni fuentes institucionales.

### Fichas micológicas descriptivas

- Sociedad Micológica Barakaldo, [*Macrolepiota procera*](https://micologica-barakaldo.org/macrolepiota-procera/) (publicada 2021).
- Sociedad Micológica Barakaldo, [*Boletus pinophilus*](https://micologica-barakaldo.org/boletus-pinophilus/) (publicada 2019, modificada 2020).
- Sociedad Micológica Barakaldo, [*Amanita caesarea*](https://micologica-barakaldo.org/amanita-caesarea/) (publicada 2022, modificada 2024).
- Sociedad Micológica Barakaldo, [*Cantharellus cibarius*](https://micologica-barakaldo.org/cantharellus-cibarius/) (publicada 2021, modificada 2025).
- Sociedad Micológica Barakaldo, [*Hygrophorus marzuolus*](https://micologica-barakaldo.org/hygrophorus-marzuolus/) (publicada 2020).

Estas fichas son fuente para vocabulario, identificación descriptiva, hábitat narrativo y estación orientativa. La aplicación deberá guardar su nivel de evidencia distinto del de artículos científicos y no presentar sus ventanas como una climatología cuantitativa o predicción.

## Evidencia científica localizada

### Producción en pinares del centro de España

Taye et al. estudiaron durante 17 años la producción de setas comestibles en pinares de *Pinus pinaster* del centro de España y relacionaron producción con condiciones meteorológicas y características del sitio. El artículo informa de que la precipitación a finales de verano/comienzos de otoño favoreció la emergencia y producción del conjunto estudiado, con diferencias entre especies; para *L. deliciosus* informa una relación positiva entre acidez del suelo y rendimiento.

**Alcance:** pinares de *P. pinaster* y lugares incluidos por el estudio. No es un calendario ni un umbral nacional. Para extraer umbrales, coeficientes o retardos por especie habría que revisar el artículo completo y sus métodos/datos, no el resumen.

> Taye, Z. M.; Martínez-Peña, F.; Bonet, J. A.; Martínez de Aragón, J.; de-Miguel, S. (2016). “Meteorological conditions and site characteristics driving edible mushroom production in *Pinus pinaster* forests of Central Spain”. *Fungal Ecology*, 23, 30–41. [https://doi.org/10.1016/j.funeco.2016.05.008](https://doi.org/10.1016/j.funeco.2016.05.008)

### Productividad y clima en pinares mediterráneos

Olano et al. analizaron series largas de rendimiento de pinares de *P. pinaster* y *P. sylvestris* en el centro de España, incorporando variables climáticas y de productividad. El trabajo incluye *B. edulis* y *L. deliciosus*. La humedad del suelo estimada mediante teledetección aportó capacidad predictiva comparable a la precipitación en los sitios estudiados; el NDVI del año previo también se relacionó con la productividad. Son relaciones de modelos locales y no indican por sí mismas una probabilidad calibrada para una ubicación arbitraria.

**Alcance:** rodales, periodo, variables y método concretos del artículo. No se deben reutilizar sus correlaciones como porcentaje de presencia en otros bosques, ni tratar la humedad por satélite como medición de suelo en el punto.

> Olano, J. M. et al. (2020). “Primary productivity and climate control mushroom yields in Mediterranean pine forests”. *Agricultural and Forest Meteorology*, 288–289, 108015. [https://doi.org/10.1016/j.agrformet.2020.108015](https://doi.org/10.1016/j.agrformet.2020.108015)

### *Boletus edulis*: clima y gestión forestal

Parladé et al. investigaron la dinámica del micelio y la producción de carpóforos de *B. edulis* en relación con variables climáticas y gestión forestal. Es una referencia directa para ampliar la ficha de esta especie. No se incorporan aquí valores de los que no se haya verificado el texto completo.

> Parladé, J.; Martínez-Peña, F.; Pera, J. (2017). “Effects of forest management and climatic variables on the mycelium dynamics and sporocarp production of the ectomycorrhizal fungus *Boletus edulis*”. *Forest Ecology and Management*, 390, 73–79. [https://doi.org/10.1016/j.foreco.2017.01.025](https://doi.org/10.1016/j.foreco.2017.01.025)

### Micelio y fructificación de *B. edulis* y *L. deliciosus*

De la Varga et al. estudiaron la dinámica estacional del micelio extrarradical de ambas especies en pinares del centro de España. El trabajo sirve para distinguir dinámica de micelio de aparición de carpóforos: una no equivale directamente a la otra.

> De la Varga, H.; Águeda, B.; Ágreda, T.; Martínez-Peña, F.; Parladé, J.; Pera, J. (2013). “Seasonal dynamics of *Boletus edulis* and *Lactarius deliciosus* extraradical mycelium in pine forests of central Spain”. *Mycorrhiza*, 23, 391–402. [https://doi.org/10.1007/s00572-013-0481-3](https://doi.org/10.1007/s00572-013-0481-3)

Liu et al. estudiaron la correlación entre micelio extrarradical de *L. deliciosus*, productividad de cuerpos fructíferos y aclareo forestal. No debe usarse el título como prueba de un umbral climático.

> Liu, B. et al. (2016). “*Lactarius deliciosus* Fr. soil extraradical mycelium correlates with stand fruitbody productivity and is increased by forest thinning”. *Forest Ecology and Management*, 380, 196–201. [https://doi.org/10.1016/j.foreco.2016.08.053](https://doi.org/10.1016/j.foreco.2016.08.053)

### Ecología de plantaciones de *Tuber melanosporum*

Herrero de Aza et al. caracterizaron comunidades fúngicas y bacterianas de plantaciones truferas de robles en el norte de España. Es una fuente contextual para las plantaciones estudiadas, no una regla de lluvia/temperatura ni evidencia de aptitud de cualquier bosque.

> Herrero de Aza, C. et al. (2022). “Fungal and Bacterial Communities in *Tuber melanosporum* Plantations from Northern Spain”. *Forests*, 13(3), 385. [https://doi.org/10.3390/f13030385](https://doi.org/10.3390/f13030385)

## Datos disponibles por punto: situación actual

### Ya disponibles en Setómetro

La app obtiene por coordenadas, a través de Open-Meteo Forecast, condiciones actuales de temperatura del aire, humedad relativa y precipitación, además de series diarias de precipitación acumulada, temperatura media/mínima/máxima y humedad relativa media. La pantalla usa 15 días anteriores. Son datos de modelo para una cuadrícula asociada a las coordenadas, no mediciones de una estación instalada en el punto.

Open-Meteo también devolvió, en una consulta de prueba a las coordenadas de Almendralejo, series horarias modeladas de temperatura de superficie del suelo y humedad volumétrica de capas de 0–1, 1–3 y 3–9 cm. La API admitió solicitar 60 días de histórico para esa prueba. **Estos campos no están integrados en la app**. La respuesta de una prueba no garantiza cobertura, profundidad ni calidad apropiadas en todos los lugares/fechas. Tampoco acredita humedad del suelo a la profundidad del micelio.

Fuente: [documentación de Open-Meteo Forecast](https://open-meteo.com/en/docs) y [Historical Weather API](https://open-meteo.com/en/docs/historical-weather-api). Antes de integrar nuevas variables: comprobar cobertura por modelo/fecha, licencias, resolución, antigüedad y qué significa exactamente cada variable. Mantener la atribución y el límite de uso no comercial del plan gratuito.

La altitud se presenta como altitud modelada comunicada por Open-Meteo. Un valor introducido en una ubicación guardada puede tener otra procedencia y no debe combinarse con aquél sin etiquetarla.

### Hábitat, árboles y suelo

Un servicio único que devuelva inventario micológico y composición de árboles exacta para cualquier coordenada **no está demostrado por esta investigación**. Hay fuentes cartográficas candidatas de ocupación forestal/suelo, pero sus clases, fechas, resolución y acceso tienen que validarse antes de usarlas:

- **SIOSE (Sistema de Información sobre Ocupación del Suelo de España):** iniciativa coordinada entre administraciones para una base de ocupación del suelo de España. Puede ayudar a clasificar cobertura/hábitat, pero hay que verificar si el producto y servicio disponible distinguen las especies arbóreas requeridas y con qué resolución/fecha. [Portal SIOSE](https://www.siose.es/)
- **CORINE Land Cover / Copernicus:** cartografía paneuropea con 44 clases temáticas y actualizaciones por ciclos; ofrece contexto de cobertura forestal, pero una clase amplia de bosque no identifica necesariamente las especies de árboles de una parcela. La página de producto consultada menciona como actualización más reciente la edición de 2018. [Copernicus CORINE Land Cover](https://land.copernicus.eu/en/products/corine-land-cover)
- **Mapa Forestal de España (MFE) e Inventario Forestal Nacional (IFN):** fuentes estatales candidatas a investigar para tipos de bosque/composición. Aún hay que localizar y validar portal/API/capas descargables, actualidad, resolución y condiciones de reutilización; este documento no presupone que exista un endpoint de consulta por coordenada listo para integrar.
- **Suelo:** textura, pH, materia orgánica, drenaje y humedad observada podrían ser relevantes según especie. La fuente, escala, fecha y precisión de cada producto deben verificarse. Una capa nacional modelada no equivale a una muestra del suelo del sitio.

### Cotos micológicos y gestión

No se ha verificado en esta revisión un catálogo/API nacional único, completo y actualizable de todos los cotos micológicos españoles. La existencia de permisos, acotados, temporadas y regulación puede depender de comunidad autónoma, provincia, municipio o entidad gestora. No mostrar un punto como coto oficial sin una fuente administrativa que lo respalde.

Como ejemplo regional se localizó [Micocyl](https://www.micocyl.es/), cuyo servicio/app ayuda a saber si el usuario está dentro de montes regulados por Micocyl. Esto demuestra la existencia de una solución de ámbito regional, no un inventario nacional ni una API pública con licencia y cobertura confirmadas. Hay que contactar con el gestor para comprobar si ofrece datos reutilizables y sus condiciones.

Retos para el catálogo:

1. Identificar para cada administración competente el registro oficial, datos abiertos, servicio cartográfico o listado vigente.
2. Registrar entidad responsable, fuente, fecha de actualización, geometría, vigencia, condiciones/permisos y licencia de reutilización.
3. Distinguir “coto con regulación micológica” de “hábitat donde una especie podría vivir”. Un coto no certifica árboles, fructificación ni presencia.
4. Si no se encuentra cobertura fiable para una zona, permitir indicar “no sé / no consta” en vez de inferir que no hay coto.

### Preguntar al usuario por los árboles

Como alternativa inmediata a un mapa forestal insuficiente, al consultar un punto la app puede preguntar qué árboles observa la persona. Requisitos de diseño:

- Pregunta opcional y con opción **“No lo sé”**.
- Permitir más de un tipo de árbol y guardar la respuesta como **declaración del usuario**, no como dato verificado.
- Separar árbol observado, tipo de bosque seleccionado manualmente, clasificación obtenida de una fuente cartográfica y fuente/fecha de esa clasificación.
- No convertir una respuesta desconocida en ausencia del árbol.
- Explicar brevemente para qué sirve la pregunta, sin presentar nombres de árboles como identificación de setas ni asegurar aptitud.

La altitud no sustituye a la información de hospedador, cobertura vegetal, suelo, gestión ni microclima.

## Requisitos para una evaluación que ordene “Mis Cotos”

El usuario quiere que “Mis Cotos”/sus ubicaciones de control muestre primero las que tengan condiciones más favorables. No se debe ordenar todavía por precipitación reciente ni por una puntuación inventada: una misma condición puede tener significados distintos por especie, árbol hospedador, suelo, estación y región.

Diseño funcional solicitado:

1. Definir primero el conjunto de especies de interés (selección del usuario o especies compatibles sustentadas por el hábitat).
2. Calcular una **evaluación de condiciones por especie**, separada de la presencia real, con factores y periodo explicados.
3. Usar estados legibles y no probabilísticos mientras no exista calibración: por ejemplo, “condiciones poco favorables según los datos disponibles”, “señales compatibles, con incertidumbre” o “datos insuficientes”. Definir los estados con la bibliografía, no en este documento.
4. Mostrar junto al estado los factores que lo explican, datos ausentes, fuente, periodo y alcance regional de la regla aplicada.
5. Ordenar las ubicaciones monitorizadas por esa evaluación solo si todas comparan la misma especie/criterio y hay datos suficientes. Si se mezclan especies, separar listas/filtros o aplicar una regla de agregación validada; no mezclar puntuaciones incompatibles.
6. Sin red o con datos insuficientes, comunicar el estado y el motivo; no conservar una clasificación antigua como si fuese actual sin indicarlo.
7. Validar la utilidad con feedback de campo y evaluar después calibración por región/especie. No llamar “probabilidad de encontrar” a una clase heurística.

## Feedback: validación de campo y futuro servidor

**Primera versión:** guardar el feedback solo en el dispositivo. Registrar como mínimo:

- punto monitorizado y coordenada disponible, con origen/precisión cuando exista;
- especie (taxón seleccionado de catálogo verificado; permitir “no identificada”);
- resultado independiente: “encontré”, “busqué y no encontré”, “no busqué”;
- fecha/hora de observación y opcionalmente número aproximado de ejemplares/foto/nota;
- quién o qué fuente añadió el registro, sin transmitirlo fuera del dispositivo;
- consentimiento explícito si en el futuro se decide enviar datos.

Un “no encontré” es una observación de búsqueda, no una prueba de ausencia. Las observaciones oportunistas también tienen sesgo de esfuerzo, temporada, accesibilidad y experiencia de identificación. Los resúmenes deben exponer esos límites.

**Futuro:** evaluar sincronización optativa con un servidor en el homelab del usuario. Antes: definir autenticación, copias de seguridad, cifrado en tránsito/almacenamiento, exportación y borrado, minimización de coordenadas sensibles, sincronización/conflictos, funcionamiento offline, acceso remoto seguro y tratamiento de datos. No depender de un servicio doméstico para que funcione la app local ni exponerlo directamente a Internet sin una arquitectura segura.

## Retos priorizados para la siguiente IA/equipo

1. Completar una revisión bibliográfica por especie priorizando España y la península, monografías micológicas identificables y estudios revisados por pares. Capturar citas completas y fragmentos/páginas que sustenten cada afirmación.
2. Ampliar fichas de *M. procera*, *B. pinophilus*, *B. edulis*, *L. deliciosus* y *A. caesarea*. Añadir especies populares solo cuando exista evidencia y fuentes suficientes.
3. Para cada estudio, extraer taxón validado, fase biológica estudiada, hospedador, bosque/suelo, región, fechas, duración, variable, efecto, método, incertidumbre y límites. No transcribir ni inventar un “umbral ideal” si el artículo no lo ofrece.
4. Auditar variables meteorológicas: historial diario ≥60 días cuando esté disponible, datos horarios, humedad/temperatura del suelo; revisar resolución, cobertura, procedencia, restricciones y uso no comercial de Open-Meteo.
5. Investigar e integrar hábitat por coordenada desde SIOSE/MFE/IFN u otra fuente oficial, con metadatos de fecha/resolución/licencia. Comparar clasificación remota con inspección manual de árbol.
6. Determinar si se puede mantener un catálogo fiable de cotos mediante las administraciones competentes; no inventar ni presentar listados parciales como cobertura nacional.
7. Diseñar la pregunta opcional de árboles al seleccionar un punto y cómo editar/refrescar la respuesta, conservándola como dato declarado.
8. Implementar feedback local con los tres estados distintos; diseñar un esquema migrable para sincronización optativa futura con homelab, todavía sin servidor.
9. Definir y validar la evaluación explicable por especie. Solo después usarla para ordenar “Mis Cotos”, con comparaciones homogéneas, frescura de datos y estado “insuficiente” explícito.
10. Validar contra observaciones de campo registradas con esfuerzo de búsqueda, fechas, especie y localización. No publicar probabilidad estadística hasta que la validación y calibración lo permitan.

## Fuentes de datos técnicas consultadas

- Open-Meteo, [Forecast API](https://open-meteo.com/en/docs), [Historical Weather API](https://open-meteo.com/en/docs/historical-weather-api), [licencia/atribución](https://open-meteo.com/en/licence) y [términos](https://open-meteo.com/en/terms).
- SIOSE, [Sistema de Información sobre Ocupación del Suelo de España](https://www.siose.es/).
- Copernicus Land Monitoring Service, [CORINE Land Cover](https://land.copernicus.eu/en/products/corine-land-cover).

## Historial de versiones del documento

- **2026-10-22:** mapa inicial. Evidencia climática localizada principalmente para producción en pinares españoles de *B. edulis* y *L. deliciosus*; se añadieron fichas descriptivas micológicas para varias especies, con su nivel de evidencia explícito. Añadidos retos de cobertura de datos, cotos, identificación de árboles, ordenación del dashboard y feedback local/futuro homelab.
