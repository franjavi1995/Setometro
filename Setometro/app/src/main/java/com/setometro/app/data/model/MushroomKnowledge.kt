package com.setometro.app.data.model

data class MushroomKnowledgeSource(
    val title: String,
    val sourceType: String,
    val url: String
)

data class MushroomKnowledgeEntry(
    val id: String,
    val commonName: String,
    val scientificName: String,
    val evidenceLevel: String,
    val geographicScope: String,
    val knownEvidence: List<String>,
    val factorsRelevantToApp: List<String>,
    val unknowns: List<String>,
    val sources: List<MushroomKnowledgeSource>
)

val mushroomKnowledgeBook = listOf(
    MushroomKnowledgeEntry(
        id = "macrolepiota-procera",
        commonName = "Parasol o apagador",
        scientificName = "Macrolepiota procera",
        evidenceLevel = "Ficha micológica descriptiva",
        geographicScope = "La ficha no documenta una región de muestreo que permita generalizar una regla.",
        knownEvidence = listOf(
            "La ficha la describe en bosques y claros.",
            "Sitúa su fructificación descrita entre finales de verano y mediados de otoño.",
            "La ficha no asigna un árbol hospedador concreto."
        ),
        factorsRelevantToApp = listOf(
            "Hábitat descrito: bosque y claros.",
            "Estación orientativa descrita: finales de verano a mediados de otoño."
        ),
        unknowns = listOf(
            "No se aportan umbrales de lluvia, temperatura, humedad del suelo o altitud.",
            "No hay una fenología cuantificada por región ni un estudio de parcelas/años en la fuente revisada."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Macrolepiota procera — Sociedad Micológica Barakaldo",
                "Ficha micológica",
                "https://micologica-barakaldo.org/macrolepiota-procera/"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "boletus-pinophilus",
        commonName = "Boleto de pino",
        scientificName = "Boletus pinophilus (sin. usado: B. pinicola)",
        evidenceLevel = "Ficha micológica descriptiva",
        geographicScope = "La ficha no delimita localidades ni áreas de muestreo.",
        knownEvidence = listOf(
            "La ficha describe fructificación desde primavera hasta mediados de otoño.",
            "Menciona bosques de coníferas como Pinus y Abies, y frondosas como Fagus, Castanea y Quercus.",
            "Describe suelos ácidos y zonas de media montaña o montaña."
        ),
        factorsRelevantToApp = listOf(
            "Árboles/hábitat descritos: determinados géneros de coníferas y frondosas.",
            "Suelo descrito como ácido y relieve de media montaña/montaña.",
            "Estación descrita: primavera a mediados de otoño."
        ),
        unknowns = listOf(
            "No se proporcionan valores numéricos de altitud, pH, lluvia, temperatura ni humedad.",
            "No se presenta una serie de observaciones que cuantifique la fructificación por región."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Boletus pinophilus — Sociedad Micológica Barakaldo",
                "Ficha micológica",
                "https://micologica-barakaldo.org/boletus-pinophilus/"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "boletus-edulis",
        commonName = "Boletus, hongo o calabaza",
        scientificName = "Boletus edulis",
        evidenceLevel = "Estudios científicos en pinares; alcance local",
        geographicScope = "Pinares estudiados del centro de España; no representa toda España.",
        knownEvidence = listOf(
            "Hay estudios de campo españoles sobre producción de carpóforos, dinámica del micelio, clima y gestión forestal.",
            "En pinares mediterráneos estudiados, modelos de rendimiento incluyeron productividad forestal y clima; humedad del suelo estimada por teledetección y NDVI previo aportaron información.",
            "Los estudios no convierten esos resultados en presencia confirmada en una coordenada ni en una probabilidad nacional."
        ),
        factorsRelevantToApp = listOf(
            "Humedad del suelo y productividad/estado de la vegetación aparecen como variables informativas en estudios de pinares concretos.",
            "Clima y gestión forestal se investigan en relación con micelio y producción."
        ),
        unknowns = listOf(
            "Las fuentes revisadas no aportan un umbral general de milímetros, temperatura o altitud aplicable a cualquier punto de España.",
            "Debe verificarse el hospedador, estación y detalle de resultados en el texto completo antes de convertirlos en reglas por región.",
            "La humedad de suelo satelital o modelada no equivale a una medición a la profundidad del micelio."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Taye et al. (2016), Meteorological conditions and site characteristics driving edible mushroom production in Pinus pinaster forests of Central Spain",
                "Estudio científico, 17 años",
                "https://doi.org/10.1016/j.funeco.2016.05.008"
            ),
            MushroomKnowledgeSource(
                "Olano et al. (2020), Primary productivity and climate control mushroom yields in Mediterranean pine forests",
                "Estudio científico, series largas",
                "https://doi.org/10.1016/j.agrformet.2020.108015"
            ),
            MushroomKnowledgeSource(
                "Parladé et al. (2017), Effects of forest management and climatic variables on Boletus edulis",
                "Estudio científico",
                "https://doi.org/10.1016/j.foreco.2017.01.025"
            ),
            MushroomKnowledgeSource(
                "De la Varga et al. (2013), Seasonal dynamics of Boletus edulis and Lactarius deliciosus mycelium",
                "Estudio científico",
                "https://doi.org/10.1007/s00572-013-0481-3"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "lactarius-deliciosus",
        commonName = "Níscalo o rovellón",
        scientificName = "Lactarius deliciosus",
        evidenceLevel = "Estudios científicos en pinares; alcance local",
        geographicScope = "Pinares de Pinus pinaster y otros pinares estudiados del centro de España.",
        knownEvidence = listOf(
            "En un estudio de pinares de Pinus pinaster del centro de España, la precipitación de finales de verano/comienzos de otoño se relacionó con la emergencia y producción del conjunto de setas comestibles estudiado.",
            "En ese ámbito, el estudio informa de una relación positiva entre acidez del suelo y rendimiento de L. deliciosus.",
            "Otro trabajo estudió la dinámica estacional de su micelio en pinares; la dinámica del micelio no equivale directamente a la aparición de carpóforos."
        ),
        factorsRelevantToApp = listOf(
            "Precipitación de finales de verano/comienzos de otoño: señal observada en los pinares estudiados, no dosis universal.",
            "Acidez del suelo: asociación con rendimiento descrita para el ámbito de estudio, sin umbral de pH general.",
            "El contexto de pinar es relevante en las fuentes citadas; verificar el hospedador por región antes de aplicarlo."
        ),
        unknowns = listOf(
            "No se deduce una cantidad de lluvia, retardo exacto ni calendario universal.",
            "No se aporta un pH objetivo transferible a otros suelos.",
            "No permite estimar presencia en cualquier punto ni extrapolar a hábitats no estudiados."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Taye et al. (2016), Meteorological conditions and site characteristics driving edible mushroom production in Pinus pinaster forests of Central Spain",
                "Estudio científico, 17 años",
                "https://doi.org/10.1016/j.funeco.2016.05.008"
            ),
            MushroomKnowledgeSource(
                "Olano et al. (2020), Primary productivity and climate control mushroom yields in Mediterranean pine forests",
                "Estudio científico, series largas",
                "https://doi.org/10.1016/j.agrformet.2020.108015"
            ),
            MushroomKnowledgeSource(
                "De la Varga et al. (2013), Seasonal dynamics of Boletus edulis and Lactarius deliciosus mycelium",
                "Estudio científico",
                "https://doi.org/10.1007/s00572-013-0481-3"
            ),
            MushroomKnowledgeSource(
                "Liu et al. (2016), Lactarius deliciosus mycelium, fruitbody productivity and forest thinning",
                "Estudio científico",
                "https://doi.org/10.1016/j.foreco.2016.08.053"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "amanita-caesarea",
        commonName = "Oronja",
        scientificName = "Amanita caesarea",
        evidenceLevel = "Ficha micológica descriptiva",
        geographicScope = "La ficha ofrece una descripción general, no una serie fenológica regional.",
        knownEvidence = listOf(
            "La ficha la describe como termófila y asociada a bosques de frondosas.",
            "Menciona principalmente encinas, robles y castaños.",
            "Sitúa su aparición descrita en verano y principios de otoño."
        ),
        factorsRelevantToApp = listOf(
            "Hábitat descrito: frondosas, especialmente Quercus y Castanea.",
            "Estación cálida descrita: verano y principios de otoño."
        ),
        unknowns = listOf(
            "“Termófila” no proporciona un umbral numérico de temperatura.",
            "No se cuantifican lluvia, humedad del suelo, altitud ni diferencias regionales."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Amanita caesarea — Sociedad Micológica Barakaldo",
                "Ficha micológica",
                "https://micologica-barakaldo.org/amanita-caesarea/"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "cantharellus-cibarius",
        commonName = "Rebozuelo",
        scientificName = "Cantharellus cibarius",
        evidenceLevel = "Ficha micológica descriptiva",
        geographicScope = "Descripción general sin delimitación regional de fructificación.",
        knownEvidence = listOf(
            "La ficha menciona bosques de coníferas o frondosas y suelos ácidos.",
            "Indica primavera, verano y otoño como estaciones de aparición."
        ),
        factorsRelevantToApp = listOf(
            "Hábitat descrito: bosques de coníferas o frondosas.",
            "Suelo descrito como ácido y estación amplia de primavera a otoño."
        ),
        unknowns = listOf(
            "No se presentan valores de pH, temperatura, lluvia, humedad o altitud.",
            "La ficha no delimita la estación por región ni aporta una serie cuantitativa."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Cantharellus cibarius — Sociedad Micológica Barakaldo",
                "Ficha micológica",
                "https://micologica-barakaldo.org/cantharellus-cibarius/"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "hygrophorus-marzuolus",
        commonName = "Marzuelo",
        scientificName = "Hygrophorus marzuolus",
        evidenceLevel = "Ficha micológica descriptiva",
        geographicScope = "La ficha enumera regiones peninsulares, pero no constituye un mapa de registros.",
        knownEvidence = listOf(
            "La ficha lo sitúa desde mediados de invierno hasta mediados de primavera, con máximo descrito en febrero-abril.",
            "Menciona bosques de coníferas, frondosas y mixtos.",
            "Describe suelos ácidos y/o arcillosos."
        ),
        factorsRelevantToApp = listOf(
            "Estación fría descrita: invierno a primavera.",
            "Hábitat descrito: bosques de coníferas, frondosas o mixtos y ciertos tipos de suelo."
        ),
        unknowns = listOf(
            "No se especifican temperatura, humedad, lluvia, pH ni altitud numéricos.",
            "La ventana temporal no es una predicción local ni está cuantificada para cada región."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Hygrophorus marzuolus — Sociedad Micológica Barakaldo",
                "Ficha micológica",
                "https://micologica-barakaldo.org/hygrophorus-marzuolus/"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "amanita-ponderosa",
        commonName = "Gurumelo",
        scientificName = "Amanita ponderosa",
        evidenceLevel = "Ficha divulgativa firmada; evidencia de menor nivel",
        geographicScope = "La ficha es divulgativa y no aporta un muestreo regional.",
        knownEvidence = listOf(
            "La ficha la asocia con Quercus y jaras en bosque mediterráneo.",
            "Describe preferencia primaveral."
        ),
        factorsRelevantToApp = listOf(
            "Hábitat descrito: entorno mediterráneo con Quercus y jaras.",
            "Estación descrita como primaveral."
        ),
        unknowns = listOf(
            "Información pendiente de corroborar con fuentes independientes.",
            "No hay umbrales de lluvia, temperatura, suelo o altitud."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Amanita ponderosa — Jorge Jiménez Santos",
                "Ficha divulgativa firmada",
                "https://www.amanitacesarea.com/amanita-ponderosa.html"
            )
        )
    ),
    MushroomKnowledgeEntry(
        id = "pleurotus-eryngii",
        commonName = "Seta de cardo",
        scientificName = "Pleurotus eryngii",
        evidenceLevel = "Ficha divulgativa firmada; evidencia de menor nivel",
        geographicScope = "La ficha es divulgativa y no aporta un muestreo regional.",
        knownEvidence = listOf(
            "La ficha la relaciona con raíces/restos de cardos, especialmente Eryngium campestre.",
            "Describe praderas o claros y predominio otoñal, con posible aparición primaveral.",
            "No es una especie cuyo factor de hábitat principal descrito sea un árbol hospedador."
        ),
        factorsRelevantToApp = listOf(
            "Hábitat no arbóreo descrito: cardos y praderas o claros.",
            "Estación descrita: principalmente otoño, posiblemente primavera."
        ),
        unknowns = listOf(
            "Información pendiente de corroborar con fuentes independientes.",
            "No se cuantifican lluvia, temperatura, suelo, humedad ni altitud."
        ),
        sources = listOf(
            MushroomKnowledgeSource(
                "Pleurotus eryngii — Jorge Jiménez Santos",
                "Ficha divulgativa firmada",
                "https://www.amanitacesarea.com/pleurotus-eryngii.html"
            )
        )
    )
)
