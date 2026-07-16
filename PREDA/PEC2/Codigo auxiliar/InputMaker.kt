package es.juanlum

import java.io.File

fun main() {

    val file:File = File("entrada.txt")

    for (i in 1..100) {

        File("entrada$i.txt").writeText("1 3 ${i.toString()}")

    }
}
