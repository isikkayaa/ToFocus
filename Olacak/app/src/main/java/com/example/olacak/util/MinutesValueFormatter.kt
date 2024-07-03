package com.example.olacak.util

import com.github.mikephil.charting.formatter.ValueFormatter

class MinutesValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return "${value.toInt()} Min."
    }
}
