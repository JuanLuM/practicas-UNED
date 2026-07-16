package es.juanlum

import java.io.File

fun main() {

    println("Enter the number of rows: ")
    val rows = readln().toInt()
    println("Enter the number of columns: ")
    val cols = readln().toInt()

    val matrix = matrix(rows, cols)
    val worstMatrix = worstMatrix(rows, cols)


    writeMatrixToFile(matrix, "matrix.txt")

    for (i in 1..cols) {
        writeMatrixToFile(matrix, "normal_matrix", i)
    }
    for (i in 1..cols) {
        writeRiggedMatrixToFile(matrix, "rigged_matrix", i)
    }
    for (i in 1..cols) {
        writeMatrixToFile(worstMatrix, "worst_matrix", i)
    }

}

fun matrix(x: Int, y: Int, min:Int=10, max:Int=100): Array<IntArray> {

    val matrix = Array(x) { IntArray(y) }
    for (i in 0 until x) {
        for (j in 0 until y) {
            matrix[i][j] = (min until max).random()
        }
    }
    return matrix
}

fun riggedMatrix(x: Int, y: Int, min:Int=10, max:Int=100): Array<IntArray> {

    val matrix = Array(x) { IntArray(y) }
    for (i in 0 until x) {
        for (j in 0 until y) {
            if (i == j) {
                matrix[i][j] = 0
            } else {
                matrix[i][j] = (min until max).random()
            }
        }
    }
    return matrix
}

fun worstMatrix(x: Int, y: Int, value:Int=1): Array<IntArray> {

    val matrix = Array(x) { IntArray(y) }
    for (i in 0 until x) {
        for (j in 0 until y) {
            matrix[i][j] = value
        }
    }
    return matrix
}


fun writeMatrixToFile(matrix: Array<IntArray>, filename: String) {

    File(filename).printWriter().use { out ->
        out.println("${matrix.size} ${matrix[0].size}")
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                out.print("${matrix[i][j]} ")
            }
            out.println()
        }
    }
}

fun writeMatrixToFile(matrix: Array<IntArray>, filename: String, dimension: Int) {

    File("$filename$dimension.txt").printWriter().use { out ->
        out.println("$dimension $dimension")
        for (i in 0 until dimension) {
            for (j in 0 until dimension) {
                out.print("${matrix[i][j]} ")
            }
            out.println()
        }
    }
}

fun writeRiggedMatrixToFile(normalMatrix: Array<IntArray>, filename: String, dimension: Int = matrix.size) {

    File("$filename$dimension.txt").printWriter().use { out ->
        out.println("$dimension $dimension")
        for (i in 0 until dimension) {
            for (j in 0 until dimension) {
                if (i == j) {
                    out.print("0 ")
                } else {
                    out.print("${matrix[i][j]} ")
                }
            }
            out.println()
        }
    }
}
