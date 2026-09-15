package com.example.ui.components

import android.content.ContentValues
import android.os.Build
import android.provider.MediaStore
import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.example.model.AppLanguage
import com.example.model.LocalizationManager
import com.example.model.SteelGrade
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SparkOrange
import com.example.ui.theme.SparkYellow
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

enum class CaptureMode {
    PHOTO,
    VIDEO
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SteelCaptureSection(
    grade: SteelGrade,
    language: AppLanguage,
    isAnalyzing: Boolean = false,
    isAnalysisComplete: Boolean = false,
    onSampleCaptured: (SteelGrade, Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val strings = remember(language) { LocalizationManager.getStrings(language) }
    var captureMode by remember { mutableStateOf(CaptureMode.PHOTO) }
    var lastCapturedNotice by remember { mutableStateOf<String?>(null) }
    var showFlashEffect by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()

    // Permissions state
    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
        )
    )

    // Temporário para armazenar o URI da captura atual
    var tempUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para tirar foto
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && tempUri != null) {
            // Salva na galeria pública
            saveMediaToGallery(context, tempUri!!, true)
            lastCapturedNotice = strings.photoCapturedSuccess
            onSampleCaptured(grade, tempUri)
        } else {
            tempUri = null
        }
    }

    // Launcher para gravar vídeo
    val captureVideoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CaptureVideo()
    ) { success ->
        if (success && tempUri != null) {
            // Salva na galeria pública
            saveMediaToGallery(context, tempUri!!, false)
            lastCapturedNotice = strings.recordedVideoAnalysis
            onSampleCaptured(grade, tempUri)
        } else {
            tempUri = null
        }
    }

    // Photo picker standard Android
    val mediaPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            lastCapturedNotice = if (captureMode == CaptureMode.PHOTO) {
                strings.photoCapturedSuccess
            } else {
                strings.recordedVideoAnalysis
            }
            onSampleCaptured(grade, uri)
        }
    }

    // Flash visual ao disparar captura
    LaunchedEffect(showFlashEffect) {
        if (showFlashEffect) {
            delay(120)
            showFlashEffect = false
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Painel de Captura de Mídia (Foto / Vídeo)
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("steel_capture_control"),
            shape = RoundedCornerShape(22.dp),
            color = DarkCard,
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Seletor de Modo: FOTO vs VÍDEO
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .background(DarkSurface, RoundedCornerShape(12.dp))
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        CaptureModeTab(
                            text = strings.photoMode,
                            icon = Icons.Default.CameraAlt,
                            isSelected = captureMode == CaptureMode.PHOTO,
                            onClick = {
                                captureMode = CaptureMode.PHOTO
                            }
                        )
                        CaptureModeTab(
                            text = strings.videoMode,
                            icon = Icons.Default.Videocam,
                            isSelected = captureMode == CaptureMode.VIDEO,
                            onClick = {
                                captureMode = CaptureMode.VIDEO
                            }
                        )
                    }

                    // Botão para carregar arquivo da galeria
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = DarkSurface,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .clickable {
                                val request = if (captureMode == CaptureMode.PHOTO) {
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                } else {
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                                }
                                mediaPickerLauncher.launch(request)
                            }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoLibrary,
                                contentDescription = strings.pickFromGallery,
                                tint = TextWhite,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = strings.pickFromGallery,
                                color = TextWhite,
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }

                // Botão Central de Disparo de Foto / Vídeo
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (captureMode == CaptureMode.PHOTO) {
                        // Botão de Captura de Foto (ANALISE)
                        Button(
                            onClick = {
                                if (!isAnalyzing) {
                                    if (permissionState.allPermissionsGranted) {
                                        showFlashEffect = true
                                        try {
                                            val file = File(context.cacheDir, "temp_steel_${System.currentTimeMillis()}.jpg")
                                            if (file.exists()) file.delete()
                                            file.createNewFile()
                                            val uri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.fileprovider",
                                                file
                                            )
                                            tempUri = uri
                                            takePictureLauncher.launch(uri)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Erro ao abrir câmera: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        permissionState.launchMultiplePermissionRequest()
                                    }
                                }
                            },
                            enabled = !isAnalyzing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("capture_photo_button"),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAnalyzing) DarkCardElevated else NeonGreen,
                                contentColor = if (isAnalyzing) TextMuted else PureBlack
                            )
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = NeonGreenBright,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = strings.capturePhoto,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAnalyzing) strings.analyzingTitle else strings.capturePhoto,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    } else {
                        // Botão de Gravação de Vídeo (Iniciar / Parar / ANALISAR VÍDEO)
                        Button(
                            onClick = {
                                if (!isAnalyzing) {
                                    if (permissionState.allPermissionsGranted) {
                                        try {
                                            val file = File(context.cacheDir, "temp_steel_vid_${System.currentTimeMillis()}.mp4")
                                            if (file.exists()) file.delete()
                                            file.createNewFile()
                                            val uri = FileProvider.getUriForFile(
                                                context,
                                                "${context.packageName}.fileprovider",
                                                file
                                            )
                                            tempUri = uri
                                            captureVideoLauncher.launch(uri)
                                        } catch (e: Exception) {
                                            Toast.makeText(context, "Erro ao abrir câmera: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
                                        permissionState.launchMultiplePermissionRequest()
                                    }
                                }
                            },
                            enabled = !isAnalyzing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("capture_video_button"),
                            shape = RoundedCornerShape(26.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isAnalyzing) DarkCardElevated else SparkOrange,
                                contentColor = PureBlack
                            )
                        ) {
                            if (isAnalyzing) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = NeonGreenBright,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.Videocam,
                                    contentDescription = strings.captureVideo,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isAnalyzing) strings.analyzingTitle else strings.captureVideo,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }

                // Notificação de captura confirmada
                if (lastCapturedNotice != null) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = NeonGreenBright,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = lastCapturedNotice!!,
                            color = NeonGreenBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }

        // =========================================================================
        // "LOGO ABAIXO MOSTRAR DE QUE TIPO É O AÇO"
        // Cartão Prominente e Completo de Identificação do Tipo de Aço Detectado
        // =========================================================================
        if (isAnalysisComplete && !isAnalyzing) {
            DetectedSteelResultCard(
                grade = grade,
                language = language
            )
        }
    }
}

