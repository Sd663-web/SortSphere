package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import com.example.ui.MainViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

enum class NavTab(
    val title: String,
    val icon: ImageVector,
    val testTag: String
) {
    CATALOG("Catalog", Icons.Default.MenuBook, "tab_catalog"),
    VISUALIZER("Visualizer", Icons.Default.FlashOn, "tab_visualizer"),
    COMPARE("Compare", Icons.Default.Compare, "tab_compare"),
    QUIZ_NOTES("Quiz & Notes", Icons.Default.Psychology, "tab_quiz_notes"),
    AI_TUTOR("AI Tutor", Icons.Default.AutoAwesome, "tab_ai_tutor")
}

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                var selectedTab by remember { mutableStateOf(NavTab.CATALOG) }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavigationBar(
                            modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars)
                        ) {
                            NavTab.entries.forEach { tab ->
                                NavigationBarItem(
                                    selected = selectedTab == tab,
                                    onClick = { selectedTab = tab },
                                    icon = { Icon(tab.icon, contentDescription = tab.title) },
                                    label = { Text(tab.title) },
                                    modifier = Modifier.testTag(tab.testTag)
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (selectedTab) {
                            NavTab.CATALOG -> {
                                CatalogScreen(
                                    viewModel = viewModel,
                                    onSelectDS = { ds ->
                                        viewModel.setActiveDS(ds)
                                        selectedTab = NavTab.VISUALIZER
                                    }
                                )
                            }
                            NavTab.VISUALIZER -> {
                                VisualizerScreen(viewModel = viewModel)
                            }
                            NavTab.COMPARE -> {
                                ComparatorScreen(viewModel = viewModel)
                            }
                            NavTab.QUIZ_NOTES -> {
                                QuizNotesScreen(viewModel = viewModel)
                            }
                            NavTab.AI_TUTOR -> {
                                AITutorScreen(viewModel = viewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
