package com.example.model

enum class AppLanguage(
    val code: String,
    val displayName: String,
    val country: String,
    val flag: String,
    val standardCode: String
) {
    PT("pt-BR", "Português", "Brasil", "🇧🇷", "ABNT"),
    EN("en-US", "English", "United States", "🇺🇸", "AISI / SAE"),
    ES("es-ES", "Español", "España / Latam", "🇪🇸", "UNE / AISI"),
    DE("de-DE", "Deutsch", "Deutschland", "🇩🇪", "DIN / W-Nr."),
    JA("ja-JP", "日本語", "日本", "🇯🇵", "JIS")
}

data class UiStrings(
    val appSubtitle: String,
    val recalibrateTooltip: String,
    val languageSelectTitle: String,
    val languageSelectDesc: String,
    val analyzingTitle: String,
    val pausedTitle: String,
    val cvRealtime: String,
    val frameFrozen: String,
    val sparkOverlayActive: String,
    val sparkOverlayFrozen: String,
    val capturePhoto: String,
    val captureVideo: String,
    val stopRecording: String,
    val pickFromGallery: String,
    val photoMode: String,
    val videoMode: String,
    val detectedSteelTitle: String,
    val instantRecognitionBadge: String,
    val matchAccuracy: String,
    val chemicalCompositionTitle: String,
    val measuredTheory: String,
    val sparkDiagnosticsTitle: String,
    val lockAndExportCert: String,
    val freezeAnalysisFrame: String,
    val resumeLiveSpectrometry: String,
    val certificateDialogTitle: String,
    val certificateClose: String,
    val lockedSample: String,
    val classificationLabel: String,
    val confidenceLabel: String,
    val inRangeTag: String,
    val specRange: String,
    val sparkColorLabel: String,
    val streamLengthLabel: String,
    val burstPatternLabel: String,
    val recordedVideoAnalysis: String,
    val photoCapturedSuccess: String,
    val unidentifiedMaterial: String
)

