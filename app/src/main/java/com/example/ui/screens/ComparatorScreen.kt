package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Compare
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.MainViewModel
import com.example.ui.comparator.BigOGrowthChart
import com.example.ui.comparator.SideBySideDSCompareTable

@Composable
fun ComparatorScreen(
    viewModel: MainViewModel
) {
    val allDS = viewModel.allDSList
    val selectedDSIds by viewModel.comparatorSelectedIds.collectAsState()
    val selectedDSList by viewModel.comparatorSelectedDS.collectAsState()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .testTag("comparator_screen")
    ) {
        // Header
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Data Structure Battle & Comparator",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Select up to 3 data structures to analyze their trade-offs and complexity curves.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Selection Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(allDS) { ds ->
                    val isSelected = selectedDSIds.contains(ds.id)
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.toggleComparatorDS(ds.id) },
                        label = { Text(ds.name) },
                        leadingIcon = {
                            if (isSelected) {
                                Icon(Icons.Default.Compare, contentDescription = "Selected", modifier = Modifier.size(16.dp))
                            }
                        },
                        modifier = Modifier.testTag("chip_ds_${ds.id}")
                    )
                }
            }
        }

        // Side by Side Matrix Table
        SideBySideDSCompareTable(selectedDS = selectedDSList)

        // Mathematical Big-O Growth Curves Canvas Chart
        BigOGrowthChart(selectedDSList = selectedDSList)

        // Scenario Recommender Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Recommendation",
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "CS Scenario Matcher",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                val scenarioText = when {
                    selectedDSIds.contains("hash_table") && selectedDSIds.contains("bst") ->
                        "• Key Lookup: Choose Hash Table for O(1) avg lookup when order doesn't matter.\n• Range Queries: Choose BST/AVL for ordered iteration and range queries like [K1..K2]."
                    selectedDSIds.contains("array_list") && selectedDSIds.contains("linked_list") ->
                        "• Random Indexing: Choose Dynamic Array for instantaneous O(1) random index access.\n• Head Insertions: Choose Linked List for O(1) head pushes without resize pauses."
                    else ->
                        "• Memory Overhead: Array-based structures minimize pointer overhead and maximize CPU cache locality.\n• Dynamic Node Pointers: Linked/Tree structures eliminate array reallocation pauses at the cost of pointer bytes."
                }

                Text(
                    text = scenarioText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
