package com.example.smartfixer.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PastFixDao {
    @Query("SELECT * FROM past_fixes ORDER BY date DESC")
    fun getAllFixes(): Flow<List<PastFixEntity>>

    @Query("SELECT * FROM past_fixes WHERE id = :id")
    suspend fun getFixById(id: Long): PastFixEntity?

    @Insert
    suspend fun insertFix(fix: PastFixEntity): Long
}