object LocalizationManager {
    fun getStrings(lang: AppLanguage): UiStrings = when (lang) {
        AppLanguage.PT -> UiStrings(
            appSubtitle = "IDENTIFICADOR DE AÇOS • SPECTRO-CV",
            recalibrateTooltip = "Recalibrar Ensaio",
            languageSelectTitle = "SELECIONAR IDIOMA / NORMA",
            languageSelectDesc = "Ajuste os parâmetros textuais e normas metalúrgicas regionais:",
            analyzingTitle = "ANALISANDO MATERIAL...",
            pausedTitle = "LEITURA PAUSADA",
            cvRealtime = "CV: EM TEMPO REAL",
            frameFrozen = "QUADRO FIXADO",
            sparkOverlayActive = "ESPECTROMETRIA CV ATIVA",
            sparkOverlayFrozen = "QUADRO CONGELADO",
            capturePhoto = "ANALISE",
            captureVideo = "ANALISAR VÍDEO",
            stopRecording = "FINALIZAR E ANALISAR",
            pickFromGallery = "MÍDIA / GALERIA",
            photoMode = "FOTO",
            videoMode = "VÍDEO",
            detectedSteelTitle = "TIPO DE AÇO DETECTADO",
            instantRecognitionBadge = "RECONHECIMENTO ÓPTICO CV",
            matchAccuracy = "PRECISÃO",
            chemicalCompositionTitle = "COMPOSIÇÃO QUÍMICA DA LIGA",
            measuredTheory = "TEOR MEDIDO CV",
            sparkDiagnosticsTitle = "TELEMETRIA DO ENSAIO DE FAÍSCAS",
            lockAndExportCert = "BLOQUEAR E EMITIR LAUDO TÉCNICO",
            freezeAnalysisFrame = "CONGELAR QUADRO DE ANÁLISE",
            resumeLiveSpectrometry = "RETOMAR ESPECTROMETRIA AO VIVO",
            certificateDialogTitle = "LAUDO METALÚRGICO DIGITAL",
            certificateClose = "CONCLUIR",
            lockedSample = "Amostra Bloqueada",
            classificationLabel = "Classificação",
            confidenceLabel = "Confiabilidade",
            inRangeTag = "CONFORME",
            specRange = "Faixa Nom.",
            sparkColorLabel = "COR DA CENTELHA",
            streamLengthLabel = "COMPRIMENTO DO FEIXE",
            burstPatternLabel = "PADRÃO DE EXPLOSÃO",
            recordedVideoAnalysis = "VÍDEO ANALISADO COM SUCESSO",
            photoCapturedSuccess = "FOTO ANALISADA VIA ESPECTROMETRIA",
            unidentifiedMaterial = "Material não Identificado"
        )
        AppLanguage.EN -> UiStrings(
            appSubtitle = "STEEL IDENTIFIER • SPECTRO-CV",
            recalibrateTooltip = "Recalibrate Assay",
            languageSelectTitle = "SELECT LANGUAGE / STANDARD",
            languageSelectDesc = "Choose your preferred region and metallurgical standard:",
            analyzingTitle = "ANALYZING MATERIAL...",
            pausedTitle = "SCAN PAUSED",
            cvRealtime = "CV: LIVE TELEMETRY",
            frameFrozen = "FRAME FROZEN",
            sparkOverlayActive = "CV SPECTROMETRY ACTIVE",
            sparkOverlayFrozen = "FRAME LOCKED",
            capturePhoto = "ANALYZE",
            captureVideo = "ANALYZE VIDEO",
            stopRecording = "STOP & ANALYZE",
            pickFromGallery = "MEDIA / GALLERY",
            photoMode = "PHOTO",
            videoMode = "VIDEO",
            detectedSteelTitle = "DETECTED STEEL GRADE",
            instantRecognitionBadge = "OPTICAL CV RECOGNITION",
            matchAccuracy = "ACCURACY",
            chemicalCompositionTitle = "CHEMICAL ALLOY COMPOSITION",
            measuredTheory = "CV MEASURED",
            sparkDiagnosticsTitle = "SPARK TEST TELEMETRY",
            lockAndExportCert = "LOCK & EXPORT CERTIFICATE",
            freezeAnalysisFrame = "FREEZE ANALYSIS FRAME",
            resumeLiveSpectrometry = "RESUME LIVE SPECTROMETRY",
            certificateDialogTitle = "DIGITAL METALLURGICAL REPORT",
            certificateClose = "DONE",
            lockedSample = "Locked Sample",
            classificationLabel = "Classification",
            confidenceLabel = "Confidence",
            inRangeTag = "IN RANGE",
            specRange = "Spec Range",
            sparkColorLabel = "SPARK COLOR",
            streamLengthLabel = "STREAM LENGTH",
            burstPatternLabel = "BURST PATTERN",
            recordedVideoAnalysis = "VIDEO PROCESSED SUCCESSFULLY",
            photoCapturedSuccess = "PHOTO ANALYZED VIA SPECTROMETRY",
            unidentifiedMaterial = "Unidentified Material"
        )
        AppLanguage.ES -> UiStrings(
            appSubtitle = "IDENTIFICADOR DE ACEROS • SPECTRO-CV",
            recalibrateTooltip = "Recalibrar Ensayo",
            languageSelectTitle = "SELECCIONAR IDIOMA / NORMA",
            languageSelectDesc = "Ajuste el idioma y la norma metalúrgica aplicable:",
            analyzingTitle = "ANALIZANDO MATERIAL...",
            pausedTitle = "LECTURA EN PAUSA",
            cvRealtime = "CV: TIEMPO REAL",
            frameFrozen = "CUADRO FIJADO",
            sparkOverlayActive = "ESPECTROMETRÍA CV ACTIVA",
            sparkOverlayFrozen = "CUADRO CONGELADO",
            capturePhoto = "ANALIZAR",
            captureVideo = "ANALIZAR VÍDEO",
            stopRecording = "FINALIZAR Y ANALIZAR",
            pickFromGallery = "MEDIOS / GALERÍA",
            photoMode = "FOTO",
            videoMode = "VÍDEO",
            detectedSteelTitle = "TIPO DE ACERO DETECTADO",
            instantRecognitionBadge = "RECONOCIMIENTO ÓPTICO CV",
            matchAccuracy = "PRECISIÓN",
            chemicalCompositionTitle = "COMPOSICIÓN QUÍMICA DE LA ALEACIÓN",
            measuredTheory = "VALOR MEDIDO CV",
            sparkDiagnosticsTitle = "TELEMETRÍA DEL ENSAYO DE CHISPAS",
            lockAndExportCert = "BLOQUEAR Y EMITIR CERTIFICADO",
            freezeAnalysisFrame = "CONGELAR CUADRO DE ANÁLISIS",
            resumeLiveSpectrometry = "REANUDAR ESPECTROMETRÍA EN VIVO",
            certificateDialogTitle = "CERTIFICADO METALÚRGICO DIGITAL",
            certificateClose = "CERRAR",
            lockedSample = "Muestra Bloqueada",
            classificationLabel = "Clasificación",
            confidenceLabel = "Confiabilidad",
            inRangeTag = "CONFORME",
            specRange = "Rango Nom.",
            sparkColorLabel = "COLOR DE CHISPA",
            streamLengthLabel = "LONGITUD DE HAZ",
            burstPatternLabel = "PATRÓN DE ESTALLIDO",
            recordedVideoAnalysis = "VÍDEO ANALIZADO CON ÉXITO",
            photoCapturedSuccess = "FOTO ANALIZADA POR ESPECTROMETRÍA",
            unidentifiedMaterial = "Material no Identificado"
        )
        AppLanguage.DE -> UiStrings(
            appSubtitle = "STAHL-IDENTIFIKATOR • SPECTRO-CV",
            recalibrateTooltip = "Prüfung Neu Kalibrieren",
            languageSelectTitle = "SPRACHE / NORM WÄHLEN",
            languageSelectDesc = "Wählen Sie regionale metallurgische Normen und Sprache:",
            analyzingTitle = "MATERIAL WIRD ANALYSIERT...",
            pausedTitle = "MESSUNG PAUSIERT",
            cvRealtime = "CV: ECHTZEIT-STREAM",
            frameFrozen = "BILD EINGEFROREN",
            sparkOverlayActive = "CV-SPEKTROMETRIE AKTIV",
            sparkOverlayFrozen = "BILD FIXIERT",
            capturePhoto = "ANALYSIEREN",
            captureVideo = "VIDEO ANALYSIEREN",
            stopRecording = "BEENDEN & ANALYSIEREN",
            pickFromGallery = "MEDIEN / GALERIE",
            photoMode = "FOTO",
            videoMode = "VIDEO",
            detectedSteelTitle = "ERKANNTES STAHLMATERIAL",
            instantRecognitionBadge = "OPTISCHE CV-ERKENNUNG",
            matchAccuracy = "GENAUIGKEIT",
            chemicalCompositionTitle = "CHEMISCHE LEGIERUNGSZUSAMMENSETZUNG",
            measuredTheory = "CV-GEMESSEN",
            sparkDiagnosticsTitle = "FUNKENPROBEN-TELEMETRIE",
            lockAndExportCert = "SPERREN & ZERTIFIKAT ERSTELLEN",
            freezeAnalysisFrame = "ANALYSEBILD EINFRIEREN",
            resumeLiveSpectrometry = "ECHTZEIT-MESSUNG FORTSETZEN",
            certificateDialogTitle = "DIGITALES WERKSTOFFZEUGNIS",
            certificateClose = "FERTIG",
            lockedSample = "Gesperrte Probe",
            classificationLabel = "Klassifikation",
            confidenceLabel = "Zuverlässigkeit",
            inRangeTag = "KONFORM",
            specRange = "Sollbereich",
            sparkColorLabel = "FUNKENFARBE",
            streamLengthLabel = "STRAHLLÄNGE",
            burstPatternLabel = "EXPLOSIONSMUSTER",
            recordedVideoAnalysis = "VIDEO ERFOLGREICH ANALYSIERT",
            photoCapturedSuccess = "FOTO PER SPEKTROMETRIE ANALYSIERT",
            unidentifiedMaterial = "Nicht identifiziertes Material"
        )
        AppLanguage.JA -> UiStrings(
            appSubtitle = "鋼種識別アナライザー • SPECTRO-CV",
            recalibrateTooltip = "再キャリブレーション",
            languageSelectTitle = "言語・規格の選択",
            languageSelectDesc = "冶金規格と言語を選択してください:",
            analyzingTitle = "材料を分析中...",
            pausedTitle = "分析一時停止",
            cvRealtime = "CV: リアルタイム",
            frameFrozen = "フレーム固定中",
            sparkOverlayActive = "CV分光計測アクティブ",
            sparkOverlayFrozen = "画像ロック中",
            capturePhoto = "分析する",
            captureVideo = "動画を分析する",
            stopRecording = "停止して分析",
            pickFromGallery = "写真・動画を選択",
            photoMode = "写真",
            videoMode = "動画",
            detectedSteelTitle = "検出された鋼種",
            instantRecognitionBadge = "光学CV自動認識",
            matchAccuracy = "適合精度",
            chemicalCompositionTitle = "合金化学成分",
            measuredTheory = "CV実測値",
            sparkDiagnosticsTitle = "火花試験テレメトリー",
            lockAndExportCert = "ロックして検査成績書を発行",
            freezeAnalysisFrame = "分析フレームを静止",
            resumeLiveSpectrometry = "リアルタイム分析を再開",
            certificateDialogTitle = "デジタル鋼材検査成績書",
            certificateClose = "完了",
            lockedSample = "検査対象鋼材",
            classificationLabel = "分類",
            confidenceLabel = "信頼度",
            inRangeTag = "適合",
            specRange = "規格範囲",
            sparkColorLabel = "火花の色",
            streamLengthLabel = "火花の長さ",
            burstPatternLabel = "破裂形態",
            recordedVideoAnalysis = "動画分析が完了しました",
            photoCapturedSuccess = "写真分光分析が完了しました",
            unidentifiedMaterial = "未確認の材料"
        )
    }

