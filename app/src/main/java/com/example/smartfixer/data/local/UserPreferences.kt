package com.example.smartfixer.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
)

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val NAME = stringPreferencesKey("name")
        val EMAIL = stringPreferencesKey("email")
        val NOTIFICATIONS = booleanPreferencesKey("notifications")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
    }

    val profile: Flow<UserProfile> = context.dataStore.data.map { prefs ->
        UserProfile(
            name = prefs[Keys.NAME] ?: "",
            email = prefs[Keys.EMAIL] ?: "",
            notificationsEnabled = prefs[Keys.NOTIFICATIONS] ?: true,
            darkModeEnabled = prefs[Keys.DARK_MODE] ?: false
        )
    }

    suspend fun saveProfile(profile: UserProfile) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NAME] = profile.name
            prefs[Keys.EMAIL] = profile.email
            prefs[Keys.NOTIFICATIONS] = profile.notificationsEnabled
            prefs[Keys.DARK_MODE] = profile.darkModeEnabled
        }
    }
}
