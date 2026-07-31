package com.example.ui.comparator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BigO
import com.example.data.model.DataStructure
import kotlin.math.log2
import kotlin.math.min

// --- Color Helpers for Big-O Complexity Badges ---
@Composable
fun getBigOColor(bigO: BigO): Color {
    return when (bigO.rating) {
        1 -> Color(0xFF10B981) // Green (Excellent)
        2 -> Color(0xFF84CC16) // Lime Green
        3 -> Color(0xFFEAB308) // Yellow
        4 -> Color(0xFFF97316) // Orange
        else -> Color(0xFFEF4444) // Red (Poor)
    }
}

@Composable
fun BigOBadge(bigO: BigO, label: String? = null) {
    val bgColor = getBigOColor(bigO).copy(alpha = 0.15f)
    val textColor = getBigOColor(bigO)

    Surface(
        color = bgColor,
        shape = RoundedCornerShape(8.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, textColor.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (label != null) {
                Text(
                    text = "$label: ",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = bigO.notation,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = textColor,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

// --- Big-O Growth Curves Canvas ---
@Composable
fun BigOGrowthChart(
    selectedDSList: List<DataStructure>
) {
    var elementN by remember { mutableFloatStateOf(100f) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("big_o_chart_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Big-O Growth Curves",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "N = ${elementN.toInt()} elements",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Slider for N
            Slider(
                value = elementN,
                onValueChange = { elementN = it },
                valueRange = 10f..1000f,
                modifier = Modifier.testTag("n_elements_slider")
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Canvas Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(12.dp)
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Grid lines
                    val numGridLines = 4
                    for (i in 0..numGridLines) {
                        val y = h * i / numGridLines
                        drawLine(
                            color = Color.Gray.copy(alpha = 0.2f),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1f
                        )
                    }

                    // Curve Plotting
                    fun plotCurve(color: Color, func: (Float) -> Float) {
                        val path = Path()
                        val steps = 50
                        for (step in 0..steps) {
                            val normX = step.toFloat() / steps
                            val x = normX * w
                            val inputN = normX * elementN
                            val valY = min(func(inputN), elementN * 2)
                            val normY = valY / (elementN * 2)
                            val y = h - (normY * h)

                            if (step == 0) path.moveTo(x, y) else path.lineTo(x, y)
                        }
                        drawPath(path = path, color = color, style = Stroke(width = 4f))
                    }

                    // O(1) - Green
                    plotCurve(Color(0xFF10B981)) { 10f }

                    // O(log n) - Lime
                    plotCurve(Color(0xFF84CC16)) { n -> (log2(n.toDouble().coerceAtLeast(1.0)) * 15).toFloat() }

                    // O(n) - Yellow
                    plotCurve(Color(0xFFEAB308)) { n -> n * 0.2f }

                    // O(n log n) - Orange
                    plotCurve(Color(0xFFF97316)) { n -> (n * log2(n.toDouble().coerceAtLeast(1.0)) * 0.05).toFloat() }

                    // O(n^2) - Red
                    plotCurve(Color(0xFFEF4444)) { n -> (n * n * 0.001f) }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                LegendItem(Color(0xFF10B981), "O(1)")
                LegendItem(Color(0xFF84CC16), "O(log n)")
                LegendItem(Color(0xFFEAB308), "O(n)")
                LegendItem(Color(0xFFF97316), "O(n log n)")
                LegendItem(Color(0xFFEF4444), "O(n²)")
            }
        }
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontFamily = FontFamily.Monospace
        )
    }
}

// --- Side by Side Matrix Comparison ---
@Composable
fun SideBySideDSCompareTable(
    selectedDS: List<DataStructure>
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Operation Complexity Matrix",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Header Row with DS Names
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Operation",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1.2f)
                )
                selectedDS.forEach { ds ->
                    Text(
                        text = ds.name,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Divider(modifier = Modifier.padding(vertical = 8.dp))

            // Access
            CompareMatrixRow("Access (Avg)", selectedDS) { it.complexity.accessAvg }
            CompareMatrixRow("Search (Avg)", selectedDS) { it.complexity.searchAvg }
            CompareMatrixRow("Insert (Avg)", selectedDS) { it.complexity.insertAvg }
            CompareMatrixRow("Delete (Avg)", selectedDS) { it.complexity.deleteAvg }
            CompareMatrixRow("Worst Space", selectedDS) { it.complexity.spaceWorst }
        }
    }
}

@Composable
fun CompareMatrixRow(
    operationName: String,
    selectedDS: List<DataStructure>,
    bigOExtractor: (DataStructure) -> BigO
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = operationName,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1.2f)
        )
        selectedDS.forEach { ds ->
            Box(modifier = Modifier.weight(1f)) {
                BigOBadge(bigO = bigOExtractor(ds))
            }
        }
    }
}