    fun getLocalizedClassification(grade: SteelGrade, lang: AppLanguage): String = when (lang) {
        AppLanguage.PT -> grade.classification
        AppLanguage.EN -> when (grade.code) {
            "SAE 1045" -> "Medium Carbon Steel for Mechanical Construction"
            "SAE 8620" -> "Low Alloy Ni-Cr-Mo Carburizing Steel"
            "SAE 4140" -> "High Strength Cr-Mo Alloy Quenched & Tempered Steel"
            "AISI 304" -> "Austenitic Stainless Steel 18/8 (Cr-Ni)"
            "AISI D2" -> "High Carbon High Chromium Cold Work Tool Steel"
            "SAE 5160" -> "Chromium Spring & Cutlery High Fatigue Steel"
            "Wrought Iron" -> "Wrought Iron (Low Slag Content)"
            "SAE 1020" -> "Low Carbon Structural Steel"
            "SAE 1095" -> "High Carbon Steel / W1 Tool Steel"
            "SAE 4340" -> "High Strength Ni-Cr-Mo Alloy Steel"
            "AISI O1" -> "Oil-Hardening Cold Work Tool Steel"
            "AISI H13" -> "Hot-Work Tool Steel"
            "AISI M2" -> "High-Speed Steel (M2)"
            "AISI 420" -> "Martensitic Stainless Steel"
            "Cast Iron" -> "Gray Cast Iron"
            "N/A" -> "Unidentified Material"
            else -> grade.classification
        }
        AppLanguage.ES -> when (grade.code) {
            "SAE 1045" -> "Acero Medio Carbono para Construcción Mecánica"
            "SAE 8620" -> "Acero Baja Aleación Ni-Cr-Mo para CementACIÓN"
            "SAE 4140" -> "Acero Cromo-Molibdeno de Alta Resistencia"
            "AISI 304" -> "Acero Inoxidable Austenítico 18/8 (Cr-Ni)"
            "AISI D2" -> "Acero para Herramientas Alto Carbono Alto Cromo"
            "SAE 5160" -> "Acero Cromo-Manganeso para Ballestas y Cuchillería"
            "Wrought Iron" -> "Hierro Pudelado"
            "SAE 1020" -> "Acero de Bajo Carbono"
            "SAE 1095" -> "Acero de Alto Carbono"
            "SAE 4340" -> "Acero Ni-Cr-Mo de Alta Resistencia"
            "AISI O1" -> "Acero para Herramientas de Temple en Aceite"
            "AISI H13" -> "Acero para Trabajo en Caliente"
            "AISI M2" -> "Acero Rápido (HSS)"
            "AISI 420" -> "Acero Inoxidable Martensítico"
            "Cast Iron" -> "Hierro Fundido Gris"
            "N/A" -> "Material não Identificado"
            else -> grade.classification
        }
        AppLanguage.DE -> when (grade.code) {
            "SAE 1045" -> "Vergütungsstahl C45E (DIN 1.1191)"
            "SAE 8620" -> "Einsatzstahl 21NiCrMo2 (DIN 1.6523)"
            "SAE 4140" -> "Vergütungsstahl 42CrMo4 (DIN 1.7225)"
            "AISI 304" -> "Austenitischer Edelstahl X5CrNi18-10 (DIN 1.4301)"
            "AISI D2" -> "Kaltarbeitsstahl X153CrMoV12 (DIN 1.2379)"
            "SAE 5160" -> "Federstahl 55Cr3 (DIN 1.7176)"
            "Wrought Iron" -> "Schmiedeeisen / Puddeleisen"
            "SAE 1020" -> "Baustahl C22 (DIN 1.0402)"
            "SAE 1095" -> "Kohlenstoffstahl C105U (DIN 1.1545)"
            "SAE 4340" -> "CrNiMo-Stahl 34CrNiMo6 (DIN 1.6582)"
            "AISI O1" -> "Kaltarbeitsstahl 90MnCrV8 (DIN 1.2510)"
            "AISI H13" -> "Warmarbeitsstahl X40CrMoV5-1 (DIN 1.2344)"
            "AISI M2" -> "Schnellarbeitsstahl HS6-5-2 (DIN 1.3343)"
            "AISI 420" -> "Edelstahl X20Cr13 (DIN 1.4021)"
            "Cast Iron" -> "Grauguss GG-25 (DIN 1691)"
            "N/A" -> "Nicht identifiziertes Material"
            else -> grade.classification
        }
        AppLanguage.JA -> when (grade.code) {
            "SAE 1045" -> "機械構造用炭素鋼 (JIS S45C)"
            "SAE 8620" -> "ニッケルクロムモリブデン鋼 (JIS SNCM220)"
            "SAE 4140" -> "クロムモリブデン鋼 (JIS SCM440)"
            "AISI 304" -> "オーステナイト系ステンレス鋼 (JIS SUS304)"
            "AISI D2" -> "冷間金型用合金工具鋼 (JIS SKD11)"
            "SAE 5160" -> "ばね鋼・刃物用高張力鋼 (JIS SUP9)"
            "Wrought Iron" -> "錬鉄 (Rentetsu)"
            "SAE 1020" -> "低炭素鋼 (JIS S20C)"
            "SAE 1095" -> "高炭素鋼 (JIS SK95)"
            "SAE 4340" -> "ニッケルクロムモリブデン鋼 (JIS SNCM439)"
            "AISI O1" -> "合金工具鋼 (JIS SKS3)"
            "AISI H13" -> "熱間金型用鋼 (JIS SKD61)"
            "AISI M2" -> "高速度工具鋼 (JIS SKH51)"
            "AISI 420" -> "マルテンサイト系ステンレス鋼 (JIS SUS420)"
            "Cast Iron" -> "ねずみ鋳鉄 (JIS FC250)"
            "N/A" -> "未確認の材料"
            else -> grade.classification
        }
    }

