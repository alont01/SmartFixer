package com.example.smartfixer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfixer.data.local.UserPreferencesRepository
import com.example.smartfixer.data.local.UserProfile
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = UserPreferencesRepository(application)

    val profile: StateFlow<UserProfile> = repository.profile
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserProfile())

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            repository.saveProfile(profile)
        }
    }
}
