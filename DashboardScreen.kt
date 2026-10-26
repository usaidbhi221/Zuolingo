package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.DashboardTab
import com.example.ui.ZuolingoViewModel
import com.example.ui.theme.DuolingoGreen
import com.example.ui.theme.DuolingoGreenDark

@Composable
fun DashboardScreen(
    viewModel: ZuolingoViewModel,
    onStartQuiz: (Int) -> Unit
) {
    val currentTab by viewModel.currentTab.collectAsState()

    BackHandler(enabled = currentTab != DashboardTab.HOME) {
        viewModel.setTab(DashboardTab.HOME)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_screen"),
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    tonalElevation = 6.dp,
                    modifier = Modifier
                        .widthIn(max = 680.dp)
                        .navigationBarsPadding()
                        .testTag("dashboard_bottom_bar")
                ) {
                    // 1. Home Tab
                    NavigationBarItem(
                        selected = currentTab == DashboardTab.HOME,
                        onClick = { viewModel.setTab(DashboardTab.HOME) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Home",
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == DashboardTab.HOME) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DuolingoGreenDark,
                            selectedTextColor = DuolingoGreenDark,
                            indicatorColor = Color(0xFFD7FFB8)
                        ),
                        modifier = Modifier.testTag("tab_home")
                    )

                    // 2. Levels Tab (Winding game path)
                    NavigationBarItem(
                        selected = currentTab == DashboardTab.LEVELS,
                        onClick = { viewModel.setTab(DashboardTab.LEVELS) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.SportsEsports,
                                contentDescription = "Levels",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "1000 Map",
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == DashboardTab.LEVELS) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DuolingoGreenDark,
                            selectedTextColor = DuolingoGreenDark,
                            indicatorColor = Color(0xFFD7FFB8)
                        ),
                        modifier = Modifier.testTag("tab_levels")
                    )

                    // 3. AI Brain Tab (WhatsApp Style)
                    NavigationBarItem(
                        selected = currentTab == DashboardTab.AI_BRAIN,
                        onClick = { viewModel.setTab(DashboardTab.AI_BRAIN) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = "AI Brain",
                                modifier = Modifier.size(26.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "AI Brain",
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == DashboardTab.AI_BRAIN) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DuolingoGreenDark,
                            selectedTextColor = DuolingoGreenDark,
                            indicatorColor = Color(0xFFD7FFB8)
                        ),
                        modifier = Modifier.testTag("tab_ai_brain")
                    )

                    // 4. Profile Tab
                    NavigationBarItem(
                        selected = currentTab == DashboardTab.PROFILE,
                        onClick = { viewModel.setTab(DashboardTab.PROFILE) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = {
                            Text(
                                text = "Profile",
                                fontSize = 11.sp,
                                fontWeight = if (currentTab == DashboardTab.PROFILE) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = DuolingoGreenDark,
                            selectedTextColor = DuolingoGreenDark,
                            indicatorColor = Color(0xFFD7FFB8)
                        ),
                        modifier = Modifier.testTag("tab_profile")
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
            Crossfade(targetState = currentTab, label = "tab_crossfade") { tab ->
                when (tab) {
                    DashboardTab.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToAiBrain = { viewModel.setTab(DashboardTab.AI_BRAIN) },
                        onStartQuiz = { level -> onStartQuiz(level) },
                        onOpenLevelSelection = { page ->
                            viewModel.setLevelPage(page)
                            viewModel.setTab(DashboardTab.LEVELS)
                        }
                    )

                    DashboardTab.LEVELS -> LevelSelectionScreen(
                        viewModel = viewModel,
                        onBackClick = { viewModel.setTab(DashboardTab.HOME) },
                        onLevelSelected = { level -> onStartQuiz(level) }
                    )

                    DashboardTab.AI_BRAIN -> AiBrainScreen(
                        viewModel = viewModel
                    )

                    DashboardTab.PROFILE -> ProfileScreen(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