    fun getLocalizedStandard(grade: SteelGrade, lang: AppLanguage): String = when (lang) {
        AppLanguage.PT -> grade.standard
        AppLanguage.EN -> "AISI / ASTM / SAE"
        AppLanguage.ES -> "UNE / AISI / ASTM"
        AppLanguage.DE -> when (grade.code) {
            "SAE 1045" -> "DIN EN 10083 (1.1191)"
            "SAE 8620" -> "DIN EN 10084 (1.6523)"
            "SAE 4140" -> "DIN EN 10083 (1.7225)"
            "AISI 304" -> "DIN EN 10088 (1.4301)"
            "AISI D2" -> "DIN EN ISO 4957 (1.2379)"
            "SAE 5160" -> "DIN EN 10089 (1.7176)"
            "Wrought Iron" -> "DIN 17100"
            "SAE 1020" -> "DIN EN 10083 (1.0402)"
            "SAE 1095" -> "DIN EN ISO 4957 (1.1545)"
            "SAE 4340" -> "DIN EN 10083 (1.6582)"
            "AISI O1" -> "DIN EN ISO 4957 (1.2510)"
            "AISI H13" -> "DIN EN ISO 4957 (1.2344)"
            "AISI M2" -> "DIN EN ISO 4957 (1.3343)"
            "AISI 420" -> "DIN EN 10088 (1.4021)"
            "Cast Iron" -> "DIN EN 1561"
            else -> "DIN / ISO"
        }
        AppLanguage.JA -> when (grade.code) {
            "SAE 1045" -> "JIS G 4051 (S45C)"
            "SAE 8620" -> "JIS G 4053 (SNCM220)"
            "SAE 4140" -> "JIS G 4053 (SCM440)"
            "AISI 304" -> "JIS G 4303 (SUS304)"
            "AISI D2" -> "JIS G 4404 (SKD11)"
            "SAE 5160" -> "JIS G 4801 (SUP9)"
            "Wrought Iron" -> "JIS G 3101 (equiv)"
            "SAE 1020" -> "JIS G 4051 (S20C)"
            "SAE 1095" -> "JIS G 4401 (SK95)"
            "SAE 4340" -> "JIS G 4053 (SNCM439)"
            "AISI O1" -> "JIS G 4404 (SKS3)"
            "AISI H13" -> "JIS G 4404 (SKD61)"
            "AISI M2" -> "JIS G 4403 (SKH51)"
            "AISI 420" -> "JIS G 4303 (SUS420)"
            "Cast Iron" -> "JIS G 5501 (FC250)"
            else -> "JIS G Standard"
        }
    }

}
