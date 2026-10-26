package com.example.ui.screens

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.ArabicCurriculumRepository
import com.example.ui.ZuolingoViewModel
import com.example.ui.theme.DuolingoGoldCrown
import com.example.ui.theme.DuolingoGreen
import com.example.ui.theme.DuolingoGreenDark

@Composable
fun LevelSelectionScreen(
    viewModel: ZuolingoViewModel,
    onBackClick: () -> Unit,
    onLevelSelected: (Int) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()
    val currentPage by viewModel.currentLevelPage.collectAsState()

    var showJumpDialog by remember { mutableStateOf(false) }
    var jumpInput by remember { mutableStateOf("") }

    // Levels per page (10 levels on a winding path, exactly matching Image 3)
    val levelsPerPage = 10
    val startLevel = ((currentPage - 1) * levelsPerPage) + 1
    val endLevel = (startLevel + 9).coerceAtMost(ArabicCurriculumRepository.TOTAL_LEVELS)
    val totalPages = ArabicCurriculumRepository.TOTAL_LEVELS / levelsPerPage

    val (tierNum, tierTitle) = ArabicCurriculumRepository.getTierForLevel(startLevel)

    // Animation for active level bounce
    val infiniteTransition = rememberInfiniteTransition(label = "active_bounce")
    val bounceOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -6f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bounce"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFE8F5E9),
                        Color(0xFFF1F8E9),
                        Color(0xFFDCEDC8)
                    )
                )
            ),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
        ) {
            // --- TOP HEADER (Duolingo Stage Banner) ---
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = DuolingoGreen),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.testTag("level_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "SECTION $tierNum • 1000 LEVELS",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = "Levels $startLevel – $endLevel",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }

                        // Jump Dialog Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.25f))
                                .clickable { showJumpDialog = true }
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "1000 🎯",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                        }
                    }

                    // Section switcher tabs
                    ScrollableTabRow(
                        selectedTabIndex = ((currentPage - 1) / 10).coerceIn(0, 9),
                        edgePadding = 8.dp,
                        containerColor = Color.Transparent,
                        contentColor = Color.White,
                        divider = {}
                    ) {
                        for (stage in 1..10) {
                            val stageStart = ((stage - 1) * 100) + 1
                            val stageEnd = stage * 100
                            val isSelected = ((currentPage - 1) / 10) == (stage - 1)

                            Tab(
                                selected = isSelected,
                                onClick = { viewModel.jumpToStage(stage) },
                                text = {
                                    Text(
                                        text = "$stageStart–$stageEnd",
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Bold,
                                        color = if (isSelected) Color.White else Color.White.copy(alpha = 0.6f)
                                    )
                                }
                            )
                        }
                    }
                }
            }

            // --- WINDING PATH OF 10 LEVELS (Exact Image 3 game design!) ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(vertical = 20.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // List of 10 levels on the current page
                val levelIndices = (startLevel..endLevel).toList()

                items(levelIndices) { levelNum ->
                    val isCompleted = levelNum in userProfile.completedLevels
                    val isUnlocked = levelNum <= userProfile.highestUnlockedLevel || isCompleted
                    val isActive = levelNum == userProfile.highestUnlockedLevel
                    val stars = userProfile.levelStars[levelNum] ?: (if (isCompleted) 3 else 0)

                    // Zig-zag offset like Duolingo / Image 3 winding path:
                    // Center, Left, Right, Center, Left, Right...
                    val xOffset = when ((levelNum - startLevel) % 4) {
                        0 -> 0.dp
                        1 -> (-50).dp
                        2 -> 0.dp
                        3 -> 50.dp
                        else -> 0.dp
                    }

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .offset(x = xOffset)
                            .padding(vertical = 4.dp)
                    ) {
                        // If active, show adorable mascot owl indicator floating above!
                        if (isActive) {
                            Box(
                                modifier = Modifier
                                    .offset(y = bounceOffset.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "🦉 START", fontSize = 11.sp, fontWeight = FontWeight.Black, color = DuolingoGreenDark)
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                        }

                        // The 3D Game Stepping Stone (Shield / Circle)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(76.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isActive -> DuolingoGoldCrown
                                        isCompleted -> DuolingoGreen
                                        isUnlocked -> DuolingoGreen
                                        else -> Color(0xFFB0BEC5)
                                    }
                                )
                                .clickable(enabled = isUnlocked) { onLevelSelected(levelNum) }
                                .testTag("level_stone_$levelNum")
                        ) {
                            // Inner button surface for 3D depth
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(66.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isActive -> Color(0xFFFFD54F)
                                            isCompleted -> DuolingoGreen
                                            isUnlocked -> Color(0xFF66BB6A)
                                            else -> Color(0xFFCFD8DC)
                                        }
                                    )
                            ) {
                                if (isCompleted) {
                                    // Game Completed Icon
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Completed",
                                        tint = Color.White,
                                        modifier = Modifier.size(34.dp)
                                    )
                                } else if (isActive) {
                                    // Active level star
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Active",
                                        tint = Color(0xFFE65100),
                                        modifier = Modifier.size(34.dp)
                                    )
                                } else if (isUnlocked) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = "Unlocked",
                                        tint = Color.White,
                                        modifier = Modifier.size(30.dp)
                                    )
                                } else {
                                    // Locked shield
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Locked",
                                        tint = Color(0xFF78909C),
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Level Number & Stars (⭐⭐⭐)
                        Text(
                            text = "Level $levelNum",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isUnlocked) Color(0xFF2E7D32) else Color(0xFF78909C)
                        )

                        if (isCompleted) {
                            Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                                for (s in 1..3) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = DuolingoGoldCrown,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- BOTTOM PAGINATION CONTROLLER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { viewModel.prevPage() },
                    enabled = currentPage > 1,
                    modifier = Modifier.testTag("prev_level_page")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Previous Page",
                        tint = if (currentPage > 1) DuolingoGreen else Color.LightGray
                    )
                }

                Text(
                    text = "Page $currentPage of $totalPages",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF37474F)
                )

                IconButton(
                    onClick = { viewModel.nextPage() },
                    enabled = currentPage < totalPages,
                    modifier = Modifier.testTag("next_level_page")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Next Page",
                        tint = if (currentPage < totalPages) DuolingoGreen else Color.LightGray
                    )
                }
            }
        }
    }

    // Direct Jump Dialog
    if (showJumpDialog) {
        AlertDialog(
            onDismissRequest = { showJumpDialog = false },
            title = {
                Text("Jump to Any Level (1–1000)", fontWeight = FontWeight.Black)
            },
            text = {
                Column {
                    Text("Enter any level from 1 to 1000 to practice immediately:", fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = jumpInput,
                        onValueChange = { jumpInput = it.filter { c -> c.isDigit() } },
                        label = { Text("Level Number") },
                        placeholder = { Text("e.g. 8, 150, 750, 1000") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val lvl = jumpInput.toIntOrNull()
                        if (lvl != null && lvl in 1..1000) {
                            val targetPage = ((lvl - 1) / levelsPerPage) + 1
                            viewModel.setLevelPage(targetPage)
                            showJumpDialog = false
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DuolingoGreen)
                ) {
                    Text("Go", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showJumpDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
