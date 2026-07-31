package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.ui.MainViewModel
import com.example.ui.quiz.QuizCard
import com.example.ui.quiz.QuizData

@Composable
fun QuizNotesScreen(
    viewModel: MainViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Quiz, 1 = Student Notes
    val activeDS by viewModel.activeDS.collectAsState()
    val noteEntity by viewModel.getNoteForDs(activeDS.id).collectAsState(initial = null)

    var studentNoteText by remember(activeDS.id, noteEntity?.content) {
        mutableStateOf(noteEntity?.content ?: "")
    }

    var totalScore by remember { mutableIntStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("quiz_notes_screen")
    ) {
        // Tab Selector
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                text = { Text("CS Big-O Quiz") },
                icon = { Icon(Icons.Default.Quiz, contentDescription = "Quiz") },
                modifier = Modifier.testTag("tab_quiz")
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                text = { Text("Study Notes") },
                icon = { Icon(Icons.Default.EditNote, contentDescription = "Notes") },
                modifier = Modifier.testTag("tab_notes")
            )
        }

        if (selectedTab == 0) {
            // Quiz Tab
            Column(modifier = Modifier.fillMaxSize()) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Complexity Mastery Quiz",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Score: $totalScore / ${QuizData.sampleQuestions.size}",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(QuizData.sampleQuestions) { q ->
                        QuizCard(
                            question = q,
                            onAnswerSelected = { isCorrect ->
                                if (isCorrect) totalScore += 1
                            }
                        )
                    }
                }
            }
        } else {
            // Study Notes Tab
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Personal Notes: ${activeDS.name}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Notes are saved locally in Room Database for offline revision.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = studentNoteText,
                    onValueChange = { studentNoteText = it },
                    placeholder = { Text("Write your CS lecture notes, formulas, or tricky edge cases here...") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("student_note_input"),
                    shape = RoundedCornerShape(16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        viewModel.saveNote(activeDS.id, studentNoteText)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_note_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Bookmark, contentDescription = "Save")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Notes to Database")
                }
            }
        }
    }
}
