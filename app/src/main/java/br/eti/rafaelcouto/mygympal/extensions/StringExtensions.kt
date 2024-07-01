package br.eti.rafaelcouto.mygympal.extensions

fun String.toIntOrZero(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        0
    }
}
