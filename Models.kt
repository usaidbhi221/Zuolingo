package com.example.data.model

import com.example.ui.theme.AppThemeMode

enum class PartOfSpeech(val arabicTitle: String, val englishTitle: String, val urduTitle: String) {
    ISM("اِسْم", "Noun", "اسم"),
    FIL("فِعْل", "Verb", "فعل"),
    HARF("حَرْف", "Particle", "حرف")
}

data class ArabicAnalysis(
    val word: String,
    val transliteration: String,
    val partOfSpeech: PartOfSpeech,
    val urduMeaning: String,
    val englishMeaning: String,
    val nahw: String,
    val sarf: String,
    val qaida: String,
    val examples: List<Pair<String, String>> = emptyList()
)

data class AiChatMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val timestamp: Long = System.currentTimeMillis(),
    val analysis: ArabicAnalysis? = null,
    val isLoading: Boolean = false
)

enum class QuestionType {
    WORD_CHOICE,        // Choose correct translation / meaning
    SENTENCE_BUILDER,   // Assemble Arabic word chips in order
    FILL_BLANK,         // Select the missing word or particle (Harf)
    GRAMMAR_RULE        // Identify Ism/Fi'l/Harf or grammatical case
}

data class QuizQuestion(
    val id: String,
    val levelNumber: Int,
    val type: QuestionType,
    val promptUrdu: String,
    val promptEnglish: String,
    val targetArabic: String,
    val arabicWordsPool: List<String> = emptyList(), // For Sentence Builder
    val correctAnswer: String,
    val options: List<String> = emptyList(),
    val transliteration: String = "",
    val nahwExplanation: String = "",
    val sarfExplanation: String = "",
    val qaidaTip: String = ""
)

data class LevelInfo(
    val levelNumber: Int,
    val tierNumber: Int,
    val tierTitle: String,
    val title: String,
    val description: String,
    val stars: Int = 0, // 0 to 3
    val isUnlocked: Boolean = false,
    val isCompleted: Boolean = false,
    val questions: List<QuizQuestion> = emptyList() // Exactly 10 questions per level!
)

data class UserProfile(
    val username: String = "الطالب الذكي (Smart Student)",
    val email: String = "student@zuolingo.app",
    val avatar: String = "🦉",
    val streakDays: Int = 7,
    val totalXp: Int = 480,
    val gems: Int = 180,
    val hearts: Int = 5,
    val maxHearts: Int = 5,
    val highestUnlockedLevel: Int = 8,
    val completedLevels: Set<Int> = setOf(1, 2, 3, 4, 5, 6, 7),
    val levelStars: Map<Int, Int> = mapOf(1 to 3, 2 to 3, 3 to 3, 4 to 2, 5 to 3, 6 to 3, 7 to 2),
    val themeMode: AppThemeMode = AppThemeMode.ZUOLINGO_GREEN_WHITE,
    val customColorHex: Long = 0xFF58CC02,
    val chatBackgroundIndex: Int = 0,
    val chatBubbleStyleIndex: Int = 0,
    val weekDaysActive: List<Boolean> = listOf(true, true, true, true, true, true, true), // Mon to Sun
    val soundEnabled: Boolean = true,
    val hapticsEnabled: Boolean = true,
    val isLoggedIn: Boolean = true
)
