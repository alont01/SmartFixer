package com.example.smartfixer

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfixer.data.DiagnosisRequest
import com.example.smartfixer.data.DiagnosisResult
import com.example.smartfixer.data.local.AppDatabase
import com.example.smartfixer.data.local.PastFixEntity
import com.example.smartfixer.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

sealed class DiagnosisUiState {
    object Idle : DiagnosisUiState()
    object Loading : DiagnosisUiState()
    data class Success(val result: DiagnosisResult) : DiagnosisUiState()
    data class Error(val message: String) : DiagnosisUiState()
}

class DiagnosisViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).pastFixDao()

    private val _uiState = MutableStateFlow<DiagnosisUiState>(DiagnosisUiState.Idle)
    val uiState: StateFlow<DiagnosisUiState> = _uiState

    private val _selectedImageUri = MutableStateFlow<Uri?>(null)
    val selectedImageUri: StateFlow<Uri?> = _selectedImageUri

    private val _lastCategory = MutableStateFlow<String?>(null)
    val lastCategory: StateFlow<String?> = _lastCategory

    fun setSelectedImageUri(uri: Uri?) {
        _selectedImageUri.value = uri
    }

    fun clearSelectedImage() {
        _selectedImageUri.value = null
    }

    fun diagnose(description: String, imageBase64: String? = null, imageMediaType: String? = null) {
        _uiState.value = DiagnosisUiState.Loading

        viewModelScope.launch {
            try {
                val result = RetrofitClient.apiService.diagnose(
                    DiagnosisRequest(
                        description = description,
                        imageBase64 = imageBase64,
                        imageMediaType = imageMediaType
                    )
                )
                _uiState.value = DiagnosisUiState.Success(result)
                _lastCategory.value = result.category

                // Auto-save to Room
                dao.insertFix(
                    PastFixEntity(
                        title = result.title,
                        difficulty = result.difficulty,
                        estimatedTime = result.estimatedTime,
                        toolsJson = Json.encodeToString(result.tools),
                        stepsJson = Json.encodeToString(result.steps),
                        category = result.category
                    )
                )
            } catch (e: Exception) {
                _uiState.value = DiagnosisUiState.Error(
                    e.message ?: "An unknown error occurred"
                )
            }
        }
    }

    fun resetState() {
        _uiState.value = DiagnosisUiState.Idle
        _selectedImageUri.value = null
    }
}
