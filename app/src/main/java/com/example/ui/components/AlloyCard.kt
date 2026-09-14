package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AlloyComponent
import com.example.model.AppLanguage
import com.example.model.LocalizationManager
import com.example.ui.theme.DarkCard
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SparkOrange
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

@Composable
fun AlloyComponentCard(
    alloy: AlloyComponent,
    language: AppLanguage = AppLanguage.PT,
    modifier: Modifier = Modifier
) {
    val strings = remember(language) { LocalizationManager.getStrings(language) }

    val localizedName = when (alloy.symbol) {
        "C" -> when (language) {
            AppLanguage.PT -> "Carbono"
            AppLanguage.EN -> "Carbon"
            AppLanguage.ES -> "Carbono"
            AppLanguage.DE -> "Kohlenstoff"
            AppLanguage.JA -> "炭素"
        }
        "Cr" -> when (language) {
            AppLanguage.PT -> "Cromo"
            AppLanguage.EN -> "Chromium"
            AppLanguage.ES -> "Cromo"
            AppLanguage.DE -> "Chrom"
            AppLanguage.JA -> "クロム"
        }
        "Ni" -> when (language) {
            AppLanguage.PT -> "Níquel"
            AppLanguage.EN -> "Nickel"
            AppLanguage.ES -> "Níquel"
            AppLanguage.DE -> "Nickel"
            AppLanguage.JA -> "ニッケル"
        }
        "Mn" -> when (language) {
            AppLanguage.PT -> "Manganês"
            AppLanguage.EN -> "Manganese"
            AppLanguage.ES -> "Manganeso"
            AppLanguage.DE -> "Mangan"
            AppLanguage.JA -> "マンガン"
        }
        "Mo" -> when (language) {
            AppLanguage.PT -> "Molibdênio"
            AppLanguage.EN -> "Molybdenum"
            AppLanguage.ES -> "Molibdeno"
            AppLanguage.DE -> "Molybdän"
            AppLanguage.JA -> "モリブデン"
        }
        "V" -> when (language) {
            AppLanguage.PT -> "Vanádio"
            AppLanguage.EN -> "Vanadium"
            AppLanguage.ES -> "Vanadio"
            AppLanguage.DE -> "Vanadium"
            AppLanguage.JA -> "バナジウム"
        }
        "Si" -> when (language) {
            AppLanguage.PT -> "Silício"
            AppLanguage.EN -> "Silicon"
            AppLanguage.ES -> "Silicio"
            AppLanguage.DE -> "Silizium"
            AppLanguage.JA -> "シリコン"
        }
        else -> alloy.name
    }

    // Determine meter fill based on typical metallurgical scales
    val progressTarget = when (alloy.symbol) {
        "C" -> (alloy.measuredPercent.toFloat() / 1.60f).coerceIn(0.05f, 1.0f)
        "Cr" -> (alloy.measuredPercent.toFloat() / 20.0f).coerceIn(0.05f, 1.0f)
        "Ni" -> (alloy.measuredPercent.toFloat() / 10.0f).coerceIn(0.05f, 1.0f)
        "Mn" -> (alloy.measuredPercent.toFloat() / 2.0f).coerceIn(0.05f, 1.0f)
        "Mo" -> (alloy.measuredPercent.toFloat() / 1.5f).coerceIn(0.05f, 1.0f)
        "V" -> (alloy.measuredPercent.toFloat() / 1.2f).coerceIn(0.05f, 1.0f)
        else -> (alloy.measuredPercent.toFloat() / 1.0f).coerceIn(0.05f, 1.0f)
    }

    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 800),
        label = "alloy_progress"
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .border(
                width = 1.dp,
                color = Color(0x1FFFFFFF),
                shape = RoundedCornerShape(18.dp)
            ),
        color = DarkCard,
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Símbolo químico + Nome
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(
                                color = Color(0x221DB954),
                                shape = CircleShape
                            )
                            .border(1.dp, NeonGreen.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = alloy.symbol,
                            color = NeonGreenBright,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Column {
                        Text(
                            text = localizedName,
                            color = TextWhite,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${strings.specRange}: ${alloy.nominalRange}",
                            color = TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }

                // Valor medido e indicador de conformidade
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "%.2f%%".format(alloy.measuredPercent),
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    )
                    Text(
                        text = strings.inRangeTag,
                        color = NeonGreenBright,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Smooth horizontal meter gauge
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(PureBlack)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(animatedProgress)
                        .clip(RoundedCornerShape(3.dp))
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                NeonGreen,
                                NeonGreenBright
                            )
                        )
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Metallurgical function tag
            Text(
                text = alloy.function,
                color = TextDim,
                fontSize = 11.sp,
                lineHeight = 14.sp
            )
        }
    }
}
