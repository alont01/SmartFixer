package com.example.smartfixer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfixer.data.DiagnosisRequest
import com.example.smartfixer.data.DiagnosisResult
import com.example.smartfixer.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class DiagnosisUiState {
    object Idle : DiagnosisUiState()
    object Loading : DiagnosisUiState()
    data class Success(val result: DiagnosisResult) : DiagnosisUiState()
    data class Error(val message: String) : DiagnosisUiState()
}

class DiagnosisViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<DiagnosisUiState>(DiagnosisUiState.Idle)
    val uiState: StateFlow<DiagnosisUiState> = _uiState

    fun diagnose(description: String) {
        _uiState.value = DiagnosisUiState.Loading

        viewModelScope.launch {
            try {
                val result = RetrofitClient.apiService.diagnose(
                    DiagnosisRequest(description = description)
                )
                _uiState.value = DiagnosisUiState.Success(result)
            } catch (e: Exception) {
                _uiState.value = DiagnosisUiState.Error(
                    e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = DiagnosisUiState.Idle
    }
}
