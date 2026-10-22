package com.setometro.app.data.model

data class ObservedSpecies(
    val name: String,
    val scientificName: String?
)

val documentedObservedSpecies = listOf(
    ObservedSpecies("Parasol", "Macrolepiota procera"),
    ObservedSpecies("Boleto de pino", "Boletus pinophilus"),
    ObservedSpecies("Boletus", "Boletus edulis"),
    ObservedSpecies("Níscalo", "Lactarius deliciosus"),
    ObservedSpecies("Oronja", "Amanita caesarea"),
    ObservedSpecies("Rebozuelo", "Cantharellus cibarius"),
    ObservedSpecies("Marzuelo", "Hygrophorus marzuolus"),
    ObservedSpecies("Gurumelo", "Amanita ponderosa"),
    ObservedSpecies("Seta de cardo", "Pleurotus eryngii")
)
