package com.glucozo.androidassessment.util

object ConvertBytes {
    fun convertBytesToKilobytes(bytes: Long): Double {
        return bytes.toDouble() / 1024
    }

    fun convertBytesToMegabytes(bytes: Long): Double {
        return bytes.toDouble() / (1024 * 1024)
    }
}