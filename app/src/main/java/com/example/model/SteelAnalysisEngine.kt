package com.example.model

import kotlin.math.abs

data class VisualFeatures(
    val color: String,
    val streamLengthMeters: Double,
    val burstFrequency: Double, // Relacionado ao teor de carbono/explosões
    val signatures: List<String>,
    val intensityMultiplier: Double = 1.0
)

object SteelAnalysisEngine {

    /**
     * Motor de Combinação Técnica (Match Engine)
     * Compara as características capturadas (OpenCV/Morfologia) com a base de dados técnica.
     */
    fun matchGrade(features: VisualFeatures): SteelGrade {
        if (features.color == "Unknown") return SteelPresets.unidentified

        var bestMatch: SteelGrade? = null
        var highestScore = -1.0

        for (grade in SteelPresets.allGrades) {
            val score = calculateMatchScore(grade, features)
            if (score > highestScore) {
                highestScore = score
                bestMatch = grade
            }
        }

        // Se a pontuação for muito baixa (< 25%), não identifica o material
        return if (highestScore < 25.0) SteelPresets.unidentified else bestMatch!!
    }

    private fun calculateMatchScore(grade: SteelGrade, features: VisualFeatures): Double {
        var score = 0.0
        
        // 1. Rigorous Color Logic (35% weight)
        val targetColor = features.color.lowercase()
        val gradeColor = grade.sparkColor.lowercase()
        
        // Se a cor for fundamentalmente diferente, a pontuação cai drasticamente
        if (gradeColor.contains(targetColor) || targetColor.contains(gradeColor.split("-").first())) {
            score += 35.0
        } else {
            // Penalidade severa para cores totalmente incompatíveis
            return 0.0 
        }

        // 2. Carbon Level / Branching Morphology (35% weight)
        // Diferenciação crítica entre 1020 (carbono 0.2) e 1045 (carbono 0.45)
        val carbonDiff = abs(grade.carbonLevel - features.burstFrequency)
        if (carbonDiff < 0.1) {
            score += 35.0
        } else {
            score += (0.5 - carbonDiff).coerceAtLeast(0.0) * 70.0
        }

        // 3. Alloy Signature Match (30% weight)
        // Baseado em padrões técnicos como "Spear Tip", "Dendritic", "Willow leaf"
        var signatureMatches = 0
        features.signatures.forEach { featureSig ->
            if (grade.alloySignatures.any { gradeSig -> 
                gradeSig.contains(featureSig, ignoreCase = true) || 
                featureSig.contains(gradeSig, ignoreCase = true) 
            }) {
                signatureMatches++
            }
        }
        
        if (features.signatures.isNotEmpty()) {
            val matchRatio = signatureMatches.toDouble() / features.signatures.size
            score += matchRatio * 30.0
        }

        return score
    }
}
