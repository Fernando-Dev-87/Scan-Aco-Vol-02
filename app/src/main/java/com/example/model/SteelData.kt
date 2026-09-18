package com.example.model

data class AlloyComponent(
    val name: String,
    val symbol: String,
    val measuredPercent: Double,
    val nominalRange: String,
    val function: String
)

data class SparkCharacteristic(
    val property: String,
    val value: String,
    val detail: String
)

data class SteelGrade(
    val code: String,
    val standard: String,
    val classification: String,
    val confidencePercent: Int,
    val summary: String,
    val sparkColor: String,
    val streamLength: String,
    val burstPattern: String,
    val alloys: List<AlloyComponent>,
    val characteristics: List<SparkCharacteristic>,
    val carbonLevel: Double, // Usado para determinar branching ratio
    val isMagnetic: Boolean,
    val alloySignatures: List<String>
)

object SteelPresets {
    val sae1045 = SteelGrade(
        code = "SAE 1045",
        standard = "ABNT / SAE / AISI",
        classification = "Aço Médio Carbono (Aço 45)",
        confidencePercent = 100,
        summary = "Feixe amarelo brilhante, longo e grosso. Rajadas secundárias abundantes com presença de pólen (pontos brilhantes) entre as linhas. Nós brilhantes e cauda expandida.",
        sparkColor = "Bright White-Yellow",
        streamLength = "1.3m",
        burstPattern = "Multi-split dendritic with pollen",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 0.46, "0.43 - 0.50%", "Controla intensidade das ramificações"),
            AlloyComponent("Manganês", "Mn", 0.72, "0.60 - 0.90%", "Desoxidação")
        ),
        characteristics = listOf(
            SparkCharacteristic("Explosão", "Dendrítica", "Ramificações em múltiplos níveis"),
            SparkCharacteristic("Pólen", "Detectado", "Pontos de luz entre as linhas de fluxo"),
            SparkCharacteristic("Cauda", "Larga", "Grande volume de centelhas na ponta")
        ),
        carbonLevel = 0.45,
        isMagnetic = true,
        alloySignatures = listOf("Dendritic", "Pollen", "Multi-split", "Bright nodes", "Large tail")
    )

    val sae1020 = SteelGrade(
        code = "SAE 1020",
        standard = "ABNT / SAE / AISI",
        classification = "Aço de Baixo Carbono",
        confidencePercent = 100,
        summary = "Linhas aerodinâmicas longas, retas e levemente penduradas. Explosões esparsas com padrão de ruptura dividido simples.",
        sparkColor = "White-Straw",
        streamLength = "1.6m",
        burstPattern = "Sparse divided rupture",
        alloys = listOf(AlloyComponent("Carbono", "C", 0.20, "0.18 - 0.23%", "Baixa dureza")),
        characteristics = listOf(
            SparkCharacteristic("Ruptura", "Simples", "Feixes que se partem apenas uma vez"),
            SparkCharacteristic("Trajetória", "Linear", "Linhas retas sem muitas curvas")
        ),
        carbonLevel = 0.20,
        isMagnetic = true,
        alloySignatures = listOf("Long straight lines", "Sparse bursts", "Divided rupture", "Thin stream")
    )

    val sae1095 = SteelGrade(
        code = "SAE 1095",
        standard = "ABNT / SAE / AISI",
        classification = "Aço de Alto Carbono",
        confidencePercent = 100,
        summary = "Explosões em forma de árvore multi-bifurcada densas e aglomeradas. Distância muito pequena entre as explosões. Volume massivo de centelhas.",
        sparkColor = "Bright White",
        streamLength = "0.8m",
        burstPattern = "Dense tree-shape dendritic",
        alloys = listOf(AlloyComponent("Carbono", "C", 0.95, "0.90 - 1.03%", "Alta dureza")),
        characteristics = listOf(
            SparkCharacteristic("Densidade", "Crítica", "Aglomerados de explosões massivos"),
            SparkCharacteristic("Morfologia", "Árvore", "Ramificações complexas e curtas")
        ),
        carbonLevel = 0.95,
        isMagnetic = true,
        alloySignatures = listOf("Dendritic tree", "Clustered bursts", "High density", "Short stream")
    )

    val sae4140 = SteelGrade(
        code = "SAE 4140",
        standard = "ABNT / SAE / AISI",
        classification = "Aço Cromo-Molibdênio (42CrMo)",
        confidencePercent = 100,
        summary = "Feixe amarelo-alaranjado com flores compostas. Possui flores na ponta da lança (efeito molibdênio) e padrões levemente irregulares.",
        sparkColor = "Yellow-Orange",
        streamLength = "1.2m",
        burstPattern = "Composite flowers with spear tips",
        alloys = listOf(
            AlloyComponent("Cromo", "Cr", 1.00, "0.80 - 1.10%", "Temperabilidade"),
            AlloyComponent("Molibdênio", "Mo", 0.20, "0.15 - 0.25%", "Assinatura visual")
        ),
        characteristics = listOf(
            SparkCharacteristic("Assinatura", "Ponta de Lança", "Formato de seta na ponta do feixe"),
            SparkCharacteristic("Regularidade", "Irregular", "Padrão de explosão levemente confuso")
        ),
        carbonLevel = 0.40,
        isMagnetic = true,
        alloySignatures = listOf("Spear Tip", "Composite flowers", "Pollen", "Molybdenum effect")
    )

    val aisiM2 = SteelGrade(
        code = "AISI M2",
        standard = "ASTM / AISI / HSS",
        classification = "Aço Rápido (W6Mo5Cr4V2)",
        confidencePercent = 100,
        summary = "Cor amarelo-alaranjada curta com base vermelho escuro. Cauda aerodinâmica espessa lembrando uma 'folha de salgueiro' com flores na cauda.",
        sparkColor = "Orange-Red",
        streamLength = "0.6 m",
        burstPattern = "Willow leaf tail",
        alloys = listOf(
            AlloyComponent("Tungstênio", "W", 6.00, "5.50 - 6.75%", "Dureza ao rubro"),
            AlloyComponent("Molibdênio", "Mo", 5.00, "4.50 - 5.50%", "Inibidor de faísca")
        ),
        characteristics = listOf(
            SparkCharacteristic("Morfologia", "Folha de Salgueiro", "Ponta do feixe com formato foliar"),
            SparkCharacteristic("Cor Base", "Vermelho Escuro", "Característico de aços rápidos")
        ),
        carbonLevel = 0.85,
        isMagnetic = true,
        alloySignatures = listOf("Willow leaf", "Dull red base", "Small explosions")
    )

    val aisiD2 = SteelGrade(
        code = "AISI D2",
        standard = "ABNT / AISI / DIN 1.2379",
        classification = "Aço Ferramenta Cr-Mo-V (Cr12MoV)",
        confidencePercent = 100,
        summary = "Feixe fino e extremamente curto. Linhas onduladas e intermitentes com fogos de artifício poderosos em três flores diferentes. Ponta de lança óbvia.",
        sparkColor = "Orange",
        streamLength = "0.7 m",
        burstPattern = "Three-flower explosion with spear tips",
        alloys = listOf(
            AlloyComponent("Cromo", "Cr", 12.00, "11.0 - 13.0%", "Resistência abrasiva"),
            AlloyComponent("Vanádio", "V", 0.80, "0.50 - 1.10%", "Carbetos duros")
        ),
        characteristics = listOf(
            SparkCharacteristic("Feixe", "Ondulado/Curto", "Intermitente e muito delgado"),
            SparkCharacteristic("Explosão", "Tripla", "Três estágios de flores com muito pólen")
        ),
        carbonLevel = 1.55,
        isMagnetic = true,
        alloySignatures = listOf("Spear Tip", "Wavy streams", "Intermittent lines", "Triple flowers")
    )

    val castIron = SteelGrade(
        code = "Cast Iron",
        standard = "ASTM / ABNT",
        classification = "Ferro Fundido",
        confidencePercent = 100,
        summary = "Feixe curto avermelhado com explosões repetitivas pequenas. Alto carbono satura a centelha.",
        sparkColor = "Red-Orange",
        streamLength = "0.5 m",
        burstPattern = "Repetitive small bursts",
        alloys = listOf(AlloyComponent("Carbono", "C", 3.00, "> 2.5%", "Saturação")),
        characteristics = listOf(SparkCharacteristic("Comprimento", "Extremamente Curto", "Queda rápida das centelhas")),
        carbonLevel = 3.0,
        isMagnetic = true,
        alloySignatures = listOf("Repetitive bursts", "Dull color")
    )

    val aisi304 = SteelGrade(
        code = "AISI 304",
        standard = "ABNT / ASTM",
        classification = "Aço Inox Austenítico",
        confidencePercent = 100,
        summary = "Produz poucas faíscas devido à alta liga. Cor e intensidade variam, mas geralmente feixe curto sem explosões de carbono.",
        sparkColor = "Reddish-Straw",
        streamLength = "0.6 m",
        burstPattern = "Continuous lines no bursts",
        alloys = listOf(AlloyComponent("Cromo", "Cr", 18.00, "18-20%", "Inoxidável")),
        characteristics = listOf(SparkCharacteristic("Magnetismo", "Não Magnético", "Fase austenítica")),
        carbonLevel = 0.05,
        isMagnetic = false,
        alloySignatures = listOf("Continuous lines", "No explosions")
    )

    val grade16MnCr5 = SteelGrade(
        code = "16MnCr5",
        standard = "DIN / ISO",
        classification = "Aço Cementação Cr-Mn",
        confidencePercent = 100,
        summary = "Aço para cementação; feixe com ramificações finas e fluxo suprimido. Cor amarelo-palha característica.",
        sparkColor = "Straw",
        streamLength = "1.2m",
        burstPattern = "Fine forks",
        alloys = listOf(
            AlloyComponent("Cromo", "Cr", 0.95, "0.8-1.1%", "Dureza"),
            AlloyComponent("Manganês", "Mn", 1.15, "1.0-1.3%", "Temperabilidade")
        ),
        characteristics = listOf(SparkCharacteristic("Morfologia", "Garfos Finos", "Bifurcações discretas")),
        carbonLevel = 0.16,
        isMagnetic = true,
        alloySignatures = listOf("Fine forks", "Straw color")
    )

    val unidentified = SteelGrade(
        code = "N/A",
        standard = "---",
        classification = "Material não Encontrado",
        confidencePercent = 0,
        summary = "As características visuais capturadas não correspondem a nenhum padrão de faíscas conhecido.",
        sparkColor = "---",
        streamLength = "---",
        burstPattern = "---",
        alloys = emptyList(),
        characteristics = emptyList(),
        carbonLevel = 0.0,
        isMagnetic = false,
        alloySignatures = emptyList()
    )

    val allGrades = listOf(
        sae1045, sae1020, sae1095, sae4140, aisiM2, aisiD2, castIron, aisi304, grade16MnCr5
    )
}
