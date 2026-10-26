package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionType
import com.example.ui.QuizState
import com.example.ui.ZuolingoViewModel
import com.example.ui.theme.DuolingoGemBlue
import com.example.ui.theme.DuolingoGoldCrown
import com.example.ui.theme.DuolingoGreen
import com.example.ui.theme.DuolingoGreenDark
import com.example.ui.theme.DuolingoHeartRed
import com.example.ui.theme.DuolingoStreakFlame

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun QuizScreen(
    viewModel: ZuolingoViewModel,
    onExitQuiz: () -> Unit
) {
    val quizState by viewModel.quizState.collectAsState()
    val levelInfo = quizState.levelInfo ?: return
    val questions = levelInfo.questions

    var showExitDialog by remember { mutableStateOf(false) }

    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No questions found for this level.")
        }
        return
    }

    val currentQ = questions[quizState.currentQuestionIndex.coerceIn(questions.indices)]
    val progress = (quizState.currentQuestionIndex.toFloat()) / questions.size.toFloat()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F9FA))
            .testTag("quiz_screen"),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .widthIn(max = 680.dp)
                .padding(bottom = if (quizState.isSubmitted) 130.dp else 90.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // --- 1. TOP APP BAR (Duolingo Header) ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    onClick = { showExitDialog = true },
                    modifier = Modifier.testTag("quiz_close_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Exit Quiz",
                        tint = Color(0xFF757575)
                    )
                }

                // Clean Duolingo Progress Bar (Round, green, no black outline)
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .weight(1f)
                        .height(14.dp)
                        .padding(horizontal = 12.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = DuolingoGreen,
                    trackColor = Color(0xFFE0E0E0)
                )

                // Hearts
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.testTag("quiz_hearts")
                ) {
                    Icon(
                        imageVector = Icons.Default.Favorite,
                        contentDescription = "Hearts Left",
                        tint = DuolingoHeartRed,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${quizState.heartsRemaining}",
                        fontWeight = FontWeight.Black,
                        color = DuolingoHeartRed,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // --- 2. QUESTION HEADER & PROMPT ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Word ${quizState.currentQuestionIndex + 1} of ${questions.size} (10 Words)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DuolingoGreenDark
                    )

                    // Rule tip toggle button
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFFFF8E1))
                            .clickable { viewModel.toggleExplanationDialog(true) }
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = Color(0xFFF57F17),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "قاعدہ (Grammar Rule)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFF57F17)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Prompts
                Text(
                    text = currentQ.promptUrdu,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF212121)
                )
                Text(
                    text = currentQ.promptEnglish,
                    fontSize = 14.sp,
                    color = Color(0xFF757575)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Target Arabic Word / Audio Banner
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = currentQ.targetArabic,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Black,
                                color = DuolingoGreenDark
                            )
                            if (currentQ.transliteration.isNotBlank()) {
                                Text(
                                    text = currentQ.transliteration,
                                    fontSize = 14.sp,
                                    color = Color(0xFF757575)
                                )
                            }
                        }

                        IconButton(
                            onClick = { viewModel.audioHelper.speakArabic(currentQ.targetArabic) },
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(DuolingoGreen)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak",
                                tint = Color.White
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- 3. QUESTION BODY ---
            when (currentQ.type) {
                QuestionType.SENTENCE_BUILDER -> {
                    SentenceBuilderSection(
                        question = currentQ,
                        quizState = quizState,
                        onAddWord = { viewModel.addWordChip(it) },
                        onRemoveWord = { viewModel.removeWordChip(it) }
                    )
                }
                else -> {
                    WordChoiceSection(
                        question = currentQ,
                        selectedOption = quizState.selectedOption,
                        isSubmitted = quizState.isSubmitted,
                        onSelectOption = { viewModel.selectOption(it) }
                    )
                }
            }
        }

        // --- 4. BOTTOM ACTION & FEEDBACK SHEET ---
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .widthIn(max = 680.dp)
        ) {
            if (quizState.isSubmitted) {
                FeedbackBottomBanner(
                    isCorrect = quizState.isCorrect,
                    nahwExplanation = currentQ.nahwExplanation,
                    correctAnswer = currentQ.correctAnswer,
                    onContinue = {
                        // Directly moves to next word!
                        viewModel.nextQuestionOrFinish()
                    }
                )
            } else {
                val canSubmit = when (currentQ.type) {
                    QuestionType.SENTENCE_BUILDER -> quizState.assembledWords.isNotEmpty()
                    else -> quizState.selectedOption != null
                }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color.White,
                    shadowElevation = 8.dp
                ) {
                    Box(modifier = Modifier.padding(16.dp)) {
                        Button(
                            onClick = { viewModel.submitAnswer() },
                            enabled = canSubmit,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                                .testTag("quiz_check_button"),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DuolingoGreen,
                                disabledContainerColor = Color(0xFFE0E0E0)
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Text(
                                text = "CHECK",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = if (canSubmit) Color.White else Color(0xFF9E9E9E)
                            )
                        }
                    }
                }
            }
        }
    }

    // Grammar Rule Dialog
    if (quizState.showExplanationDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.toggleExplanationDialog(false) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFFF57F17),
                    modifier = Modifier.size(28.dp)
                )
            },
            title = {
                Text(
                    text = "قواعد النحو والصرف (Grammar Breakdown)",
                    fontWeight = FontWeight.Black,
                    fontSize = 17.sp
                )
            },
            text = {
                Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                    Text(
                        text = "💡 القاعدة (Rule):",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFF57F17),
                        fontSize = 14.sp
                    )
                    Text(
                        text = currentQ.qaidaTip,
                        fontSize = 13.sp,
                        color = Color(0xFF424242)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "📚 التحليل النحوي والإعراب (Nahw):",
                        fontWeight = FontWeight.Bold,
                        color = DuolingoGreenDark,
                        fontSize = 14.sp
                    )
                    Text(
                        text = currentQ.nahwExplanation,
                        fontSize = 13.sp,
                        color = Color(0xFF424242)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "🔬 الصرف والميزان (Sarf):",
                        fontWeight = FontWeight.Bold,
                        color = DuolingoGoldCrown,
                        fontSize = 14.sp
                    )
                    Text(
                        text = currentQ.sarfExplanation,
                        fontSize = 13.sp,
                        color = Color(0xFF424242)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { viewModel.toggleExplanationDialog(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = DuolingoGreen)
                ) {
                    Text("فهمت (Got it)", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Exit Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Lesson?", fontWeight = FontWeight.Bold) },
            text = { Text("Are you sure you want to exit? Your progress in this lesson will be lost.") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        onExitQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = DuolingoHeartRed)
                ) {
                    Text("Exit", color = Color.White)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Keep Learning")
                }
            }
        )
    }

    // Level Completed Celebration Dialog
    if (quizState.isCompleted) {
        LevelCompletedDialog(
            levelNumber = quizState.levelNumber,
            starsAwarded = quizState.starsAwarded,
            sessionXp = quizState.sessionXp,
            onContinue = {
                onExitQuiz()
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SentenceBuilderSection(
    question: com.example.data.model.QuizQuestion,
    quizState: QuizState,
    onAddWord: (String) -> Unit,
    onRemoveWord: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        Text(
            text = "Tap word chips to arrange in order:",
            fontSize = 13.sp,
            color = Color(0xFF757575)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Target Drop Slot (Clean container with soft bottom border)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(18.dp))
                .background(Color.White)
                .padding(16.dp)
        ) {
            if (quizState.assembledWords.isEmpty()) {
                Text(
                    text = "Tap words below to place here...",
                    fontSize = 14.sp,
                    color = Color(0xFF9E9E9E),
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    quizState.assembledWords.forEachIndexed { index, word ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(DuolingoGreen)
                                .clickable { onRemoveWord(index) }
                                .padding(horizontal = 14.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = word,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Word Bank (بنك الكلمات):",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF424242)
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Pool of available word chips
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            question.arabicWordsPool.forEach { word ->
                val countInAssembled = quizState.assembledWords.count { it == word }
                val countInPool = question.arabicWordsPool.count { it == word }
                val isUsed = countInAssembled >= countInPool

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(if (isUsed) Color(0xFFE0E0E0) else Color.White)
                        .clickable(enabled = !isUsed && !quizState.isSubmitted) { onAddWord(word) }
                        .padding(horizontal = 18.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = word,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isUsed) Color(0xFF9E9E9E) else Color(0xFF212121)
                    )
                }
            }
        }
    }
}

@Composable
fun WordChoiceSection(
    question: com.example.data.model.QuizQuestion,
    selectedOption: String?,
    isSubmitted: Boolean,
    onSelectOption: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        question.options.forEach { option ->
            val isSelected = selectedOption == option

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isSubmitted) { onSelectOption(option) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) Color(0xFFD7FFB8) else Color.White
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 3.dp else 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = Color(0xFF212121),
                        modifier = Modifier.weight(1f)
                    )

                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) DuolingoGreen else Color(0xFFEEEEEE)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FeedbackBottomBanner(
    isCorrect: Boolean,
    nahwExplanation: String,
    correctAnswer: String,
    onContinue: () -> Unit
) {
    val bgColor = if (isCorrect) Color(0xFFD7FFB8) else Color(0xFFFFDFE0)
    val textColor = if (isCorrect) Color(0xFF2E7D32) else Color(0xFFC62828)
    val buttonColor = if (isCorrect) DuolingoGreen else DuolingoHeartRed

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(bgColor)
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isCorrect) Icons.Default.Check else Icons.Default.ErrorOutline,
                    contentDescription = null,
                    tint = textColor,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = if (isCorrect) "🎉 ممتاز! Excellent! (+20 XP)" else "Needs Practice!",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black,
                    color = textColor
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            if (!isCorrect) {
                Text(
                    text = "Correct answer: $correctAnswer",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }

            if (nahwExplanation.isNotBlank()) {
                Text(
                    text = nahwExplanation,
                    fontSize = 12.sp,
                    color = textColor.copy(alpha = 0.9f),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // CONTINUE button directly proceeds to the next word!
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("quiz_continue_button"),
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "CONTINUE",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun LevelCompletedDialog(
    levelNumber: Int,
    starsAwarded: Int,
    sessionXp: Int,
    onContinue: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onContinue,
        title = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎉 LEVEL $levelNumber COMPLETED!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    color = DuolingoGreenDark
                )
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "أحسنت! أتممت بنجاح جميع الكلمات العشر لهذا المستوى",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF424242),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (i in 1..3) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star",
                            tint = if (i <= starsAwarded) DuolingoGoldCrown else Color.LightGray,
                            modifier = Modifier.size(40.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF1F8E9))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "💎 +$sessionXp", fontSize = 17.sp, fontWeight = FontWeight.Black, color = DuolingoGreenDark)
                        Text(text = "XP Earned", fontSize = 12.sp, color = Color(0xFF616161))
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🔥 +1 Day", fontSize = 17.sp, fontWeight = FontWeight.Black, color = DuolingoStreakFlame)
                        Text(text = "Streak Kept", fontSize = 12.sp, color = Color(0xFF616161))
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("quiz_finish_button"),
                colors = ButtonDefaults.buttonColors(containerColor = DuolingoGreen),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text("CONTINUE TO MAP", fontWeight = FontWeight.Black, color = Color.White)
            }
        }
    )
}
