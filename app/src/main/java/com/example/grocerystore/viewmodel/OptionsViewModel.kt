package com.example.grocerystore.viewmodel

import android.content.Context
import androidx.compose.ui.graphics.Color
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class OptionsViewModel(context: Context) : ViewModel() {

    companion object {
        private const val PREF_NAME = "OptionsPrefs"
        private const val PREF_COLOR_KEY = "colorOption"
        private const val PREF_SIZE_KEY = "sizeOption"
    }

    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    private val defaultColorOption = Color(10, 97, 196)
    private val defaultSizeOption = 18

    private val _colorOption = MutableStateFlow(loadColorOption())
    private val colorOption: StateFlow<Color> = _colorOption

    private val _sizeOption = MutableStateFlow(loadSizeOption())
    private val sizeOption: StateFlow<Int> = _sizeOption

    fun saveColorOption(color: String) {
        when (color) {
            "Red" -> colorSave(Color(235,87,87))
            "Blue" -> colorSave(Color(10, 97, 196, 255))
            "Green" -> colorSave(Color(10,196,84))
        }
    }

    private fun colorSave(color: Color){
        preferences.edit {
            putFloat("$PREF_COLOR_KEY Red", color.red)
            putFloat("$PREF_COLOR_KEY Green", color.green)
            putFloat("$PREF_COLOR_KEY Blue", color.blue)
        }
        _colorOption.value = color
    }

    fun saveSizeOption(size: String) {
        var s = sizeOption.value
        when (size) {
            "Small" -> s = 14
            "Medium" -> s = 16
            "Large" -> s = 18
        }
        preferences.edit { putInt(PREF_SIZE_KEY, s) }
        _sizeOption.value = s
    }

    private fun loadColorOption(): Color {

        val r = preferences.getFloat("$PREF_COLOR_KEY Red", defaultColorOption.red)
        val g = preferences.getFloat("$PREF_COLOR_KEY Green", defaultColorOption.green)
        val b = preferences.getFloat("$PREF_COLOR_KEY Blue", defaultColorOption.blue)


        return Color(r, g, b)
    }

    private fun loadSizeOption(): Int {
        return preferences.getInt(PREF_SIZE_KEY, defaultSizeOption) ?: defaultSizeOption
    }

    fun returnSizeOption(): Int {
        return sizeOption.value
    }

    fun returnColorOption(): Color {
        return colorOption.value
    }

    fun returnSizeOptionAsString(): String {
        val size = sizeOption.value
        return when (size) {
            14 -> "Small"
            16 -> "Medium"
            18 -> "Large"
            else -> "Medium" // Default to "Medium" for unknown sizes
        }
    }

    fun returnColorOptionAsString(): String {
        val color = colorOption.value
        return when {
            color == Color(235, 87, 87) -> "Red"
            color == Color(10, 97, 196, 255) -> "Blue"
            color == Color(10, 196, 84) -> "Green"
            else -> "Blue" // Default to "Blue" for unknown colors
        }
    }
}