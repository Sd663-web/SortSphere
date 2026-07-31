package com.example.ui.quiz

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.FlipToBack
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

data class QuizQuestion(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

object QuizData {
    val sampleQuestions = listOf(
        QuizQuestion(
            id = 1,
            question = "What is the worst-case search time complexity of a standard Binary Search Tree (BST)?",
            options = listOf("O(1)", "O(log n)", "O(n)", "O(n log n)"),
            correctAnswerIndex = 2,
            explanation = "If elements are inserted in sorted order, an un-balanced BST degenerates into a single long linked list, making search O(n)."
        ),
        QuizQuestion(
            id = 2,
            question = "Which data structure provides amortized O(1) time complexity for tail insertions?",
            options = listOf("Singly Linked List", "Dynamic Array (ArrayList)", "Binary Heap", "AVL Tree"),
            correctAnswerIndex = 1,
            explanation = "Dynamic arrays allocate double buffer capacity when full. Although resizing takes O(n), it happens so infrequently that average/amortized cost is O(1)."
        ),
        QuizQuestion(
            id = 3,
            question = "Why is a Binary Heap faster than an AVL Tree for priority queue operations?",
            options = listOf(
                "It has lower constant factors and stores nodes densely in an array without pointer overhead.",
                "It guarantees O(1) search time for all keys.",
                "It automatically balances left and right height.",
                "It uses hash codes instead of comparisons."
            ),
            correctAnswerIndex = 0,
            explanation = "Binary Heaps use contiguous array indexing (parent at i, children at 2i+1, 2i+2), maximizing CPU cache locality without pointer traversal overhead."
        ),
        QuizQuestion(
            id = 4,
            question = "Which collision resolution strategy in Hash Tables uses linked lists inside array buckets?",
            options = listOf("Linear Probing", "Quadratic Probing", "Separate Chaining", "Double Hashing"),
            correctAnswerIndex = 2,
            explanation = "Separate Chaining stores a linked list (or small tree) at each hash bucket index to handle keys with identical hash indices."
        ),
        QuizQuestion(
            id = 5,
            question = "What is the primary advantage of a Trie over a Hash Map for auto-complete?",
            options = listOf(
                "O(k) prefix matching speed independent of dataset size N.",
                "Lower memory footprint per node.",
                "O(1) worst-case insertion.",
                "Automatic thread safety."
            ),
            correctAnswerIndex = 0,
            explanation = "Trie prefix searches only depend on the length of the string k, permitting instant prefix matching for auto-complete without scanning N keys."
        )
    )
}

@Composable
fun QuizCard(
    question: QuizQuestion,
    onAnswerSelected: (Boolean) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var isSubmitted by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .testTag("quiz_card")
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Question ${question.id}",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = question.question,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))

            question.options.forEachIndexed { index, option ->
                val isCorrect = index == question.correctAnswerIndex
                val isSelected = index == selectedOption

                val containerColor = when {
                    isSubmitted && isCorrect -> MaterialTheme.colorScheme.primaryContainer
                    isSubmitted && isSelected && !isCorrect -> MaterialTheme.colorScheme.errorContainer
                    isSelected -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }

                OutlinedCard(
                    onClick = {
                        if (!isSubmitted) {
                            selectedOption = index
                            isSubmitted = true
                            onAnswerSelected(index == question.correctAnswerIndex)
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .testTag("quiz_option_$index")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSubmitted) {
                            if (isCorrect) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = MaterialTheme.colorScheme.primary)
                            } else if (isSelected) {
                                Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            if (isSubmitted) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "Explanation:",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}
