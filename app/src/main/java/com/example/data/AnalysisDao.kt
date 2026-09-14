package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AnalysisDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(analysis: AnalysisEntity)

    @Query("SELECT * FROM analysis_history ORDER BY timestamp DESC")
    fun getAll(): Flow<List<AnalysisEntity>>

    @Delete
    suspend fun delete(analysis: AnalysisEntity)
}
