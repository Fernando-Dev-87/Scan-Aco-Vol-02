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
    val signatures: List<String>
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

    private fun calculateMatchScore(grade: SteelGrade, features: VisualFeatures): Double {
        // Se a cor for desconhecida ou comprimento zero, a probabilidade de ser faísca é nula
        if (features.color == "Unknown" || features.streamLengthMeters <= 0.1) {
            return 0.0
        }

        var score = 0.0
        
        // 1. Color Matching (30% weight)
        // Check if the detected color keyword exists in the grade description
        if (grade.sparkColor.contains(features.color, ignoreCase = true)) {
            score += 30.0
        }

        // 2. Stream Length Proximity (30% weight)
        val gradeLength = extractLength(grade.streamLength)
        val lengthDiff = abs(gradeLength - features.streamLengthMeters)
        // Score decreases as difference increases, max 30 points
        score += (2.0 - lengthDiff).coerceAtLeast(0.0) * 15.0 

        // 3. Carbon/Burst Frequency Correlation (20% weight)
        // Spark burst frequency is directly proportional to carbon content
        val carbonDiff = abs(grade.carbonLevel - features.burstFrequency)
        score += (1.0 - carbonDiff).coerceAtLeast(0.0) * 20.0

        // 4. Alloy Signature Detection (20% weight)
        // Matches specific visual patterns like "Spear Tip", "Thorns", "Bushy Stars"
        var signatureMatches = 0
        features.signatures.forEach { featureSig ->
            if (grade.alloySignatures.any { gradeSig -> gradeSig.contains(featureSig, ignoreCase = true) }) {
                signatureMatches++
            }
        }
        
        if (features.signatures.isNotEmpty()) {
            score += (signatureMatches.toDouble() / features.signatures.size) * 20.0
        } else if (grade.alloySignatures.isEmpty()) {
            // Neutral bonus if neither have specific signatures (e.g., Wrought Iron/Low Carbon)
            score += 10.0
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
