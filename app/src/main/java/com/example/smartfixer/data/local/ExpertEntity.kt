package com.example.smartfixer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "experts")
data class ExpertEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val category: String,
    val phone: String,
    val email: String,
    val hourlyRate: Double,
    val description: String,
    val availability: String,
    val rating: Float,
    val yearsExperience: Int,
    val certifications: String,
    val serviceArea: String,
    val createdAt: Long = System.currentTimeMillis()
)
