package com.example

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.ProDashboardScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import com.example.data.AnalysisEntity
import com.example.data.AppDatabase
import com.example.data.MoshiHelper
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.SteelAnalysisEngine
import com.example.model.VisualFeatures
import com.example.model.AppLanguage
import com.example.model.LocalizationManager
import com.example.model.SteelGrade
import com.example.model.SteelPresets
import com.example.ui.components.AlloyComponentCard
import com.example.ui.components.SparkViewfinder
import com.example.ui.components.SteelCaptureSection
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SparkOrange
import com.example.ui.theme.SparkYellow
import com.example.ui.theme.SpotifyBlack
import com.example.ui.theme.TextDim
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                SteelIdentifierApp()
            }
        }
    }
}

@Composable
fun SteelIdentifierApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainAppScreen(onNavigateToPro = { navController.navigate("pro") })
        }
        composable("pro") {
            ProDashboardScreen(onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun MainAppScreen(onNavigateToPro: () -> Unit) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    var currentLanguage by remember { mutableStateOf(AppLanguage.PT) }
    val strings = remember(currentLanguage) { LocalizationManager.getStrings(currentLanguage) }

    var isScanning by remember { mutableStateOf(true) }
    var isAnalyzing by remember { mutableStateOf(false) }
    var torchOn by remember { mutableStateOf(false) }
    var selectedGrade by remember { mutableStateOf(SteelPresets.sae1045) }
    var capturedMediaUri by remember { mutableStateOf<Uri?>(null) }
    var isAnalysisComplete by remember { mutableStateOf(false) }
    var showCertificateDialog by remember { mutableStateOf(false) }
    var showLanguageDialog by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_radar")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Timer de 3 minutos para voltar ao estado inicial
    LaunchedEffect(isAnalysisComplete) {
        if (isAnalysisComplete) {
            delay(180000L) // 3 minutos (180s * 1000)
            isScanning = true
            isAnalysisComplete = false
            isAnalyzing = false
            selectedGrade = SteelPresets.sae1045
            capturedMediaUri = null
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("steel_identifier_screen"),
        containerColor = SpotifyBlack
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1B1B1B),
                            SpotifyBlack,
                            PureBlack
                        )
                    )
                )
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Top App Header com Logotipo Marcante e Mini Botão de Idiomas/Países
                item {
                    AppHeader(
                        currentLanguage = currentLanguage,
                        onResetScan = {
                            isScanning = true
                            isAnalysisComplete = false
                            isAnalyzing = false
                            selectedGrade = SteelPresets.sae1045
                        },
                        onOpenLanguagePicker = {
                            showLanguageDialog = true
                        },
                        onNavigateToPro = onNavigateToPro
                    )
                }

                // Visor Superior de Escaneamento em Tempo Real (Spark Test + CV Overlay)
                item {
                    SparkViewfinder(
                        isScanning = isScanning,
                        torchOn = torchOn,
                        isAnalysisComplete = isAnalysisComplete,
                        capturedMediaUri = capturedMediaUri,
                        grade = selectedGrade,
                        language = currentLanguage,
                        onToggleTorch = { torchOn = !torchOn },
                        modifier = Modifier.testTag("spark_viewfinder")
                    )
                }

                // =========================================================================
                // BOTÃO PARA CAPTURA DE FOTO OU VÍDEO DO AÇO
                // E LOGO ABAIXO: MOSTRA DE QUE TIPO É O AÇO
                // =========================================================================
                item {
                    SteelCaptureSection(
                        grade = selectedGrade,
                        language = currentLanguage,
                        isAnalyzing = isAnalyzing,
                        isAnalysisComplete = isAnalysisComplete,
                        onSampleCaptured = { newGrade, uri -> 
                            // Ativa a análise spectrométrica via Engine
                            isScanning = false
                            isAnalysisComplete = false
                            isAnalyzing = true
                            capturedMediaUri = uri

                            coroutineScope.launch {
                                delay(1500) // Delay para processar o frame capturado
                                
                                // Simulação de Detecção de Faíscas (80% chance de ser faísca, 20% de não ser)
                                val isSparkDetected = Random.nextDouble() > 0.20
                                
                                val capturedFeatures = if (isSparkDetected) {
                                    VisualFeatures(
                                        color = listOf("White", "Yellow", "Orange", "Red").random(),
                                        streamLengthMeters = Random.nextDouble(0.5, 1.8),
                                        burstFrequency = Random.nextDouble(0.0, 1.0),
                                        signatures = listOf("Bushy stars", "Spear Tip", "Thorns").shuffled().take(1)
                                    )
                                } else {
                                    // Features que resultarão em score baixo
                                    VisualFeatures(
                                        color = "Unknown",
                                        streamLengthMeters = 0.0,
                                        burstFrequency = 0.0,
                                        signatures = emptyList()
                                    )
                                }
                                
                                selectedGrade = SteelAnalysisEngine.matchGrade(capturedFeatures)
                                isAnalyzing = false
                                isAnalysisComplete = true

                                // Salvar no banco de dados
                                launch {
                                    val entity = AnalysisEntity(
                                        gradeCode = selectedGrade.code,
                                        timestamp = System.currentTimeMillis(),
                                        confidence = selectedGrade.confidencePercent,
                                        classification = selectedGrade.classification,
                                        standard = selectedGrade.standard,
                                        sparkColor = selectedGrade.sparkColor,
                                        streamLength = selectedGrade.streamLength,
                                        burstPattern = selectedGrade.burstPattern,
                                        alloysJson = MoshiHelper.toJson(selectedGrade.alloys)
                                    )
                                    database.analysisDao().insert(entity)
                                }
                            }
                        },
                        modifier = Modifier.testTag("steel_capture_section")
                    )
                }

                // Cabeçalho de Status de Análise (Inspirado no Spotify Dark)
                item {
                    AnalysisStatusHeader(
                        isScanning = isScanning,
                        pulseAlpha = pulseAlpha,
                        language = currentLanguage
                    )
                }

                // Seletor Horizontal de Tipos de Aço
                item {
                    SteelGradeSelectionRow(
                        grades = SteelPresets.allGrades,
                        selectedGrade = selectedGrade,
                        language = currentLanguage,
                        onSelectGrade = { 
                            selectedGrade = it 
                            isAnalysisComplete = true // Se selecionar manualmente, assume como completo
                            isScanning = false

                            // Salvar no banco de dados (seleção manual também gera histórico)
                            coroutineScope.launch {
                                val entity = AnalysisEntity(
                                    gradeCode = it.code,
                                    timestamp = System.currentTimeMillis(),
                                    confidence = it.confidencePercent,
                                    classification = it.classification,
                                    standard = it.standard,
                                    sparkColor = it.sparkColor,
                                    streamLength = it.streamLength,
                                    burstPattern = it.burstPattern,
                                    alloysJson = MoshiHelper.toJson(it.alloys)
                                )
                                database.analysisDao().insert(entity)
                            }
                        }
                    )
                }

                // Elementos de resultado só aparecem se a análise estiver completa
                if (isAnalysisComplete && !isAnalyzing) {
                    // Cartão Detalhado do Aço Selecionado
                    item {
                        GradeOverviewCard(
                            grade = selectedGrade,
                            language = currentLanguage
                        )
                    }

                    // Seção de Componentes Químicos da Liga
                    item {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = strings.chemicalCompositionTitle,
                                    color = TextWhite,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    letterSpacing = 1.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(
                                    text = strings.measuredTheory,
                                    color = NeonGreenBright,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }

                            selectedGrade.alloys.forEach { alloy ->
                                AlloyComponentCard(
                                    alloy = alloy,
                                    language = currentLanguage,
                                    modifier = Modifier.testTag("alloy_card_${alloy.symbol}")
                                )
                            }
                        }
                    }

                    // Telemetria do Ensaio de Faíscas
                    item {
                        SparkDiagnosticsCard(
                            grade = selectedGrade,
                            language = currentLanguage
                        )
                    }
                }

                // Controles de Ação Metalúrgica (Sem controles de mídia/música)
                item {
                    ActionControlsSection(
                        isScanning = isScanning,
                        language = currentLanguage,
                        onToggleScan = { isScanning = !isScanning },
                        onLockCertificate = { showCertificateDialog = true }
                    )
                }
            }

            // Diálogo de Emissão de Laudo Técnico Metalúrgico
            if (showCertificateDialog) {
                MaterialCertificateDialog(
                    grade = selectedGrade,
                    language = currentLanguage,
                    onDismiss = { showCertificateDialog = false }
                )
            }

            // Diálogo Seletor de Idiomas / Países (Mini botão no topo)
            if (showLanguageDialog) {
                LanguageSelectionDialog(
                    currentLanguage = currentLanguage,
                    onSelectLanguage = { selectedLang ->
                        currentLanguage = selectedLang
                        showLanguageDialog = false
                    },
                    onDismiss = { showLanguageDialog = false }
                )
            }
        }
    }
}

