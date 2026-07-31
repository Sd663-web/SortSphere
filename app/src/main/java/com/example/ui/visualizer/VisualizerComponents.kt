package com.example.ui.visualizer

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.VisualizerType
import kotlinx.coroutines.delay

// --- Array / Dynamic Array Visualizer ---
@Composable
fun ArrayVisualizerView(
    arrayData: List<Int>,
    highlightIndex: Int?,
    targetIndex: Int?,
    operationText: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Contiguous Memory Buffer",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            itemsIndexed(arrayData) { index, value ->
                val isHighlighted = index == highlightIndex
                val isTarget = index == targetIndex
                val bgColor = when {
                    isTarget -> MaterialTheme.colorScheme.tertiaryContainer
                    isHighlighted -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
                val borderColor = when {
                    isTarget -> MaterialTheme.colorScheme.tertiary
                    isHighlighted -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.outline
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "[$index]",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontFamily = FontFamily.Monospace
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                            .testTag("array_cell_$index"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = value.toString(),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    if (isHighlighted || isTarget) {
                        Icon(
                            imageVector = Icons.Default.ArrowUpward,
                            contentDescription = "Pointer",
                            tint = if (isTarget) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .size(20.dp)
                                .padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- Linked List Visualizer ---
@Composable
fun LinkedListVisualizerView(
    nodes: List<Int>,
    activeIndex: Int?,
    isDoubly: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = if (isDoubly) "Doubly Linked Nodes" else "Singly Linked Nodes",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            contentPadding = PaddingValues(horizontal = 8.dp)
        ) {
            itemsIndexed(nodes) { index, value ->
                val isActive = index == activeIndex
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Node Card
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            2.dp,
                            if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .testTag("ll_node_$index")
                            .shadow(if (isActive) 8.dp else 2.dp, RoundedCornerShape(12.dp))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = if (index == 0) "HEAD" else if (index == nodes.size - 1) "TAIL" else "Node $index",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = value.toString(),
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Divider(
                                modifier = Modifier
                                    .height(32.dp)
                                    .width(1.dp),
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "next",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Pointer Arrow
                    if (index < nodes.size - 1) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Next Pointer",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            if (isDoubly) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Prev Pointer",
                                    tint = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    } else {
                        // Null pointer
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Pointer",
                                tint = MaterialTheme.colorScheme.outline
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "NULL",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }
                }
            }
        }
    }
}

// --- Stack & Queue Visualizer ---
@Composable
fun StackQueueVisualizerView(
    items: List<Int>,
    isStack: Boolean,
    activeItemIndex: Int?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isStack) "Stack Buffer (LIFO - Last In First Out)" else "Queue Buffer (FIFO - First In First Out)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (isStack) {
            // Stack rendered vertically
            Box(
                modifier = Modifier
                    .width(160.dp)
                    .heightIn(min = 180.dp)
                    .border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(8.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.Bottom),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items.reversed().forEachIndexed { revIdx, value ->
                        val originalIndex = items.size - 1 - revIdx
                        val isTop = revIdx == 0
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isTop) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("stack_item_$originalIndex")
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "$value ${if (isTop) "(TOP)" else ""}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isTop) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Queue rendered horizontally
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "DEQUEUE\n(Front)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Dequeue direction",
                    tint = MaterialTheme.colorScheme.error
                )

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items.forEachIndexed { idx, value ->
                        val isFront = idx == 0
                        val isRear = idx == items.size - 1
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isFront -> MaterialTheme.colorScheme.errorContainer
                                    isRear -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.surface
                                }
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .size(50.dp)
                                .testTag("queue_item_$idx")
                        ) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                Text(
                                    text = value.toString(),
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Enqueue direction",
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "ENQUEUE\n(Rear)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

// --- Hash Table Visualizer ---
@Composable
fun HashTableVisualizerView(
    buckets: List<List<Pair<String, Int>>>,
    activeBucketIndex: Int?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Hash Table Buckets (Chaining)",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(12.dp))

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            buckets.forEachIndexed { bucketIdx, chain ->
                val isActive = bucketIdx == activeBucketIndex
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Bucket Index Box
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isActive) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant)
                            .border(2.dp, if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                            .testTag("bucket_$bucketIdx"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "[$bucketIdx]",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Chain Arrow",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    if (chain.isEmpty()) {
                        Text(
                            text = "empty",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline,
                            fontFamily = FontFamily.Monospace
                        )
                    } else {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            itemsIndexed(chain) { _, pair ->
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    ),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text(
                                        text = "${pair.first}: ${pair.second}",
                                        style = MaterialTheme.typography.labelMedium,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- Binary Search Tree Visualizer (Canvas Rendered) ---
@Composable
fun TreeVisualizerView(
    nodes: List<Int>,
    highlightNode: Int?
) {
    val treeColor = MaterialTheme.colorScheme.primary
    val activeColor = MaterialTheme.colorScheme.tertiary
    val onPrimaryColor = MaterialTheme.colorScheme.onPrimary

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Binary Search Tree Hierarchy",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                .padding(8.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val width = size.width
                val height = size.height

                // Simple 3-level tree node locations
                val rootPos = Offset(width / 2f, 40.dp.toPx())
                val leftChildPos = Offset(width / 4f, 110.dp.toPx())
                val rightChildPos = Offset(3 * width / 4f, 110.dp.toPx())

                val l1Pos = Offset(width / 8f, 180.dp.toPx())
                val l2Pos = Offset(3 * width / 8f, 180.dp.toPx())
                val r1Pos = Offset(5 * width / 8f, 180.dp.toPx())
                val r2Pos = Offset(7 * width / 8f, 180.dp.toPx())

                // Draw Edges
                drawLine(treeColor, rootPos, leftChildPos, strokeWidth = 4f)
                drawLine(treeColor, rootPos, rightChildPos, strokeWidth = 4f)
                drawLine(treeColor, leftChildPos, l1Pos, strokeWidth = 3f)
                drawLine(treeColor, leftChildPos, l2Pos, strokeWidth = 3f)
                drawLine(treeColor, rightChildPos, r1Pos, strokeWidth = 3f)
                drawLine(treeColor, rightChildPos, r2Pos, strokeWidth = 3f)

                // Draw Nodes
                val nodePosList = listOf(rootPos, leftChildPos, rightChildPos, l1Pos, l2Pos, r1Pos, r2Pos)
                nodes.forEachIndexed { index, valNum ->
                    if (index < nodePosList.size) {
                        val pos = nodePosList[index]
                        val isHighlighted = valNum == highlightNode
                        drawCircle(
                            color = if (isHighlighted) activeColor else treeColor,
                            radius = 22.dp.toPx(),
                            center = pos
                        )
                    }
                }
            }

            // Node overlay texts
            val nodeValues = nodes
            val textPositions = listOf(
                Alignment.TopCenter to Offset(0f, 22.dp.value),
                Alignment.TopStart to Offset(80.dp.value, 92.dp.value),
                Alignment.TopEnd to Offset((-80).dp.value, 92.dp.value)
            )

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                nodeValues.take(7).forEach { value ->
                    Text(
                        text = value.toString(),
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier.padding(top = 28.dp)
                    )
                }
            }
        }
    }
}
