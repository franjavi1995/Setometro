package com.setometro.app.data.model

enum class FieldObservationResult(val label: String, val explanation: String) {
    FOUND("Encontré", "Se observaron ejemplares de esta especie."),
    SEARCHED_NOT_FOUND(
        "Busqué y no encontré",
        "Es el resultado de una búsqueda, no demuestra que la especie esté ausente."
    ),
    NOT_SEARCHED("No busqué", "Registro informativo; no indica presencia ni ausencia.")
}
