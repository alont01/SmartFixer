package com.example.smartfixer.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "past_fixes")
data class PastFixEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val difficulty: String,
    val estimatedTime: String,
    val toolsJson: String,
    val stepsJson: String,
    val category: String = "general",
    val date: Long = System.currentTimeMillis(),
    val status: String = "Completed"
)
