package com.example.tp.ui.theme


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : ViewModel() {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        viewModelScope.launch {
            dataStore.data.collect { preferences ->
                val darkMode = preferences[DARK_MODE_KEY] ?: false
                _isDarkMode.value = darkMode
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            dataStore.edit { preferences ->
                val currentMode = preferences[DARK_MODE_KEY] ?: false
                preferences[DARK_MODE_KEY] = !currentMode
            }
        }
    }

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
    }
}