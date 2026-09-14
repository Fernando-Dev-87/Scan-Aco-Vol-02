package com.example.ui.components

import android.net.Uri
import coil.compose.AsyncImage
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.outlined.FlashOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.model.AppLanguage
import com.example.model.LocalizationManager
import com.example.model.SteelGrade
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SparkOrange
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun SparkViewfinder(
    isScanning: Boolean,
    torchOn: Boolean,
    isAnalysisComplete: Boolean = false,
    capturedMediaUri: Uri? = null,
    grade: SteelGrade? = null,
    language: AppLanguage = AppLanguage.PT,
    onToggleTorch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }
    val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")

    // Laser scan line vertical progression
    val scanProgress by infiniteTransition.animateFloat(
        initialValue = 0.05f,
        targetValue = 0.95f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scan_progress"
    )

    // Pulse animation for CV bounding boxes and status indicator
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.45f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    // Subtle drift for bounding box tracking
    val boxDrift by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "box_drift"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.28f)
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0x551DB954),
                        Color(0x221DB954),
                        Color(0x11FFFFFF)
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        color = DarkSurface,
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isAnalysisComplete && capturedMediaUri != null) {
                // Mostra a foto/vídeo da análise ocultando a imagem animada
                AsyncImage(
                    model = capturedMediaUri,
                    contentDescription = "Captured media for analysis",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Live Macro Spark Test Photo (Animated state)
                Image(
                    painter = painterResource(id = R.drawable.img_spark_test_macro),
                    contentDescription = "Macro industrial steel spark test",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Torch illumination overlay if enabled
            if (torchOn) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    Color(0x33FFFFFF),
                                    Color(0x15FFFFFF),
                                    Color.Transparent
                                ),
                                center = Offset(300f, 250f),
                                radius = 450f
                            )
                        )
                )
            }

            // Overlay de Resultado da Análise (Quando concluído)
            if (isAnalysisComplete && grade != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(PureBlack.copy(alpha = 0.85f))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = NeonGreen.copy(alpha = 0.15f),
                            border = BorderStroke(2.dp, NeonGreenBright)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CenterFocusStrong,
                                contentDescription = null,
                                tint = NeonGreenBright,
                                modifier = Modifier.size(48.dp).padding(8.dp)
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = strings.detectedSteelTitle,
                                color = NeonGreenBright,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp
                            )
                            Text(
                                text = grade.code,
                                color = TextWhite,
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = LocalizationManager.getLocalizedClassification(grade, language),
                                color = TextMuted,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                textAlign = TextAlign.Center
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = DarkCardElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${strings.matchAccuracy}: ${grade.confidencePercent}%",
                                    color = NeonGreenBright,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }

            // High-Tech Digital HUD & Computer Vision Canvas
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .drawBehind {
                        val canvasWidth = size.width
                        val canvasHeight = size.height

                        // Corner Viewfinder Brackets
                        val cornerLen = 22.dp.toPx()
                        val cornerStroke = 2.dp.toPx()
                        val cornerColor = Color(0xBB1DB954)
                        val pad = 16.dp.toPx()

                        // Top-Left corner bracket
                        drawLine(cornerColor, Offset(pad, pad), Offset(pad + cornerLen, pad), cornerStroke)
                        drawLine(cornerColor, Offset(pad, pad), Offset(pad, pad + cornerLen), cornerStroke)

                        // Top-Right corner bracket
                        drawLine(cornerColor, Offset(canvasWidth - pad, pad), Offset(canvasWidth - pad - cornerLen, pad), cornerStroke)
                        drawLine(cornerColor, Offset(canvasWidth - pad, pad), Offset(canvasWidth - pad, pad + cornerLen), cornerStroke)

                        // Bottom-Left corner bracket
                        drawLine(cornerColor, Offset(pad, canvasHeight - pad), Offset(pad + cornerLen, canvasHeight - pad), cornerStroke)
                        drawLine(cornerColor, Offset(pad, canvasHeight - pad), Offset(pad, canvasHeight - pad - cornerLen), cornerStroke)

                        // Bottom-Right corner bracket
                        drawLine(cornerColor, Offset(canvasWidth - pad, canvasHeight - pad), Offset(canvasWidth - pad - cornerLen, canvasHeight - pad), cornerStroke)
                        drawLine(cornerColor, Offset(canvasWidth - pad, canvasHeight - pad), Offset(canvasWidth - pad, canvasHeight - pad - cornerLen), cornerStroke)

                        // Faint grid lines across viewfinder for technical depth
                        val gridAlpha = 0.08f
                        drawLine(Color(0xFFFFFFFF).copy(alpha = gridAlpha), Offset(canvasWidth * 0.33f, pad), Offset(canvasWidth * 0.33f, canvasHeight - pad), 1f)
                        drawLine(Color(0xFFFFFFFF).copy(alpha = gridAlpha), Offset(canvasWidth * 0.66f, pad), Offset(canvasWidth * 0.66f, canvasHeight - pad), 1f)
                        drawLine(Color(0xFFFFFFFF).copy(alpha = gridAlpha), Offset(pad, canvasHeight * 0.5f), Offset(canvasWidth - pad, canvasHeight * 0.5f), 1f)

                        // If active scanning, draw soft glowing neon laser beam
                        if (isScanning && !isAnalysisComplete) {
                            val lineY = canvasHeight * scanProgress
                            val glowHeight = 36.dp.toPx()

                            // Vertical laser trail gradient
                            drawRect(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color(0x3310DF65),
                                        Color(0x9910DF65),
                                        Color.Transparent
                                    ),
                                    startY = lineY - glowHeight,
                                    endY = lineY + 6.dp.toPx()
                                ),
                                topLeft = Offset(pad, lineY - glowHeight),
                                size = Size(canvasWidth - (pad * 2), glowHeight + 6.dp.toPx())
                            )

                            // Crisp neon green laser core
                            drawLine(
                                color = Color(0xFF10DF65),
                                start = Offset(pad, lineY),
                                end = Offset(canvasWidth - pad, lineY),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                    }
            )

            // Computer Vision Bounding Box 1: Spark burst cluster
            if (!isAnalysisComplete) {
                Box(
                    modifier = Modifier
                        .offset(x = (28 + boxDrift).dp, y = (54 + boxDrift).dp)
                        .size(width = 148.dp, height = 90.dp)
                        .border(
                            width = 1.2.dp,
                            color = NeonGreenBright.copy(alpha = pulseAlpha),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color(0x1810DF65), shape = RoundedCornerShape(8.dp))
                        .padding(6.dp)
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(5.dp)
                                    .background(NeonGreenBright, CircleShape)
                            )
                            Text(
                                text = "CLUSTER DE FAÍSCAS α",
                                color = NeonGreenBright,
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Monospace,
                                letterSpacing = 0.5.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "DENSIDADE: 92.4% • 1840°C",
                            color = Color(0xDDFFFFFF),
                            fontSize = 7.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "ESTRELA-C TIPO 3 (RAMIFICADA)",
                            color = SparkOrange,
                            fontSize = 7.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Computer Vision Bounding Box 2: Spark projectile sprig
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = (-22 - boxDrift).dp, y = (-12 + boxDrift).dp)
                        .size(width = 114.dp, height = 62.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xAA1DB954),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .background(Color(0x101DB954), shape = RoundedCornerShape(6.dp))
                        .padding(5.dp)
                ) {
                    Column {
                        Text(
                            text = "[RAMIFICAÇÃO #04]",
                            color = NeonGreen,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "RAIO FE-C: 4.8mm",
                            color = TextMuted,
                            fontSize = 7.5.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "PRECISÃO: 94.6%",
                            color = NeonGreenBright,
                            fontSize = 7.5.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Central Telemetry Crosshair
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(44.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CenterFocusStrong,
                        contentDescription = "Retículo de mira",
                        tint = NeonGreenBright.copy(alpha = pulseAlpha * 0.7f),
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Top Status Bar inside Viewfinder
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Active status pill badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PureBlack.copy(alpha = 0.75f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x331DB954))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .background(
                                    color = if (isScanning) NeonGreenBright else Color.Gray,
                                    shape = CircleShape
                                )
                        )
                        Text(
                            text = if (isScanning) "ESPECTROMETRIA CV ATIVA" else "QUADRO CONGELADO",
                            color = TextWhite,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.8.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Quick Torch Toggle
                Surface(
                    shape = CircleShape,
                    color = PureBlack.copy(alpha = 0.75f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (torchOn) NeonGreenBright else Color(0x22FFFFFF))
                ) {
                    IconButton(
                        onClick = onToggleTorch,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (torchOn) Icons.Filled.FlashOn else Icons.Outlined.FlashOff,
                            contentDescription = "Alternar Lanterna",
                            tint = if (torchOn) NeonGreenBright else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Bottom Telemetry Metadata Strip inside Viewfinder
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, PureBlack.copy(alpha = 0.85f))
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "λ: 589nm • ESPECTRO Fe-C",
                    color = TextMuted,
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = "60.0 FPS • ISO 400 • F/1.8",
                    color = NeonGreen.copy(alpha = 0.9f),
                    fontSize = 9.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
