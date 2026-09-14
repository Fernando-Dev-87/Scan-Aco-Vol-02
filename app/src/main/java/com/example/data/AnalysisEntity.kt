package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "analysis_history")
data class AnalysisEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val gradeCode: String,
    val timestamp: Long,
    val confidence: Int,
    val classification: String,
    val standard: String,
    val sparkColor: String,
    val streamLength: String,
    val burstPattern: String,
    val alloysJson: String
)
