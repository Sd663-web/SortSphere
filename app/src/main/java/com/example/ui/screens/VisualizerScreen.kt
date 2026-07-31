package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.VisualizerType
import com.example.ui.MainViewModel
import com.example.ui.comparator.BigOBadge
import com.example.ui.visualizer.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VisualizerScreen(
    viewModel: MainViewModel
) {
    val activeDS by viewModel.activeDS.collectAsState()
    val visualizerStep by viewModel.visualizerStep.collectAsState()
    val isPlaying by viewModel.isPlaying.collectAsState()

    val arrayData by viewModel.arrayDataState.collectAsState()
    val linkedListData by viewModel.linkedListDataState.collectAsState()
    val stackData by viewModel.stackDataState.collectAsState()
    val queueData by viewModel.queueDataState.collectAsState()
    val hashTableData by viewModel.hashTableDataState.collectAsState()
    val treeData by viewModel.treeNodeDataState.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("visualizer_screen")
    ) {
        // DS Selector Bar
        Text(
            text = activeDS.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = activeDS.tagLine,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Big-O Summary Bar
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BigOBadge(bigO = activeDS.complexity.accessAvg, label = "Access")
                BigOBadge(bigO = activeDS.complexity.searchAvg, label = "Search")
                BigOBadge(bigO = activeDS.complexity.insertAvg, label = "Insert")
                BigOBadge(bigO = activeDS.complexity.deleteAvg, label = "Delete")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Visualizer Render Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                when (activeDS.defaultInteractiveType) {
                    VisualizerType.ARRAY -> {
                        ArrayVisualizerView(
                            arrayData = arrayData,
                            highlightIndex = visualizerStep % arrayData.size,
                            targetIndex = if (visualizerStep > 2) 2 else null,
                            operationText = "Scanning Index $visualizerStep"
                        )
                    }
                    VisualizerType.LINKED_LIST -> {
                        LinkedListVisualizerView(
                            nodes = linkedListData,
                            activeIndex = visualizerStep % linkedListData.size,
                            isDoubly = activeDS.id == "doubly_linked_list"
                        )
                    }
                    VisualizerType.STACK_QUEUE -> {
                        StackQueueVisualizerView(
                            items = if (activeDS.id == "stack") stackData else queueData,
                            isStack = activeDS.id == "stack",
                            activeItemIndex = visualizerStep
                        )
                    }
                    VisualizerType.HASH_TABLE -> {
                        HashTableVisualizerView(
                            buckets = hashTableData,
                            activeBucketIndex = visualizerStep % hashTableData.size
                        )
                    }
                    VisualizerType.BINARY_SEARCH_TREE, VisualizerType.HEAP, VisualizerType.GRAPH -> {
                        TreeVisualizerView(
                            nodes = treeData,
                            highlightNode = treeData.getOrNull(visualizerStep % treeData.size)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Playback & Step Control Toolbar
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = { viewModel.prevVisualizerStep() },
                                modifier = Modifier.testTag("step_prev_button")
                            ) {
                                Icon(Icons.Default.SkipPrevious, contentDescription = "Step Back")
                            }

                            FilledIconButton(
                                onClick = { viewModel.toggleAutoPlay() },
                                modifier = Modifier.testTag("play_pause_button")
                            ) {
                                Icon(
                                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlaying) "Pause" else "Play"
                                )
                            }

                            IconButton(
                                onClick = { viewModel.nextVisualizerStep() },
                                modifier = Modifier.testTag("step_next_button")
                            ) {
                                Icon(Icons.Default.SkipNext, contentDescription = "Step Forward")
                            }
                        }

                        Text(
                            text = "Step ${visualizerStep + 1} of ${activeDS.pseudocode.size}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Pseudocode Line Highlight Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Algorithm Pseudocode",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        imageVector = Icons.Default.Code,
                        contentDescription = "Code",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                        .padding(12.dp)
                ) {
                    activeDS.pseudocode.forEachIndexed { lineIdx, lineText ->
                        val isCurrentStep = lineIdx == (visualizerStep % activeDS.pseudocode.size)
                        val lineBg = if (isCurrentStep) MaterialTheme.colorScheme.primaryContainer else androidx.compose.ui.graphics.Color.Transparent
                        val textColor = if (isCurrentStep) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(lineBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${lineIdx + 1}.",
                                style = MaterialTheme.typography.labelSmall,
                                fontFamily = FontFamily.Monospace,
                                color = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.width(28.dp)
                            )
                            Text(
                                text = lineText,
                                style = MaterialTheme.typography.bodySmall,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = if (isCurrentStep) FontWeight.Bold else FontWeight.Normal,
                                color = textColor
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Memory Layout & Architecture Notes
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Memory Footprint & Cache Locality",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = activeDS.memoryLayout,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Key Trade-offs",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(6.dp))

                activeDS.pros.forEach { pro ->
                    Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Check, contentDescription = "Pro", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = pro, style = MaterialTheme.typography.bodySmall)
                    }
                }

                activeDS.cons.forEach { con ->
                    Row(modifier = Modifier.padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Close, contentDescription = "Con", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = con, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
