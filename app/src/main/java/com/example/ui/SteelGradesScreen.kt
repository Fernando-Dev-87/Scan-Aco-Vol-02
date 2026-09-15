package com.example.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.model.AppLanguage
import com.example.model.LocalizationManager
import com.example.model.SteelGrade
import com.example.model.SteelPresets
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SteelGradesScreen(
    onBack: () -> Unit,
    onSelectGrade: (SteelGrade) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "CLASSES DE AÇOS", 
                        fontWeight = FontWeight.Black, 
                        letterSpacing = 1.2.sp,
                        fontSize = 18.sp
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar", tint = NeonGreenBright)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SpotifyBlack,
                    titleContentColor = TextWhite
                )
            )
        },
        containerColor = SpotifyBlack
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(SpotifyBlack, PureBlack)
                    )
                ),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "BIBLIOTECA TÉCNICA DE MATERIAIS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            items(SteelPresets.allGrades) { grade ->
                SteelGradeCard(
                    grade = grade,
                    language = AppLanguage.PT,
                    onSelect = {
                        onSelectGrade(it)
                        onBack()
                    }
                )
            }
        }
    }
}

@Composable
fun SteelGradeCard(
    grade: SteelGrade,
    language: AppLanguage,
    onSelect: (SteelGrade) -> Unit
) {
    val localizedClassification = remember(grade, language) {
        LocalizationManager.getLocalizedClassification(grade, language)
    }
    
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onSelect(grade) },
        shape = RoundedCornerShape(18.dp),
        color = DarkCard,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = grade.code,
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = grade.standard,
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = NeonGreen.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f))
                ) {
                    Text(
                        text = "INFO",
                        color = NeonGreenBright,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = localizedClassification,
                color = TextWhite.copy(alpha = 0.9f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 18.sp
            )
            
            HorizontalDivider(color = Color(0x1AFFFFFF))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Column {
                    Text("COR", color = TextDim, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(grade.sparkColor, color = SparkYellow, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
                Column {
                    Text("FEIXE", color = TextDim, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    Text(grade.streamLength, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