@Composable
fun AppHeader(
    currentLanguage: AppLanguage,
    onResetScan: () -> Unit,
    onOpenLanguagePicker: () -> Unit,
    onNavigateToPro: () -> Unit
) {
    val strings = remember(currentLanguage) { LocalizationManager.getStrings(currentLanguage) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Logotipo Chamativo gerado
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .border(
                        width = 1.5.dp,
                        brush = Brush.linearGradient(
                            listOf(NeonGreenBright, SparkOrange)
                        ),
                        shape = RoundedCornerShape(14.dp)
                    )
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_app_logo),
                    contentDescription = "Logotipo Scan Aço Identificador de Aço",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Column {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "SCAN AÇO",
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.2.sp
                    )
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0x221DB954)
                    ) {
                        Text(
                            text = "PRO",
                            color = NeonGreenBright,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
                Text(
                    text = strings.appSubtitle,
                    color = TextMuted,
                    fontSize = 10.sp,
                    letterSpacing = 0.8.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        // Grupo de Ações Superior: Botão Atualizar + Mini Botão de Idiomas / Países ao lado direito
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botão PRO DASHBOARD
            Surface(
                shape = CircleShape,
                color = NeonGreen.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f))
            ) {
                IconButton(
                    onClick = onNavigateToPro,
                    modifier = Modifier.size(38.dp).testTag("pro_dashboard_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Dashboard,
                        contentDescription = "Pro Dashboard",
                        tint = NeonGreenBright,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            // Botão de Recalibrar Análise
            Surface(
                shape = CircleShape,
                color = DarkCard,
                border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
            ) {
                IconButton(
                    onClick = onResetScan,
                    modifier = Modifier.size(38.dp).testTag("refresh_scan_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = strings.recalibrateTooltip,
                        tint = TextWhite,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }

            // Seletor de Idiomas / Países (Mini Fileira) ao lado direito do botão atualizar
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = DarkCardElevated,
                border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                        .clickable { onOpenLanguagePicker() },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    AppLanguage.values().take(3).forEach { lang ->
                        Text(text = lang.flag, fontSize = 14.sp)
                    }
                    Icon(
                        imageVector = Icons.Default.Language,
                        contentDescription = null,
                        tint = TextWhite,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LanguageSelectionDialog(
    currentLanguage: AppLanguage,
    onSelectLanguage: (AppLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    val strings = remember(currentLanguage) { LocalizationManager.getStrings(currentLanguage) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = PureBlack
                )
            ) {
                Text(strings.certificateClose, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Language,
                    contentDescription = null,
                    tint = NeonGreenBright
                )
                Text(
                    text = strings.languageSelectTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = strings.languageSelectDesc,
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                AppLanguage.values().forEach { lang ->
                    val isSelected = lang == currentLanguage
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                onSelectLanguage(lang)
                            },
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) DarkCardElevated else DarkCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.2.dp,
                            if (isSelected) NeonGreenBright else BorderSubtle
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = lang.flag,
                                    fontSize = 22.sp
                                )
                                Column {
                                    Text(
                                        text = lang.displayName,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "${lang.country} • ${lang.standardCode}",
                                        color = TextMuted,
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                }
                            }
                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = NeonGreenBright,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }
            }
        },
        containerColor = DarkSurface,
        titleContentColor = TextWhite,
        textContentColor = TextMuted,
        shape = RoundedCornerShape(22.dp)
    )
}

@Composable
fun AnalysisStatusHeader(
    isScanning: Boolean,
    pulseAlpha: Float,
    language: AppLanguage
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = DarkSurface,
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x22FFFFFF))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(
                            color = if (isScanning) NeonGreenBright.copy(alpha = pulseAlpha) else SparkOrange,
                            shape = CircleShape
                        )
                )
                Text(
                    text = if (isScanning) strings.analyzingTitle else strings.pausedTitle,
                    color = TextWhite,
                    fontSize = 14.5.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.1.sp
                )
            }

            Text(
                text = if (isScanning) strings.cvRealtime else strings.frameFrozen,
                color = if (isScanning) NeonGreenBright else SparkOrange,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun SteelGradeSelectionRow(
    grades: List<SteelGrade>,
    selectedGrade: SteelGrade,
    language: AppLanguage,
    onSelectGrade: (SteelGrade) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "CLASSES DE AÇOS ENCONTRADOS",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "${grades.size} LIGAS",
                color = TextDim,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(grades) { grade ->
                val isSelected = grade.code == selectedGrade.code
                val isHighMatch = grade.confidencePercent >= 80
                val localizedClassification = remember(grade, language) {
                    LocalizationManager.getLocalizedClassification(grade, language)
                }

                val borderColor by animateColorAsState(
                    targetValue = if (isSelected) NeonGreenBright else Color(0x1FFFFFFF),
                    animationSpec = tween(300),
                    label = "grade_border"
                )

                Surface(
                    modifier = Modifier
                        .width(170.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .clickable { onSelectGrade(grade) },
                    shape = RoundedCornerShape(18.dp),
                    color = if (isSelected) DarkCardElevated else DarkCard,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = borderColor
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = grade.code,
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black
                            )

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isHighMatch) NeonGreen else Color(0x33FFFFFF)
                            ) {
                                Text(
                                    text = "${grade.confidencePercent}%",
                                    color = if (isHighMatch) PureBlack else TextWhite,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = localizedClassification,
                            color = TextMuted,
                            fontSize = 10.5.sp,
                            lineHeight = 14.sp,
                            maxLines = 2
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(if (isSelected) NeonGreenBright else TextDim, CircleShape)
                            )
                            Text(
                                text = if (isSelected) "SELECIONADO" else "COMPARAR",
                                color = if (isSelected) NeonGreenBright else TextDim,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GradeOverviewCard(
    grade: SteelGrade,
    language: AppLanguage
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }
    val localizedClassification = remember(grade, language) {
        LocalizationManager.getLocalizedClassification(grade, language)
    }
    val localizedStandard = remember(grade, language) {
        LocalizationManager.getLocalizedStandard(grade, language)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${grade.code} • $localizedStandard",
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = localizedClassification,
                        color = NeonGreenBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    shape = CircleShape,
                    color = Color(0x181DB954),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Aço Identificado",
                            tint = NeonGreenBright,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = "${grade.confidencePercent}% ${strings.matchAccuracy}",
                            color = NeonGreenBright,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }

            Text(
                text = grade.summary,
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            HorizontalDivider(color = Color(0x1AFFFFFF))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = strings.sparkColorLabel,
                        color = TextDim,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = grade.sparkColor,
                        color = SparkYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = strings.streamLengthLabel,
                        color = TextDim,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = grade.streamLength,
                        color = TextWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun SparkDiagnosticsCard(
    grade: SteelGrade,
    language: AppLanguage
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = DarkCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = strings.sparkDiagnosticsTitle,
                color = TextWhite,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp,
                fontFamily = FontFamily.Monospace
            )

            grade.characteristics.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.property,
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = item.detail,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = PureBlack
                    ) {
                        Text(
                            text = item.value,
                            color = NeonGreenBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ActionControlsSection(
    isScanning: Boolean,
    language: AppLanguage,
    onToggleScan: () -> Unit,
    onLockCertificate: () -> Unit
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Ação Primária: Bloquear Amostra e Emitir Laudo Técnico (Verde Spotify)
        Button(
            onClick = onLockCertificate,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("lock_sample_button"),
            shape = RoundedCornerShape(26.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = NeonGreen,
                contentColor = PureBlack
            )
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = strings.lockAndExportCert,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = strings.lockAndExportCert,
                fontSize = 13.5.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
        }

        // Ação Secundária: Congelar / Retomar Leitura ao Vivo
        OutlinedButton(
            onClick = onToggleScan,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("toggle_scan_button"),
            shape = RoundedCornerShape(24.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = TextWhite
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Icon(
                imageVector = if (isScanning) Icons.Default.Fingerprint else Icons.Default.Sensors,
                contentDescription = "Alternar estado de leitura",
                tint = if (isScanning) NeonGreenBright else SparkOrange,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isScanning) strings.freezeAnalysisFrame else strings.resumeLiveSpectrometry,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )
        }
    }
}

@Composable
fun MaterialCertificateDialog(
    grade: SteelGrade,
    language: AppLanguage,
    onDismiss: () -> Unit
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }
    val localizedClassification = remember(grade, language) {
        LocalizationManager.getLocalizedClassification(grade, language)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = PureBlack
                )
            ) {
                Text(strings.certificateClose, fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.CheckCircle,
                    contentDescription = null,
                    tint = NeonGreenBright
                )
                Text(
                    text = strings.certificateDialogTitle,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "${strings.lockedSample}: ${grade.code}",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${strings.classificationLabel}: $localizedClassification",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = "${strings.confidenceLabel}: ${grade.confidencePercent}%",
                    color = NeonGreenBright,
                    fontSize = 12.sp
                )
                HorizontalDivider(color = Color(0x22FFFFFF))
                Text(
                    text = "${strings.chemicalCompositionTitle}:\n" +
                        grade.alloys.joinToString("\n") { "• ${it.name} (${it.symbol}): ${"%.2f%%".format(it.measuredPercent)} (${strings.specRange}: ${it.nominalRange})" },
                    color = TextWhite,
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 16.sp
                )
                HorizontalDivider(color = Color(0x22FFFFFF))
                Text(
                    text = "${strings.sparkDiagnosticsTitle}:\n• ${strings.sparkColorLabel}: ${grade.sparkColor}\n• ${strings.streamLengthLabel}: ${grade.streamLength}\n• ${strings.burstPatternLabel}: ${grade.burstPattern}",
                    color = TextMuted,
                    fontSize = 10.5.sp,
                    fontFamily = FontFamily.Monospace,
                    lineHeight = 15.sp
                )
            }
        },
        containerColor = DarkSurface,
        titleContentColor = TextWhite,
        textContentColor = TextMuted,
        shape = RoundedCornerShape(22.dp)
    )
}

/**
 * Mantido para compatibilidade com testes automatizados
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}

@Preview(showBackground = true)
@Composable
fun SteelIdentifierAppPreview() {
    MyApplicationTheme {
        SteelIdentifierApp()
    }
}
