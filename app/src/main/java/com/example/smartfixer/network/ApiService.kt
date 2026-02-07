package com.example.smartfixer.network

import com.example.smartfixer.data.DiagnosisRequest
import com.example.smartfixer.data.DiagnosisResult
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("diagnose")
    suspend fun diagnose(@Body request: DiagnosisRequest): DiagnosisResult
}
