package com.example.smartfixer.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisRequest(
    val description: String,
    @SerialName("image_url")
    val imageUrl: String? = null
)
