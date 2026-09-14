package com.example.ui

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AnalysisEntity
import com.example.data.AppDatabase
import com.example.data.MoshiHelper
import com.example.model.SteelGrade
import com.example.model.SteelPresets
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.DarkCard
import com.example.ui.theme.DarkCardElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonGreenBright
import com.example.ui.theme.PureBlack
import com.example.ui.theme.SparkOrange
import com.example.ui.theme.SpotifyBlack
import com.example.ui.theme.TextDim
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import java.text.DateFormat
import java.util.Date
import kotlinx.coroutines.launch

fun AnalysisEntity.toSteelGrade(): SteelGrade {
    return SteelGrade(
        code = this.gradeCode,
        standard = this.standard,
        classification = this.classification,
        confidencePercent = this.confidence,
        summary = "",
        sparkColor = this.sparkColor,
        streamLength = this.streamLength,
        burstPattern = this.burstPattern,
        alloys = MoshiHelper.fromJson(this.alloysJson),
        characteristics = emptyList(),
        carbonLevel = 0.0,
        isMagnetic = true,
        alloySignatures = emptyList()
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProDashboardScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val database = remember { AppDatabase.getInstance(context) }
    val analysisHistory by database.analysisDao().getAll().collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    var selectedEntityForExport by remember { mutableStateOf<AnalysisEntity?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "PAINEL PROFISSIONAL", 
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(SpotifyBlack, PureBlack)
                    )
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Export & History Block Integrated
            ExportAndHistorySection(
                history = analysisHistory,
                selectedEntity = selectedEntityForExport,
                onEntitySelect = { selectedEntityForExport = it },
                onDeleteClick = { entity ->
                    coroutineScope.launch {
                        database.analysisDao().delete(entity)
                        if (selectedEntityForExport?.id == entity.id) {
                            selectedEntityForExport = null
                        }
                    }
                },
                onExportClick = { entity ->
                    try {
                        val grade = entity.toSteelGrade()
                        val pdfDocument = PdfDocument()
                        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create() // A4 size
                        val page = pdfDocument.startPage(pageInfo)
                        val canvas: Canvas = page.canvas
                        val paint = Paint()
                        
                        var yPos = 40f
                        
                        // Header
                        paint.textSize = 18f
                        paint.isFakeBoldText = true
                        canvas.drawText("LAUDO TÉCNICO METALÚRGICO - SCAN AÇO", 40f, yPos, paint)
                        
                        yPos += 30f
                        paint.textSize = 12f
                        paint.isFakeBoldText = false
                        canvas.drawLine(40f, yPos, 555f, yPos, paint)
                        
                        yPos += 30f
                        canvas.drawText("Material Identificado: ${grade.code}", 40f, yPos, paint)
                        yPos += 20f
                        canvas.drawText("Norma: ${grade.standard}", 40f, yPos, paint)
                        yPos += 20f
                        canvas.drawText("Classificação: ${grade.classification}", 40f, yPos, paint)
                        yPos += 20f
                        canvas.drawText("Confiança da Análise: ${grade.confidencePercent}%", 40f, yPos, paint)
                        
                        yPos += 40f
                        paint.isFakeBoldText = true
                        canvas.drawText("CARACTERÍSTICAS DE FAÍSCA", 40f, yPos, paint)
                        paint.isFakeBoldText = false
                        yPos += 25f
                        canvas.drawText("- Cor: ${grade.sparkColor}", 50f, yPos, paint)
                        yPos += 20f
                        canvas.drawText("- Comprimento: ${grade.streamLength}", 50f, yPos, paint)
                        yPos += 20f
                        canvas.drawText("- Padrão: ${grade.burstPattern}", 50f, yPos, paint)
                        
                        yPos += 40f
                        paint.isFakeBoldText = true
                        canvas.drawText("COMPOSIÇÃO QUÍMICA ESTIMADA", 40f, yPos, paint)
                        paint.isFakeBoldText = false
                        yPos += 10f
                        grade.alloys.forEach { alloy ->
                            yPos += 20f
                            canvas.drawText("• ${alloy.name} (${alloy.symbol}): ${alloy.measuredPercent}%", 50f, yPos, paint)
                        }
                        
                        yPos += 60f
                        paint.textSize = 10f
                        paint.color = android.graphics.Color.GRAY
                        canvas.drawLine(40f, yPos, 555f, yPos, paint)
                        yPos += 20f
                        canvas.drawText("Relatório gerado via Módulo Profissional Scan Aço.", 40f, yPos, paint)
                        canvas.drawText("Data: ${DateFormat.getDateTimeInstance().format(Date())}", 40f, yPos + 15f, paint)

                        pdfDocument.finishPage(page)

                        val fileName = "Laudo_${grade.code.replace(" ", "_")}.pdf"
                        val file = File(context.cacheDir, fileName)
                        pdfDocument.writeTo(FileOutputStream(file))
                        pdfDocument.close()

                        val contentUri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            file
                        )

                        val shareIntent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/pdf"
                            putExtra(Intent.EXTRA_STREAM, contentUri)
                            putExtra(Intent.EXTRA_SUBJECT, "Laudo Técnico - ${grade.code}")
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Compartilhar Laudo PDF"))
                    } catch (e: Exception) {
                        Toast.makeText(context, "Erro ao gerar PDF: ${e.message}", Toast.LENGTH_LONG).show()
                        e.printStackTrace()
                    }
                }
            )

            // Best Practices Guide
            SectionHeader(title = "GUIA DE BOAS PRÁTICAS", icon = Icons.Default.Info)
            BestPracticesGuide()
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = NeonGreenBright, modifier = Modifier.size(20.dp))
        Text(
            text = title,
            color = TextWhite,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
fun ExportAndHistorySection(
    history: List<AnalysisEntity>,
    selectedEntity: AnalysisEntity?,
    onEntitySelect: (AnalysisEntity) -> Unit,
    onDeleteClick: (AnalysisEntity) -> Unit,
    onExportClick: (AnalysisEntity) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = DarkCardElevated,
        border = BorderStroke(1.dp, NeonGreen.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null, tint = NeonGreenBright, modifier = Modifier.size(28.dp))
                Text(
                    "EXPORTAR LAUDO TÉCNICO",
                    color = TextWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    letterSpacing = 0.5.sp
                )
            }

            Text(
                "Selecione uma análise do histórico para gerar o documento PDF:",
                color = TextMuted,
                fontSize = 12.sp
            )

            // History List inside Export Box
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 280.dp)
                    .background(PureBlack.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (history.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("Histórico vazio", color = TextDim, fontSize = 12.sp)
                    }
                } else {
                    history.forEach { entity ->
                        val isSelected = selectedEntity?.id == entity.id
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onEntitySelect(entity) },
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) NeonGreen.copy(alpha = 0.1f) else Color.Transparent,
                            border = BorderStroke(1.dp, if (isSelected) NeonGreenBright else Color.Transparent)
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isSelected) NeonGreenBright else BorderSubtle,
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        if (isSelected) {
                                            Icon(Icons.Default.Check, null, tint = PureBlack, modifier = Modifier.padding(4.dp))
                                        }
                                    }
                                    Column {
                                        Text(entity.gradeCode, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                        Text(entity.standard, color = TextMuted, fontSize = 11.sp)
                                    }
                                }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Text(
                                        "${entity.confidence}%",
                                        color = if (isSelected) NeonGreenBright else TextMuted,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        fontFamily = FontFamily.Monospace
                                    )
                                    IconButton(
                                        onClick = { onDeleteClick(entity) },
                                        modifier = Modifier.size(24.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Excluir",
                                            tint = Color.Red.copy(alpha = 0.8f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { selectedEntity?.let { onExportClick(it) } },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                enabled = selectedEntity != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonGreen,
                    contentColor = PureBlack,
                    disabledContainerColor = DarkCard,
                    disabledContentColor = TextDim
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedEntity != null) "GERAR PDF DE ${selectedEntity.gradeCode}" else "SELECIONE UMA ANÁLISE",
                    fontWeight = FontWeight.Black,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
fun BestPracticesGuide() {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        GuideItem(
            title = "Pressão Constante",
            description = "Mantenha uma pressão firme e uniforme contra o rebolo para garantir um fluxo de faíscas estável."
        )
        GuideItem(
            title = "Ângulo de Observação",
            description = "Observe as faíscas tangencialmente ao fluxo. Isso ajuda a identificar melhor as explosões de carbono."
        )
        GuideItem(
            title = "Iluminação Controlada",
            description = "Realize o teste em ambiente com luz difusa. Evite luz solar direta que pode 'lavar' a cor das faíscas."
        )
    }
}

@Composable
fun GuideItem(title: String, description: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = DarkCard,
        border = BorderStroke(1.dp, Color(0x1AFFFFFF))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = SparkOrange, modifier = Modifier.size(18.dp))
            Column {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(description, color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp)
            }
        }
    }
}
