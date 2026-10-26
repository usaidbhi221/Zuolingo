package com.example.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.ArabicCurriculumRepository
import com.example.ui.ZuolingoViewModel
import com.example.ui.theme.DuolingoGemBlue
import com.example.ui.theme.DuolingoGoldCrown
import com.example.ui.theme.DuolingoGreen
import com.example.ui.theme.DuolingoGreenDark
import com.example.ui.theme.DuolingoHeartRed
import com.example.ui.theme.DuolingoStreakFlame
import com.example.ui.theme.DuolingoStreakFlameLight

@Composable
fun HomeScreen(
    viewModel: ZuolingoViewModel,
    onNavigateToAiBrain: () -> Unit,
    onStartQuiz: (Int) -> Unit,
    onOpenLevelSelection: (Int) -> Unit
) {
    val userProfile by viewModel.userProfile.collectAsState()

    // Outer container centering content for desktop/tablet screens
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
                .testTag("home_screen_column"),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // --- 1. DUOLINGO TOP STATS BAR (No outlines, pure clean elevation) ---
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(18.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Streak Flame
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("streak_indicator")
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Streak",
                            tint = DuolingoStreakFlame,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.streakDays}",
                            fontWeight = FontWeight.Black,
                            color = DuolingoStreakFlame,
                            fontSize = 16.sp
                        )
                    }

                    // Gems (Blue Diamond)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.testTag("gems_indicator")
                    ) {
                        Text(text = "💎", fontSize = 16.sp)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.gems}",
                            fontWeight = FontWeight.Black,
                            color = DuolingoGemBlue,
                            fontSize = 16.sp
                        )
                    }

                    // Hearts (Red Heart with Refill on tap)
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .testTag("hearts_indicator")
                            .clickable { viewModel.refillHearts() }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Hearts",
                            tint = DuolingoHeartRed,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${userProfile.hearts}",
                            fontWeight = FontWeight.Black,
                            color = DuolingoHeartRed,
                            fontSize = 16.sp
                        )
                    }

                    // Rank
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Rank",
                            tint = DuolingoGoldCrown,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Lvl ${userProfile.highestUnlockedLevel}",
                            fontWeight = FontWeight.Black,
                            color = DuolingoGoldCrown,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // --- 2. 7-DAY DUOLINGO STREAK LINE (As requested!) ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "🔥 7-Day Streak Goal",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Black,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Text(
                                text = "7/7 Days Active",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuolingoStreakFlame
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // 7 Days Line: Mon, Tue, Wed, Thu, Fri, Sat, Sun
                        val dayNames = listOf("M", "T", "W", "T", "F", "S", "S")
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            dayNames.forEachIndexed { index, day ->
                                val isDayActive = userProfile.weekDaysActive.getOrElse(index) { true }
                                val isToday = index == 6 // Sunday / current day

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(38.dp)
                                            .clip(CircleShape)
                                            .background(
                                                if (isDayActive) DuolingoStreakFlame
                                                else MaterialTheme.colorScheme.surfaceVariant
                                            )
                                    ) {
                                        if (isDayActive) {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = "Completed",
                                                tint = Color.White,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        } else {
                                            Text(
                                                text = "○",
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = day,
                                        fontSize = 12.sp,
                                        fontWeight = if (isToday) FontWeight.Black else FontWeight.Bold,
                                        color = if (isToday) DuolingoGreenDark else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- 3. TOP AI BRAIN QUICK CHAT BOX (Seamless WhatsApp style, No harsh outlines) ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("home_ai_brain_box")
                        .clickable { onNavigateToAiBrain() },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE8F5E9))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Psychology,
                                        contentDescription = "AI Brain",
                                        tint = DuolingoGreen,
                                        modifier = Modifier.size(26.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "AI Brain",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 18.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Instant Arabic Grammar, Nahw & Sarf",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Open",
                                tint = DuolingoGreen
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // WhatsApp-style Pill Search Bar (No outline!)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xFFF0FDF4))
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "💬 Type any word or sentence to analyze...",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.weight(1f)
                            )
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(DuolingoGreen)
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "Chat",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }

            // --- 4. CONTINUE LESSON HERO CARD ---
            item {
                val activeLevel = userProfile.highestUnlockedLevel
                val levelTitle = ArabicCurriculumRepository.getLevelTitle(activeLevel)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("continue_learning_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(DuolingoGreen.copy(alpha = 0.15f))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "ACTIVE MISSION",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Black,
                                    color = DuolingoGreenDark
                                )
                            }

                            Text(
                                text = "10 Words • Level $activeLevel",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = levelTitle,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "Learn 10 essential Arabic words and build sentences step-by-step.",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Duolingo 3D Button style (Solid green with bottom press depth)
                        Button(
                            onClick = { onStartQuiz(activeLevel) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("start_lesson_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = DuolingoGreen),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "START 10-WORD LESSON",
                                fontWeight = FontWeight.Black,
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // --- 5. WORD OF THE DAY ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "✨ Word of the Day • كَلِمَةُ الْيَوْمِ",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = DuolingoStreakFlame
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "الْحِكْمَةُ (Al-Ḥikmatu)",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Wisdom & Insight • دانائی اور حکمت",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        IconButton(
                            onClick = { viewModel.audioHelper.speakArabic("الْحِكْمَةُ") },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak",
                                tint = DuolingoGreen
                            )
                        }
                    }
                }
            }

            // --- 6. 1000 LEVEL MAP PREVIEW ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Explore 1000 Game Levels",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "View Map ➜",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuolingoGreen,
                        modifier = Modifier.clickable { onOpenLevelSelection(1) }
                    )
                }
            }

            // Quick Stage Cards
            val stages = listOf(
                Triple(1, "Stage 1: Levels 1–50", "حروف الهجاء والحركات (Alphabets & Phonetics)"),
                Triple(2, "Stage 2: Levels 51–150", "المفردات اليومية (Core Vocabulary & Nouns)"),
                Triple(3, "Stage 3: Levels 151–300", "المركبات والتراكيب (Two-Word Compounds)"),
                Triple(4, "Stage 4: Levels 301–500", "الجمل الاسمية والفعلية (Basic Sentences)")
            )

            items(stages.size) { index ->
                val (stageNum, stageName, stageDesc) = stages[index]
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onOpenLevelSelection(((stageNum - 1) * 5) + 1) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE8F5E9))
                        ) {
                            Text(
                                text = "S$stageNum",
                                fontWeight = FontWeight.Black,
                                color = DuolingoGreenDark,
                                fontSize = 14.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stageName,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = stageDesc,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "View",
                            tint = DuolingoGreen,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}
