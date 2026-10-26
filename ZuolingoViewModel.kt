package com.example.ui

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.AiChatMessage
import com.example.data.model.ArabicAnalysis
import com.example.data.model.LevelInfo
import com.example.data.model.QuestionType
import com.example.data.model.QuizQuestion
import com.example.data.model.UserProfile
import com.example.data.repository.ArabicCurriculumRepository
import com.example.service.GeminiService
import com.example.ui.theme.AppThemeMode
import com.example.util.AudioHelper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

sealed class AppScreen {
    object Splash : AppScreen()
    data class Dashboard(val initialTab: DashboardTab = DashboardTab.HOME) : AppScreen()
    data class Quiz(val levelNumber: Int) : AppScreen()
}

enum class DashboardTab(val titleUrdu: String, val titleEnglish: String) {
    HOME("ہوم", "Home"),
    LEVELS("مراحل", "Levels"),
    AI_BRAIN("العقل الذكي", "AI Brain"),
    PROFILE("پروفائل", "Profile")
}

data class QuizState(
    val levelNumber: Int = 1,
    val levelInfo: LevelInfo? = null,
    val currentQuestionIndex: Int = 0,
    val assembledWords: List<String> = emptyList(),
    val selectedOption: String? = null,
    val isSubmitted: Boolean = false,
    val isCorrect: Boolean = false,
    val heartsRemaining: Int = 5,
    val sessionXp: Int = 0,
    val isCompleted: Boolean = false,
    val starsAwarded: Int = 0,
    val showExplanationDialog: Boolean = false
)

class ZuolingoViewModel(application: Application) : AndroidViewModel(application) {

    val audioHelper = AudioHelper(application)
    private val prefs: SharedPreferences = application.getSharedPreferences("zuolingo_prefs", Context.MODE_PRIVATE)

