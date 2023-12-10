package com.example.grocerystore

class Formatter {
     fun formatDoubleToString(doubleValue: Double): String {
        return String.format("%.2f", doubleValue)
    }

     fun formatStringToDouble(stringValue: String): Double {
        return try {
            stringValue.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }
}