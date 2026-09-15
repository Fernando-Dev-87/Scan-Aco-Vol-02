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
    val carbonLevel: Double,
    val isMagnetic: Boolean,
    val alloySignatures: List<String>
)

object SteelPresets {
    val sae1045 = SteelGrade(
        code = "SAE 1045",
        standard = "ABNT / SAE / AISI",
        classification = "Aço Carbono Médio para Construção Mecânica",
        confidencePercent = 94,
        summary = "Excelente relação entre tenacidade e temperabilidade superficial por indução/chama; feixe com densas explosões ramificadas.",
        sparkColor = "Bright White",
        streamLength = "~1.3m",
        burstPattern = "Explosive stars",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 0.46, "0.43 - 0.50%", "Controla dureza mecânica e intensidade de ramificações"),
            AlloyComponent("Manganês", "Mn", 0.72, "0.60 - 0.90%", "Melhora a temperabilidade do núcleo e desoxidação"),
            AlloyComponent("Cromo", "Cr", 0.20, "0.15 - 0.25%", "Refina o grão e confere resistência residual ao desgaste"),
            AlloyComponent("Níquel", "Ni", 0.14, "0.10 - 0.20%", "Aumenta a tenacidade em temperaturas operacionais"),
            AlloyComponent("Silício", "Si", 0.24, "0.15 - 0.30%", "Desoxidante secundário com linhas finas condutoras")
        ),
        characteristics = listOf(
            SparkCharacteristic("Frequência de Explosão", "142 Hz", "Detonações contínuas de carbono próximas à ponta do feixe"),
            SparkCharacteristic("Linhas Condutoras", "Retilíneas Douradas", "Feixes lineares com alta velocidade inicial"),
            SparkCharacteristic("Geometria Estrelada", "Estrela de 6 Raios", "Ramos espessos com bifurcações multifoliadas")
        ),
        carbonLevel = 0.45,
        isMagnetic = true,
        alloySignatures = listOf("Explosive stars", "Branching bursts")
    )

    val sae8620 = SteelGrade(
        code = "SAE 8620",
        standard = "ABNT / SAE / AISI",
        classification = "Aço Baixa Liga Ni-Cr-Mo para Cementação",
        confidencePercent = 81,
        summary = "Alta tenacidade no núcleo com excelente dureza superficial após cementação; presença de pontas pontiagudas de molibdênio.",
        sparkColor = "Laranja-Avermelhado com Pontas Claras",
        streamLength = "1.2 m (Curto a Moderado)",
        burstPattern = "Ramificações Finas Dispersas com Pontas de Lança",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 0.21, "0.18 - 0.23%", "Baixo teor de base para manter núcleo dúctil após têmpera"),
            AlloyComponent("Níquel", "Ni", 0.52, "0.40 - 0.70%", "Confere tenacidade ao impacto e resistência à fratura"),
            AlloyComponent("Cromo", "Cr", 0.48, "0.40 - 0.60%", "Promove profundidade de camada cementada"),
            AlloyComponent("Molibdênio", "Mo", 0.22, "0.15 - 0.25%", "Gera pontas de centelha em formato de lança destacada"),
            AlloyComponent("Manganês", "Mn", 0.80, "0.70 - 0.90%", "Otimiza a cinética de transformação martensítica")
        ),
        characteristics = listOf(
            SparkCharacteristic("Frequência de Explosão", "98 Hz", "Densidade reduzida de explosões devido ao menor carbono"),
            SparkCharacteristic("Linhas Condutoras", "Laranja Avermelhado", "Linhas finas e bem definidas com pontas distintas"),
            SparkCharacteristic("Assinatura Molibdênio", "Ponta de Seta Desprendida", "Gotículas brilhantes que explodem separadamente")
        ),
        carbonLevel = 0.20,
        isMagnetic = true,
        alloySignatures = listOf("Spear Tip")
    )

    val sae4140 = SteelGrade(
        code = "SAE 4140",
        standard = "ABNT / SAE / AISI",
        classification = "Aço Cromo-Molibdênio Beneficiável (Alta Resistência)",
        confidencePercent = 78,
        summary = "Aço para eixos e engrenagens de alto esforço, virabrequins e bielas. Linhas finas amarelo-alaranjadas com explosões pontuais.",
        sparkColor = "Amarelo Dourado com Tom Alaranjado",
        streamLength = "1.3 m (Moderado)",
        burstPattern = "Estrelas Agrupadas com Apêndices Pontiagudos",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 0.41, "0.38 - 0.43%", "Garante resistência mecânica e dureza após beneficiamento"),
            AlloyComponent("Cromo", "Cr", 0.98, "0.80 - 1.10%", "Aumenta significativamente a temperabilidade e tenacidade"),
            AlloyComponent("Molibdênio", "Mo", 0.21, "0.15 - 0.25%", "Evita a fragilidade de revenimento e refina microestrutura"),
            AlloyComponent("Manganês", "Mn", 0.85, "0.75 - 1.00%", "Potencializa a ação do cromo na formação de carbetos"),
            AlloyComponent("Silício", "Si", 0.26, "0.15 - 0.35%", "Desoxidação e estabilidade elástica")
        ),
        characteristics = listOf(
            SparkCharacteristic("Frequência de Explosão", "125 Hz", "Explosões concentradas no terço final do feixe"),
            SparkCharacteristic("Linhas Condutoras", "Amarelo-Ouro Estável", "Feixes contínuos com pouca dispersão angular"),
            SparkCharacteristic("Dureza Estimada", "28 - 34 HRC", "Adequado para componentes dinâmicos de alta fadiga")
        ),
        carbonLevel = 0.40,
        isMagnetic = true,
        alloySignatures = listOf("Pointed appendages")
    )

    val aisi304 = SteelGrade(
        code = "AISI 304",
        standard = "ABNT / ASTM / AISI",
        classification = "Aço Inoxidável Austenítico 18/8 (Cr-Ni)",
        confidencePercent = 65,
        summary = "Extrema resistência à corrosão e não magnético. Feixe de centelhas curto e escuro, praticamente sem explosões de carbono.",
        sparkColor = "Vermelho Escuro com Pontas Palha",
        streamLength = "0.6 m (Feixe Muito Curto)",
        burstPattern = "Linhas Contínuas Suaves Sem Explosões Estreladas",
        alloys = listOf(
            AlloyComponent("Cromo", "Cr", 18.25, "18.0 - 20.0%", "Forma a camada passiva de óxido protetor contra oxidação"),
            AlloyComponent("Níquel", "Ni", 8.12, "8.0 - 10.5%", "Estabiliza a fase austenítica e confere ductilidade criogênica"),
            AlloyComponent("Manganês", "Mn", 1.45, "Max 2.00%", "Auxilia na conformabilidade mecânica a quente"),
            AlloyComponent("Silício", "Si", 0.48, "Max 1.00%", "Aumenta a fluidez na soldagem e resistência à incrustação"),
            AlloyComponent("Carbono", "C", 0.05, "Max 0.08%", "Baixo carbono para evitar precipitação de carbetos de cromo")
        ),
        characteristics = listOf(
            SparkCharacteristic("Frequência de Explosão", "12 Hz", "Praticamente sem centelhamento secundário"),
            SparkCharacteristic("Comportamento Térmico", "Alta Resistência al Calor", "Faíscas de baixa luminescência residual"),
            SparkCharacteristic("Magnetismo", "Não Magnético", "Estrutura austenítica estável à temperatura ambiente")
        ),
        carbonLevel = 0.05,
        isMagnetic = false,
        alloySignatures = emptyList()
    )

    val saeD2 = SteelGrade(
        code = "AISI D2",
        standard = "ABNT D2 / AISI / DIN 1.2379",
        classification = "Aço Ferramenta Alto Cromo Alto Carbono (Trabalho a Frio)",
        confidencePercent = 70,
        summary = "Aço de altíssima dureza e retenção de corte para matrizes e facas industriais; feixe curto avermelhado com carbetos pesados.",
        sparkColor = "Vermelho-Alaranjado Escuro",
        streamLength = "0.7 m (Feixe Curto e Denso)",
        burstPattern = "Centelhas Finas em Forma de Botões e Lanças",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 1.54, "1.40 - 1.60%", "Promove densa matriz de carbetos primários de alta dureza"),
            AlloyComponent("Cromo", "Cr", 11.85, "11.0 - 13.0%", "Garante resistência extrema à abrasão e semi-inoxidabilidade"),
            AlloyComponent("Molibdênio", "Mo", 0.82, "0.70 - 1.20%", "Aumenta a temperabilidade e tenacidade em matrizes espessas"),
            AlloyComponent("Vanádio", "V", 0.88, "0.50 - 1.10%", "Forma carbetos VC ultra-duros que previnem desgaste adesivo"),
            AlloyComponent("Manganês", "Mn", 0.38, "0.20 - 0.60%", "Desoxidação e estabilização dimensional")
        ),
        characteristics = listOf(
            SparkCharacteristic("Frequência de Explosão", "45 Hz", "Centelhas pesadas que caem rapidamente"),
            SparkCharacteristic("Linhas Condutoras", "Vermelho Escuro", "Arranjo compacto com pequenas pontas luminosas"),
            SparkCharacteristic("Dureza Típica", "58 - 62 HRC", "Máxima resistência ao desgaste abrasivo a frio")
        ),
        carbonLevel = 1.55,
        isMagnetic = true,
        alloySignatures = listOf("Buttons", "Spears")
    )

    val sae5160 = SteelGrade(
        code = "SAE 5160",
        standard = "ABNT / SAE / AISI",
        classification = "Aço Cromo-Manganês para Molas e Cutelaria",
        confidencePercent = 74,
        summary = "Excelente limite elástico e resistência à fadiga; feixe longo e volumoso com centenas de explosões estreladas secundárias.",
        sparkColor = "Amarelo Ouro Intenso e Brilhante",
        streamLength = "1.8 m (Feixe Muito Longo e Amplo)",
        burstPattern = "Chuva Densa de Estrelas Múltiplas Bifurcadas",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 0.61, "0.56 - 0.64%", "Garante alto limite elástico e resistência à deformação permanente"),
            AlloyComponent("Cromo", "Cr", 0.82, "0.70 - 0.90%", "Confere tenacidade e temperabilidade em banho de óleo"),
            AlloyComponent("Manganês", "Mn", 0.88, "0.75 - 1.00%", "Garante dureza homogênea em barras de espessura elevada"),
            AlloyComponent("Silício", "Si", 0.28, "0.15 - 0.35%", "Estabilidade elástica contra relaxamento sob carga contínua")
        ),
        characteristics = listOf(
            SparkCharacteristic("Frequência de Explosão", "178 Hz", "Detonações intensas ao longo de quase todo o feixe"),
            SparkCharacteristic("Linhas Condutoras", "Retas e Divergentes", "Abertura angular ampla das centelhas no ar"),
            SparkCharacteristic("Aplicação Clássica", "Molas Helicoidais / Cutelaria", "Capacidade de absorção de choque sem trinca")
        ),
        carbonLevel = 0.60,
        isMagnetic = true,
        alloySignatures = listOf("Dense stars")
    )

    val wroughtIron = SteelGrade(
        code = "Wrought Iron",
        standard = "Historical / ASTM",
        classification = "Ferro Pudlado (Wrought Iron)",
        confidencePercent = 90,
        summary = "Ferro com baixo carbono e inclusões de escória; feixe longo e amarelo palha sem explosões.",
        sparkColor = "Amarelo Palha",
        streamLength = "1.6 m",
        burstPattern = "Nenhuma explosão",
        alloys = listOf(AlloyComponent("Carbono", "C", 0.05, "< 0.08%", "Baixíssimo teor de carbono")),
        characteristics = listOf(SparkCharacteristic("Tipo de Feixe", "Longo e Contínuo", "Ausência de carbono impede explosões")),
        carbonLevel = 0.05,
        isMagnetic = true,
        alloySignatures = listOf("White Block")
    )

    val sae1020 = SteelGrade(
        code = "SAE 1020",
        standard = "ABNT / SAE / AISI",
        classification = "Aço de Baixo Carbono",
        confidencePercent = 95,
        summary = "Aço versátil para construção; feixe longo com poucas explosões bifurcadas.",
        sparkColor = "White-Straw",
        streamLength = "~1.6m",
        burstPattern = "Poucas explosões pequenas e bifurcadas",
        alloys = listOf(AlloyComponent("Carbono", "C", 0.20, "0.18 - 0.23%", "Base para ductilidade")),
        characteristics = listOf(SparkCharacteristic("Frequência", "Baixa", "Poucas ramificações")),
        carbonLevel = 0.15,
        isMagnetic = true,
        alloySignatures = listOf("Long straight lines", "Forked bursts")
    )

    val sae1095 = SteelGrade(
        code = "SAE 1095",
        standard = "ABNT / SAE / AISI / W1",
        classification = "Aço de Alto Carbono",
        confidencePercent = 92,
        summary = "Aço para ferramentas e cutelaria; feixe curto com explosões intensas e densas.",
        sparkColor = "Branco Brilhante",
        streamLength = "0.8 m",
        burstPattern = "Explosões intensas, densas e em forma de estrela",
        alloys = listOf(AlloyComponent("Carbono", "C", 0.95, "0.90 - 1.03%", "Alta dureza")),
        characteristics = listOf(SparkCharacteristic("Frequência", "Muito Alta", "Densa nuvem de faíscas")),
        carbonLevel = 0.95,
        isMagnetic = true,
        alloySignatures = listOf("Bushy stars")
    )

    val sae4340 = SteelGrade(
        code = "SAE 4340",
        standard = "ABNT / SAE / AISI",
        classification = "Aço Ni-Cr-Mo de Alta Resistência",
        confidencePercent = 88,
        summary = "Aço ligado para aplicações críticas; feixe médio com linhas escuras e explosões finas.",
        sparkColor = "Palha/Branco",
        streamLength = "1.2 m",
        burstPattern = "Explosões finas e pequenas, linhas escuras",
        alloys = listOf(AlloyComponent("Níquel", "Ni", 1.85, "1.65 - 2.00%", "Tenacidade")),
        characteristics = listOf(SparkCharacteristic("Assinatura", "Linhas Escuras", "Presença de elementos de liga")),
        carbonLevel = 0.40,
        isMagnetic = true,
        alloySignatures = listOf("Dark lines")
    )

    val aisiO1 = SteelGrade(
        code = "AISI O1",
        standard = "ASTM / AISI",
        classification = "Aço Ferramenta para Trabalho a Frio",
        confidencePercent = 85,
        summary = "Aço temperável em óleo; feixe médio-curto com 'espinhos' nas pontas.",
        sparkColor = "Amarelo/Laranja",
        streamLength = "0.9 m",
        burstPattern = "Espinhos nas extremidades ('thorns')",
        alloys = listOf(AlloyComponent("Manganês", "Mn", 1.20, "1.00 - 1.40%", "Estabilidade")),
        characteristics = listOf(SparkCharacteristic("Ponta", "Espinhos", "Assinatura clássica de tool steel")),
        carbonLevel = 0.95,
        isMagnetic = true,
        alloySignatures = listOf("Thorns")
    )

    val aisiH13 = SteelGrade(
        code = "AISI H13",
        standard = "ASTM / AISI",
        classification = "Aço Ferramenta para Trabalho a Quente",
        confidencePercent = 82,
        summary = "Resistência ao calor; feixe laranja-avermelhado com fluxos finos e raros estouros.",
        sparkColor = "Laranja-Avermelhado",
        streamLength = "1.0 m",
        burstPattern = "Fluxos finos e fracos, explosões raras",
        alloys = listOf(AlloyComponent("Cromo", "Cr", 5.00, "4.75 - 5.50%", "Resistência ao calor")),
        characteristics = listOf(SparkCharacteristic("Fluxo", "Fino", "Centelha discreta")),
        carbonLevel = 0.40,
        isMagnetic = true,
        alloySignatures = listOf("Thin streams")
    )

    val aisiM2 = SteelGrade(
        code = "AISI M2",
        standard = "ASTM / AISI / HSS",
        classification = "Aço Rápido (High Speed Steel)",
        confidencePercent = 80,
        summary = "Alta dureza a quente; feixe curto com listras vermelhas foscas e poucas explosões.",
        sparkColor = "Vermelho/Laranja Escuro",
        streamLength = "0.6 m",
        burstPattern = "Listras vermelhas foscas, explosões muito finas ou ausentes",
        alloys = listOf(AlloyComponent("Tungstênio", "W", 6.00, "5.50 - 6.75%", "Dureza ao rubro")),
        characteristics = listOf(SparkCharacteristic("Cor", "Vermelho Fosco", "Tungstênio/Molibdênio")),
        carbonLevel = 0.85,
        isMagnetic = true,
        alloySignatures = listOf("Dull Red", "Dull red streaks")
    )

    val aisi420 = SteelGrade(
        code = "AISI 420",
        standard = "ASTM / AISI",
        classification = "Aço Inoxidável Martensítico",
        confidencePercent = 75,
        summary = "Inox temperável; feixe médio com menos explosões que aços carbono equivalentes.",
        sparkColor = "Branco/Amarelo",
        streamLength = "1.1 m",
        burstPattern = "Menos explosões que o aço carbono",
        alloys = listOf(AlloyComponent("Cromo", "Cr", 13.00, "12.0 - 14.0%", "Inoxidável")),
        characteristics = listOf(SparkCharacteristic("Magnetismo", "Magnético", "Estrutura martensítica")),
        carbonLevel = 0.30,
        isMagnetic = true,
        alloySignatures = emptyList()
    )

    val castIron = SteelGrade(
        code = "Cast Iron",
        standard = "ASTM / ABNT",
        classification = "Ferro Fundido Cinzento",
        confidencePercent = 88,
        summary = "Alto carbono e silício; feixe muito curto com explosões repetitivas pequenas.",
        sparkColor = "Vermelho a Laranja",
        streamLength = "0.6 m",
        burstPattern = "Pequenas explosões repetitivas",
        alloys = listOf(AlloyComponent("Carbono", "C", 2.00, "> 2.0%", "Alto teor de carbono")),
        characteristics = listOf(SparkCharacteristic("Comprimento", "Muito Curto", "Alto carbono satura a centelha")),
        carbonLevel = 2.00,
        isMagnetic = true,
        alloySignatures = listOf("Repetitive bursts")
    )

    val unidentified = SteelGrade(
        code = "N/A",
        standard = "---",
        classification = "Material não Encontrado",
        confidencePercent = 0,
        summary = "As características visuais capturadas não correspondem a nenhum padrão de faíscas de aço conhecido na base de dados.",
        sparkColor = "---",
        streamLength = "---",
        burstPattern = "---",
        alloys = emptyList(),
        characteristics = emptyList(),
        carbonLevel = 0.0,
        isMagnetic = false,
        alloySignatures = emptyList()
    )

    val grade16MnCr5 = SteelGrade(
        code = "16MnCr5",
        standard = "DIN / ISO",
        classification = "Aço Cementação Cr-Mn",
        confidencePercent = 86,
        summary = "Aço para cementação com boa temperabilidade superficial; feixe com ramificações finas e fluxo suprimido.",
        sparkColor = "Orange-Straw",
        streamLength = "~1.2m",
        burstPattern = "Fine forks with suppressed stream",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 0.16, "0.14 - 0.19%", "Baixo carbono para núcleo tenaz"),
            AlloyComponent("Manganês", "Mn", 1.15, "1.00 - 1.30%", "Melhora a temperabilidade"),
            AlloyComponent("Cromo", "Cr", 0.95, "0.80 - 1.10%", "Resistência ao desgaste")
        ),
        characteristics = listOf(
            SparkCharacteristic("Fluxo", "Suprimido", "Menor volume de faíscas devido aos elementos de liga"),
            SparkCharacteristic("Ramificação", "Garfos Finos", "Bifurcações discretas na ponta")
        ),
        carbonLevel = 0.16,
        isMagnetic = true,
        alloySignatures = listOf("Suppressed stream", "Fine forks")
    )

    val vc140 = SteelGrade(
        code = "VC-140",
        standard = "ABNT",
        classification = "Aço Liga Cr para Construção Mecânica",
        confidencePercent = 84,
        summary = "Aço de alta resistência mecânica; feixe amarelado com explosões em formato de flor única.",
        sparkColor = "Yellow-Orange",
        streamLength = "~1.4m",
        burstPattern = "Single flower bursts",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 0.40, "0.36 - 0.44%", "Resistência mecânica"),
            AlloyComponent("Cromo", "Cr", 1.00, "0.90 - 1.20%", "Temperabilidade")
        ),
        characteristics = listOf(
            SparkCharacteristic("Explosão", "Flor Única", "Padrão de explosão concêntrico e limpo")
        ),
        carbonLevel = 0.40,
        isMagnetic = true,
        alloySignatures = listOf("Single flower bursts")
    )

    val vc130 = SteelGrade(
        code = "VC-130",
        standard = "ABNT / AISI D3",
        classification = "Aço Ferramenta Alto Cr Alto C (D3)",
        confidencePercent = 82,
        summary = "Aço ferramenta de altíssima dureza; feixe curto com faíscas aderentes e aglomerados densos.",
        sparkColor = "Dull Orange",
        streamLength = "~0.6m",
        burstPattern = "Bushy clusters and adhering sparks",
        alloys = listOf(
            AlloyComponent("Carbono", "C", 2.00, "1.90 - 2.20%", "Alta dureza"),
            AlloyComponent("Cromo", "Cr", 12.00, "11.0 - 13.0%", "Resistência à abrasão")
        ),
        characteristics = listOf(
            SparkCharacteristic("Aderência", "Alta", "Faíscas que parecem 'grudar' no rebolo"),
            SparkCharacteristic("Densidade", "Clusters Arbustivos", "Explosões densas e volumosas em espaço curto")
        ),
        carbonLevel = 2.0,
        isMagnetic = true,
        alloySignatures = listOf("Bushy clusters", "Adhering sparks")
    )

    val allGrades = listOf(
        sae1045, sae8620, sae4140, aisi304, saeD2, sae5160,
        wroughtIron, sae1020, sae1095, sae4340, aisiO1, aisiH13, aisiM2, aisi420, castIron,
        grade16MnCr5, vc140, vc130
    )
}

