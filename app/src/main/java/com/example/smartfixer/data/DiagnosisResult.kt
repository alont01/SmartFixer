package com.example.smartfixer.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisResult(
    val title: String,
    val difficulty: String,
    @SerialName("estimated_time")
    val estimatedTime: String,
    val tools: List<String>,
    val steps: List<String>
)
