package com.example.ui.components

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.PureBlack
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
    analysisIntensity: Float = 1.0f,
    onToggleTorch: () -> Unit,
    modifier: Modifier = Modifier
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.28f)
            .clip(RoundedCornerShape(24.dp))
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        NeonGreen.copy(alpha = 0.3f),
                        Color.Transparent
                    )
                ),
                shape = RoundedCornerShape(24.dp)
            ),
        color = DarkSurface,
        shape = RoundedCornerShape(24.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (isAnalysisComplete && capturedMediaUri != null) {
                // Foto capturada real
                AsyncImage(
                    model = capturedMediaUri,
                    contentDescription = "Captured media",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    alpha = 0.8f
                )
            } else {
                // Fundo estático e limpo com o logotipo do app
                Box(
                    modifier = Modifier.fillMaxSize().background(Color(0xFF121212)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_app_logo),
                        contentDescription = "Scan Aço Backdrop",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(140.dp),
                        alpha = 0.4f
                    )
                }
            }

            // Retículo central estático
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(40.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.CenterFocusStrong,
                    contentDescription = "Target",
                    tint = NeonGreenBright.copy(alpha = 0.4f),
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Barra Superior de Status
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = PureBlack.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, Color(0x22FFFFFF))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .background(
                                    color = if (isScanning) NeonGreenBright else Color.Gray,
                                    shape = CircleShape
                                )
                        )
                        Text(
                            text = if (isScanning) "SCAN ATIVO" else "PAUSADO",
                            color = TextWhite,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = PureBlack.copy(alpha = 0.6f),
                    border = BorderStroke(1.dp, if (torchOn) NeonGreenBright else Color(0x22FFFFFF))
                ) {
                    IconButton(
                        onClick = onToggleTorch,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = if (torchOn) Icons.Filled.FlashOn else Icons.Outlined.FlashOff,
                            contentDescription = "Torch",
                            tint = if (torchOn) NeonGreenBright else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Overlay de Resultado Central (Fixado)
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
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = strings.detectedSteelTitle,
                            color = NeonGreenBright,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.5.sp
                        )
                        Text(
                            text = grade.code,
                            color = TextWhite,
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = LocalizationManager.getLocalizedClassification(grade, language),
                            color = TextMuted,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                        
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = NeonGreen.copy(alpha = 0.2f),
                            modifier = Modifier.padding(top = 10.dp)
                        ) {
                            Text(
                                text = "${grade.confidencePercent}% PRECISÃO",
                                color = NeonGreenBright,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }

            // Barra Inferior de Metadados Estática
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, PureBlack.copy(alpha = 0.7f))
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "OFFLINE SPECTRUM ANALYSIS",
                    color = TextMuted,
                    fontSize = 8.5.sp,
                    fontFamily = FontFamily.Monospace
                )
                Text(
                    text = if (isAnalysisComplete) "FINISHED" else "CALIB: ${"%.1f".format(analysisIntensity)}x",
                    color = NeonGreenBright.copy(alpha = 0.8f),
                    fontSize = 8.5.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
        }
    }
}
