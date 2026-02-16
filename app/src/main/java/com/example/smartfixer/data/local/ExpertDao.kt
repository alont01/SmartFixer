package com.example.smartfixer.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpertDao {
    @Query("SELECT * FROM experts ORDER BY rating DESC")
    fun getAllExperts(): Flow<List<ExpertEntity>>

    @Query("SELECT * FROM experts WHERE category = :category ORDER BY rating DESC")
    fun getExpertsByCategory(category: String): Flow<List<ExpertEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpert(expert: ExpertEntity): Long

    @Update
    suspend fun updateExpert(expert: ExpertEntity)

    @Delete
    suspend fun deleteExpert(expert: ExpertEntity)

    @Query("SELECT COUNT(*) FROM experts")
    suspend fun getExpertCount(): Int
}
