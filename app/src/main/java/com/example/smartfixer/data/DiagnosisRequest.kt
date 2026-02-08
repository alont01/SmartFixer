package com.example.smartfixer.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DiagnosisRequest(
    val description: String,
    @SerialName("image_base64")
    val imageBase64: String? = null,
    @SerialName("image_media_type")
    val imageMediaType: String? = null
)
