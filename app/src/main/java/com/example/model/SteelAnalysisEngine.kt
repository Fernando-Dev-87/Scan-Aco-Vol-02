package com.example.model

import kotlin.math.abs

/**
 * VisualFeatures represents the processed optical data from the camera
 * after CV analysis of sparks.
 */
data class VisualFeatures(
    val color: String,
    val streamLengthMeters: Double,
    val burstFrequency: Double, // Normalized 0.0 to 1.0, correlates with carbon
    val signatures: List<String>,
    val intensityMultiplier: Double = 1.0 // Global compensation for grinding pressure
)

/**
 * SteelAnalysisEngine provides the offline matching logic for spark identification.
 * It compares real-time CV data against the local Knowledge Base.
 */
object SteelAnalysisEngine {

    /**
     * Matches the observed visual features against the preset steel grades.
     * Uses a weighted scoring system based on color, length, carbon level, and alloy signatures.
     */
    fun matchGrade(features: VisualFeatures): SteelGrade {
        val grades = SteelPresets.allGrades
        
        val bestMatch = grades.maxByOrNull { grade ->
            calculateMatchScore(grade, features)
        }
        
        val bestScore = bestMatch?.let { calculateMatchScore(it, features) } ?: 0.0
        
        // Se a pontuação for muito baixa (ex: não parece uma faísca), retorna não identificado
        return if (bestScore < 20.0) {
            SteelPresets.unidentified
        } else {
            bestMatch ?: SteelPresets.unidentified
        }
    }

    private val colorDistanceMap = mapOf(
        "White" to listOf("Straw", "Yellow", "Bright White", "White-Straw"),
        "Straw" to listOf("White", "Yellow", "Orange-Straw", "White-Straw"),
        "Yellow" to listOf("White", "Straw", "Orange", "Yellow-Orange"),
        "Orange" to listOf("Yellow", "Red", "Yellow-Orange", "Dull Orange", "Orange-Straw"),
        "Red" to listOf("Orange", "Dark Red", "Laranja-Avermelhado"),
        "Bright White" to listOf("White", "White-Straw"),
        "White-Straw" to listOf("White", "Straw", "Bright White"),
        "Orange-Straw" to listOf("Orange", "Straw"),
        "Yellow-Orange" to listOf("Yellow", "Orange"),
        "Dull Orange" to listOf("Orange", "Red")
    )

    private fun calculateMatchScore(grade: SteelGrade, features: VisualFeatures): Double {
        // Apply Intensity Compensation
        val compLength = features.streamLengthMeters / features.intensityMultiplier
        val compFrequency = (features.burstFrequency / features.intensityMultiplier).coerceIn(0.0, 2.5)

        if (features.color == "Unknown" || compLength <= 0.1) {
            return 0.0
        }

        var score = 0.0
        
        // 1. Weighted Spectral Matcher (20% weight)
        val targetColor = features.color.lowercase()
        val gradeColor = grade.sparkColor.lowercase()
        
        if (gradeColor.contains(targetColor)) {
            score += 20.0
        } else {
            // Check spectral proximity
            val relatedColors = colorDistanceMap.entries.find { it.key.lowercase() == targetColor }?.value ?: emptyList()
            if (relatedColors.any { gradeColor.contains(it.lowercase()) }) {
                score += 10.0 // Half points for proximal colors
            }
        }

        // 2. Stream Length Proximity (20% weight)
        val gradeLength = extractLength(grade.streamLength)
        val lengthDiff = abs(gradeLength - compLength)
        score += (2.0 - lengthDiff).coerceAtLeast(0.0) * 10.0 

        // 3. Carbon/Burst Frequency Correlation (20% weight)
        val carbonDiff = abs(grade.carbonLevel - compFrequency)
        score += (1.0 - carbonDiff).coerceAtLeast(0.0) * 20.0

        // 4. Alloy Signature Detection (40% weight) - Increased Priority
        var signatureMatches = 0
        features.signatures.forEach { featureSig ->
            if (grade.alloySignatures.any { gradeSig -> gradeSig.contains(featureSig, ignoreCase = true) }) {
                signatureMatches++
            }
        }
        
        if (features.signatures.isNotEmpty()) {
            score += (signatureMatches.toDouble() / features.signatures.size) * 40.0
        } else if (grade.alloySignatures.isEmpty()) {
            score += 15.0 // Neutral bonus
        }

        return score
    }
    
    /**
     * Helper to parse numerical length from descriptive strings like "1.4 m (Moderate)"
     */
    private fun extractLength(lengthStr: String): Double {
        return try {
            lengthStr.split(" ")[0].toDoubleOrNull() ?: 1.0
        } catch (_: Exception) {
            1.0
        }
    }
}
