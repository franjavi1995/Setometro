package com.setometro.domain

data class EspecieSeta(
    val nombre: String,
    val descripcion: String,
    val tipoBosquePreferido: List<String>,
    val condicionesIdeales: CondicionesIdeales
)

data class CondicionesIdeales(
    val lluviaMin: Double,
    val tempMin: Double,
    val tempMax: Double
)

enum class NivelProbabilidad { BAJA, MEDIA, ALTA }

object ProbabilidadSetas {
    fun calcular(
        especie: EspecieSeta,
        tipoBosque: String,
        lluvia10dias: Double,
        tempMedia10dias: Double
    ): NivelProbabilidad {
        val bosqueOk = especie.tipoBosquePreferido.any { it.equals(tipoBosque, ignoreCase = true) }
        val lluviaOk = lluvia10dias >= especie.condicionesIdeales.lluviaMin
        val tempOk = tempMedia10dias in especie.condicionesIdeales.tempMin..especie.condicionesIdeales.tempMax
        return when {
            bosqueOk && lluviaOk && tempOk -> NivelProbabilidad.ALTA
            (bosqueOk && (lluviaOk || tempOk)) || ((lluviaOk && tempOk)) -> NivelProbabilidad.MEDIA
            else -> NivelProbabilidad.BAJA
        }
    }
}

val especiesPrincipales = listOf(
    EspecieSeta(
        nombre = "Boletus edulis",
        descripcion = "Boletus comestible de gran valor, sombrero marrón claro, pie grueso.",
        tipoBosquePreferido = listOf("Robledal", "Castañar", "Hayedo"),
        condicionesIdeales = CondicionesIdeales(20.0, 10.0, 20.0)
    ),
    EspecieSeta(
        nombre = "Boletus pinícola",
        descripcion = "Boletus de pinar o bosque mixto, sombrero marrón rojizo.",
        tipoBosquePreferido = listOf("Pinar", "Mixto"),
        condicionesIdeales = CondicionesIdeales(25.0, 8.0, 18.0)
    ),
    EspecieSeta(
        nombre = "Níscalo (Lactarius deliciosus)",
        descripcion = "Seta naranja, típica de pinares, muy apreciada.",
        tipoBosquePreferido = listOf("Pinar"),
        condicionesIdeales = CondicionesIdeales(15.0, 12.0, 18.0)
    ),
    EspecieSeta(
        nombre = "Amanita caesarea",
        descripcion = "Amanita de gran valor, sombrero naranja, pie amarillo.",
        tipoBosquePreferido = listOf("Encinar", "Mixto"),
        condicionesIdeales = CondicionesIdeales(25.0, 18.0, 25.0)
    )
)
