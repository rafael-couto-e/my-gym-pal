package br.eti.rafaelcouto.gymbro.extensions

fun String.toIntOrZero(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}