/**
 * Salva a mídia capturada na galeria pública do dispositivo
 */
private fun saveMediaToGallery(context: Context, uri: Uri, isImage: Boolean) {
    try {
        val resolver = context.contentResolver
        val contentValues = ContentValues().apply {
            val timestamp = System.currentTimeMillis()
            put(MediaStore.MediaColumns.DISPLAY_NAME, "ScanAco_${timestamp}")
            put(MediaStore.MediaColumns.MIME_TYPE, if (isImage) "image/jpeg" else "video/mp4")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, if (isImage) Environment.DIRECTORY_PICTURES else Environment.DIRECTORY_MOVIES)
                put(MediaStore.MediaColumns.IS_PENDING, 1)
            }
        }

        val collection = if (isImage) {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        } else {
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        }

        val destinationUri = resolver.insert(collection, contentValues)
        if (destinationUri != null) {
            resolver.openInputStream(uri)?.use { input ->
                resolver.openOutputStream(destinationUri)?.use { output ->
                    input.copyTo(output)
                }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                contentValues.clear()
                contentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                resolver.update(destinationUri, contentValues, null, null)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

@Composable
fun CaptureModeTab(
    text: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) NeonGreen else Color.Transparent,
        animationSpec = tween(250),
        label = "tab_bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) PureBlack else TextMuted,
        animationSpec = tween(250),
        label = "tab_content"
    )

    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(9.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = contentColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = text,
            color = contentColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun DetectedSteelResultCard(
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

    val animatedAccuracy by animateFloatAsState(
        targetValue = grade.confidencePercent / 100f,
        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing),
        label = "accuracy_progress"
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("detected_steel_type_card"),
        shape = RoundedCornerShape(24.dp),
        color = DarkCardElevated,
        border = BorderStroke(
            1.5.dp,
            Brush.horizontalGradient(
                listOf(
                    NeonGreenBright,
                    NeonGreen.copy(alpha = 0.6f),
                    SparkYellow.copy(alpha = 0.4f)
                )
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Cabeçalho de Reconhecimento Instantâneo (Invertido: Emblema em cima do título)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Emblema "RECONHECIMENTO ÓPTICO CV"
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PureBlack,
                    border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.3f)),
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.AutoAwesome,
                            contentDescription = null,
                            tint = SparkYellow,
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = strings.instantRecognitionBadge,
                            color = TextWhite,
                            fontSize = 8.5.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Título "TIPO DE AÇO DETECTADO" (Ponto em cima, texto em baixo)
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(NeonGreenBright, CircleShape)
                    )
                    Text(
                        text = strings.detectedSteelTitle,
                        color = NeonGreenBright,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Exibição em Destaque do Código e da Norma do Aço
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = grade.code,
                        color = TextWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = localizedStandard,
                        color = TextMuted,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Monospace
                    )
                }

                // Porcentagem de Precisão
                Column(horizontalAlignment = Alignment.End) {
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "${grade.confidencePercent}",
                            color = NeonGreenBright,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "%",
                            color = NeonGreenBright,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Text(
                        text = strings.matchAccuracy,
                        color = TextDim,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }

            // Barra de Precisão de Reconhecimento
            LinearProgressIndicator(
                progress = { animatedAccuracy },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = NeonGreen,
                trackColor = PureBlack,
                strokeCap = StrokeCap.Round
            )

            // Nome e Classificação Metalúrgica Completa
            Text(
                text = if (grade.code == "N/A") strings.unidentifiedMaterial else localizedClassification,
                color = if (grade.code == "N/A") Color.Red else TextWhite,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 19.sp
            )

            HorizontalDivider(color = Color(0x1FFFFFFF))

            // Resumo dos Principais Componentes Químicos da Liga Detectada
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                grade.alloys.take(4).forEach { alloy ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = PureBlack,
                        border = BorderStroke(1.dp, BorderSubtle),
                        modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 6.dp, horizontal = 4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = alloy.symbol,
                                color = NeonGreenBright,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "%.2f%%".format(alloy.measuredPercent),
                                color = TextWhite,
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Características do Ensaio Óptico de Faísca (Cor e Padrão alinhados verticalmente)
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Grupo da Cor
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(SparkYellow, CircleShape)
                    )
                    Text(
                        text = grade.sparkColor,
                        color = SparkYellow,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Grupo do Padrão de Explosão (Agora abaixo da cor)
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = DarkSurface,
                    border = BorderStroke(0.5.dp, TextDim.copy(alpha = 0.3f))
                ) {
                    Text(
                        text = grade.burstPattern,
                        color = TextMuted,
                        fontSize = 9.5.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