    // Navigation State
    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Splash)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _currentTab = MutableStateFlow(DashboardTab.HOME)
    val currentTab: StateFlow<DashboardTab> = _currentTab.asStateFlow()

    // User Profile with Persistent Storage
    private val _userProfile = MutableStateFlow(loadUserProfile())
    val userProfile: StateFlow<UserProfile> = _userProfile.asStateFlow()

    // Level Selection Page State (Levels 1 to 1000)
    private val _currentLevelPage = MutableStateFlow(1)
    val currentLevelPage: StateFlow<Int> = _currentLevelPage.asStateFlow()

    // Quiz Session State
    private val _quizState = MutableStateFlow(QuizState())
    val quizState: StateFlow<QuizState> = _quizState.asStateFlow()

    // AI Brain WhatsApp Chat Messages
    private val _chatMessages = MutableStateFlow<List<AiChatMessage>>(emptyList())
    val chatMessages: StateFlow<List<AiChatMessage>> = _chatMessages.asStateFlow()

    private val _isAiThinking = MutableStateFlow(false)
    val isAiThinking: StateFlow<Boolean> = _isAiThinking.asStateFlow()

    init {
        // Initial WhatsApp welcome message
        val welcomeMsg = AiChatMessage(
            id = "welcome_1",
            isUser = false,
            text = "السلام عليكم ورحمة الله وبركاته! مرحباً بك في **AI Brain** 🧠\nأنا معلمك الخاص للغة العربية، وقواعد النحو والصرف.\nاكتب أي كلمة، حرف، أو جملة وسأشرحها لك بالتفصيل فوراً.",
            timestamp = System.currentTimeMillis()
        )
        _chatMessages.value = listOf(welcomeMsg)

        // Splash auto dismiss after smooth clean animation
        viewModelScope.launch {
            delay(2200)
            if (_currentScreen.value is AppScreen.Splash) {
                _currentScreen.value = AppScreen.Dashboard(DashboardTab.HOME)
            }
        }
    }

    // --- Persistence Handlers ---
    private fun loadUserProfile(): UserProfile {
        val username = prefs.getString("username", "الطالب الذكي (Smart Student)") ?: "الطالب الذكي (Smart Student)"
        val email = prefs.getString("email", "learner@zuolingo.app") ?: "learner@zuolingo.app"
        val avatar = prefs.getString("avatar", "🦉") ?: "🦉"
        val streak = prefs.getInt("streakDays", 7)
        val xp = prefs.getInt("totalXp", 480)
        val gems = prefs.getInt("gems", 180)
        val hearts = prefs.getInt("hearts", 5)
        val highestLvl = prefs.getInt("highestUnlockedLevel", 8)
        val themeOrdinal = prefs.getInt("themeMode", AppThemeMode.ZUOLINGO_GREEN_WHITE.ordinal)
        val themeMode = AppThemeMode.values().getOrElse(themeOrdinal) { AppThemeMode.ZUOLINGO_GREEN_WHITE }
        val customColorHex = prefs.getLong("customColorHex", 0xFF58CC02)
        val chatBg = prefs.getInt("chatBackgroundIndex", 0)
        val bubbleStyle = prefs.getInt("chatBubbleStyleIndex", 0)

        val completedLevelsString = prefs.getString("completedLevels", "1,2,3,4,5,6,7") ?: "1,2,3,4,5,6,7"
        val completedLevels = completedLevelsString.split(",")
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()

        return UserProfile(
            username = username,
            email = email,
            avatar = avatar,
            streakDays = streak,
            totalXp = xp,
            gems = gems,
            hearts = hearts,
            highestUnlockedLevel = highestLvl,
            completedLevels = completedLevels,
            themeMode = themeMode,
            customColorHex = customColorHex,
            chatBackgroundIndex = chatBg,
            chatBubbleStyleIndex = bubbleStyle,
            weekDaysActive = listOf(true, true, true, true, true, true, true)
        )
    }

    private fun saveUserProfile(profile: UserProfile) {
        prefs.edit().apply {
            putString("username", profile.username)
            putString("email", profile.email)
            putString("avatar", profile.avatar)
            putInt("streakDays", profile.streakDays)
            putInt("totalXp", profile.totalXp)
            putInt("gems", profile.gems)
            putInt("hearts", profile.hearts)
            putInt("highestUnlockedLevel", profile.highestUnlockedLevel)
            putInt("themeMode", profile.themeMode.ordinal)
            putLong("customColorHex", profile.customColorHex)
            putInt("chatBackgroundIndex", profile.chatBackgroundIndex)
            putInt("chatBubbleStyleIndex", profile.chatBubbleStyleIndex)
            putString("completedLevels", profile.completedLevels.joinToString(","))
            apply()
        }
    }

    // Navigation
    fun navigateToDashboard(tab: DashboardTab = DashboardTab.HOME) {
        _currentTab.value = tab
        _currentScreen.value = AppScreen.Dashboard(tab)
    }

    fun setTab(tab: DashboardTab) {
        _currentTab.value = tab
    }

    fun startQuiz(levelNumber: Int) {
        val user = _userProfile.value
        val levelInfo = ArabicCurriculumRepository.getLevelInfo(
            levelNumber = levelNumber,
            userCompletedLevels = user.completedLevels,
            userLevelStars = user.levelStars,
            highestUnlocked = user.highestUnlockedLevel
        )
        _quizState.value = QuizState(
            levelNumber = levelNumber,
            levelInfo = levelInfo,
            currentQuestionIndex = 0,
            assembledWords = emptyList(),
            selectedOption = null,
            isSubmitted = false,
            isCorrect = false,
            heartsRemaining = user.hearts,
            sessionXp = 0,
            isCompleted = false,
            starsAwarded = 0,
            showExplanationDialog = false
        )
        _currentScreen.value = AppScreen.Quiz(levelNumber)
    }

    fun exitQuiz() {
        _currentScreen.value = AppScreen.Dashboard(DashboardTab.LEVELS)
    }

    // Quiz Actions
    fun addWordChip(word: String) {
        val current = _quizState.value
        if (current.isSubmitted) return
        _quizState.value = current.copy(assembledWords = current.assembledWords + word)
        if (_userProfile.value.soundEnabled) {
            audioHelper.speakArabic(word)
        }
    }

    fun removeWordChip(index: Int) {
        val current = _quizState.value
        if (current.isSubmitted) return
        val list = current.assembledWords.toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            _quizState.value = current.copy(assembledWords = list)
        }
    }

    fun selectOption(option: String) {
        val current = _quizState.value
        if (current.isSubmitted) return
        _quizState.value = current.copy(selectedOption = option)
    }

    fun toggleExplanationDialog(show: Boolean) {
        _quizState.value = _quizState.value.copy(showExplanationDialog = show)
    }

    fun submitAnswer() {
        val current = _quizState.value
        val questions = current.levelInfo?.questions ?: return
        if (current.currentQuestionIndex !in questions.indices) return
        val question = questions[current.currentQuestionIndex]

        val isCorrect = when (question.type) {
            QuestionType.SENTENCE_BUILDER -> {
                val assembled = current.assembledWords.joinToString(" ")
                val cleanAssembled = stripTashkeel(assembled).trim()
                val cleanTarget = stripTashkeel(question.correctAnswer).trim()
                assembled == question.correctAnswer || cleanAssembled == cleanTarget
            }
            else -> {
                current.selectedOption == question.correctAnswer
            }
        }

        if (isCorrect) {
            if (_userProfile.value.hapticsEnabled) audioHelper.vibrateSuccess()
            _quizState.value = current.copy(
                isSubmitted = true,
                isCorrect = true,
                sessionXp = current.sessionXp + 20
            )
        } else {
            if (_userProfile.value.hapticsEnabled) audioHelper.vibrateError()
            val newHearts = (current.heartsRemaining - 1).coerceAtLeast(0)
            _quizState.value = current.copy(
                isSubmitted = true,
                isCorrect = false,
                heartsRemaining = newHearts
            )
            val updatedProfile = _userProfile.value.copy(hearts = newHearts)
            _userProfile.value = updatedProfile
            saveUserProfile(updatedProfile)
        }
    }

    // Direct progression to next word on Continue
    fun nextQuestionOrFinish() {
        val current = _quizState.value
        val questions = current.levelInfo?.questions ?: return

        if (current.currentQuestionIndex + 1 < questions.size) {
            // Directly move to next word!
            _quizState.value = current.copy(
                currentQuestionIndex = current.currentQuestionIndex + 1,
                assembledWords = emptyList(),
                selectedOption = null,
                isSubmitted = false,
                isCorrect = false,
                showExplanationDialog = false
            )
        } else {
            // Completed all 10 words!
            val stars = when {
                current.heartsRemaining >= 4 -> 3
                current.heartsRemaining >= 2 -> 2
                else -> 1
            }

            val user = _userProfile.value
            val newCompleted = user.completedLevels + current.levelNumber
            val newStars = user.levelStars.toMutableMap()
            newStars[current.levelNumber] = maxOf(newStars[current.levelNumber] ?: 0, stars)
            val newHighest = maxOf(user.highestUnlockedLevel, current.levelNumber + 1)
            val newXp = user.totalXp + current.sessionXp + 50
            val newGems = user.gems + 25

            val updatedProfile = user.copy(
                completedLevels = newCompleted,
                levelStars = newStars,
                highestUnlockedLevel = newHighest,
                totalXp = newXp,
                gems = newGems
            )
            _userProfile.value = updatedProfile
            saveUserProfile(updatedProfile)

            _quizState.value = current.copy(
                isCompleted = true,
                starsAwarded = stars
            )
        }
    }

    // Level Page Navigation
    fun setLevelPage(page: Int) {
        _currentLevelPage.value = page.coerceIn(1, 100)
    }

    fun nextPage() {
        setLevelPage(_currentLevelPage.value + 1)
    }

    fun prevPage() {
        setLevelPage(_currentLevelPage.value - 1)
    }

    fun jumpToStage(stage: Int) {
        val targetPage = ((stage - 1) * 10) + 1
        setLevelPage(targetPage)
    }

    // AI Brain Chat
    fun sendAiBrainPrompt(query: String) {
        if (query.isBlank() || _isAiThinking.value) return

        val userMsg = AiChatMessage(
            id = UUID.randomUUID().toString(),
            isUser = true,
            text = query.trim(),
            timestamp = System.currentTimeMillis()
        )
        _chatMessages.value = _chatMessages.value + userMsg
        _isAiThinking.value = true

        viewModelScope.launch {
            val (replyText, analysis) = GeminiService.queryAiBrain(query.trim())
            val aiMsg = AiChatMessage(
                id = UUID.randomUUID().toString(),
                isUser = false,
                text = replyText,
                timestamp = System.currentTimeMillis(),
                analysis = analysis
            )
            _chatMessages.value = _chatMessages.value + aiMsg
            _isAiThinking.value = false
        }
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            AiChatMessage(
                id = UUID.randomUUID().toString(),
                isUser = false,
                text = "مرحباً بك مجدداً! تم مسح المحادثة. يمكنك كتابة أي كلمة أو جملة وسأجيبك فوراً.",
                timestamp = System.currentTimeMillis()
            )
        )
    }

    fun setChatBackground(index: Int) {
        val updated = _userProfile.value.copy(chatBackgroundIndex = index)
        _userProfile.value = updated
        saveUserProfile(updated)
    }

    fun setChatBubbleStyle(index: Int) {
        val updated = _userProfile.value.copy(chatBubbleStyleIndex = index)
        _userProfile.value = updated
        saveUserProfile(updated)
    }

    // Profile & Settings
    fun setTheme(theme: AppThemeMode) {
        val updated = _userProfile.value.copy(themeMode = theme)
        _userProfile.value = updated
        saveUserProfile(updated)
    }

    fun setCustomColorHex(colorHex: Long) {
        val updated = _userProfile.value.copy(
            customColorHex = colorHex,
            themeMode = AppThemeMode.CUSTOM
        )
        _userProfile.value = updated
        saveUserProfile(updated)
    }

    fun toggleSound(enabled: Boolean) {
        val updated = _userProfile.value.copy(soundEnabled = enabled)
        _userProfile.value = updated
        saveUserProfile(updated)
    }

    fun toggleHaptics(enabled: Boolean) {
        val updated = _userProfile.value.copy(hapticsEnabled = enabled)
        _userProfile.value = updated
        saveUserProfile(updated)
    }

    fun refillHearts() {
        val user = _userProfile.value
        if (user.gems >= 50 && user.hearts < user.maxHearts) {
            val updated = user.copy(
                gems = user.gems - 50,
                hearts = user.maxHearts
            )
            _userProfile.value = updated
            saveUserProfile(updated)
            _quizState.value = _quizState.value.copy(heartsRemaining = user.maxHearts)
        }
    }

    fun updateUserProfile(username: String, avatar: String) {
        val updated = _userProfile.value.copy(
            username = username,
            avatar = avatar,
            isLoggedIn = true
        )
        _userProfile.value = updated
        saveUserProfile(updated)
    }

    private fun stripTashkeel(text: String): String {
        return text.replace(Regex("[\u064B-\u065F\u0670]"), "")
    }

    override fun onCleared() {
        super.onCleared()
        audioHelper.shutdown()
    }
}
