package com.example.data.repository

import com.example.data.model.LevelInfo
import com.example.data.model.QuestionType
import com.example.data.model.QuizQuestion

object ArabicCurriculumRepository {

    const val TOTAL_LEVELS = 1000
    const val QUESTIONS_PER_LEVEL = 10 // Exactly 10 words per level as requested!

    fun getTierForLevel(level: Int): Pair<Int, String> {
        return when {
            level <= 50 -> 1 to "المرحلة 1: حروف الهجاء والحركات (Alphabets & Phonetics)"
            level <= 150 -> 2 to "المرحلة 2: المفردات والكلمات اليومية (Core Vocabulary)"
            level <= 300 -> 3 to "المرحلة 3: المركبات والعبارات (Two-Word Compounds)"
            level <= 500 -> 4 to "المرحلة 4: الجمل الاسمية والفعلية (Basic Sentences)"
            level <= 750 -> 5 to "المرحلة 5: أسرار النحو والصرف (Grammar & Rules)"
            else -> 6 to "المرحلة 6: العربية القرآنية والطلاقة (Quranic & Fluency)"
        }
    }

    fun getLevelTitle(level: Int): String {
        return when (level) {
            1 -> "المستوى 1: الحروف الأولى (أ، ب، ت)"
            2 -> "المستوى 2: حروف (ث، ج، ح، خ)"
            3 -> "المستوى 3: الحركات القصيرة (فتحة، كسرة، ضمة)"
            4 -> "المستوى 4: السكون والشدة والتنوين"
            5 -> "المستوى 5: مفردات البيت والأسرة"
            6 -> "المستوى 6: الألوان والفواكه"
            7 -> "المستوى 7: الحيوانات والطبيعة"
            8 -> "المستوى 8: أدوات المدرسة والكتابة (Classroom & Writing)"
            9 -> "المستوى 9: أفعال الحركة (ذَهَبَ، خَرَجَ، دَخَلَ)"
            10 -> "المستوى 10: الأرقام والعد (١ إلى ١٠)"
            else -> {
                val tier = getTierForLevel(level).first
                when (tier) {
                    1 -> "المستوى $level: أصوات الحروف وبناء الكلمة"
                    2 -> "المستوى $level: قاموس الكلمات اليومية"
                    3 -> "المستوى $level: المركبات التوصيفية والإضافية"
                    4 -> "المستوى $level: بناء الجمل الاسمية والفعلية"
                    5 -> "المستوى $level: قواعد الإعراب والنحو والصرف"
                    else -> "المستوى $level: بلاغة وفصاحة لسان العرب"
                }
            }
        }
    }

    // Comprehensive bank of curated curriculum items for levels
    private val masterVocabBank = listOf(
        // Tier 1 items
        Triple("أَسَدٌ", "Asadun", "شیر / Lion"),
        Triple("بَيْتٌ", "Baytun", "گھر / House"),
        Triple("تُفَّاحٌ", "Tuffāḥun", "سیب / Apple"),
        Triple("ثَوْبٌ", "Thawbun", "کپڑا / Garment"),
        Triple("جَمَلٌ", "Jamalun", "اونٹ / Camel"),
        Triple("حَلِيبٌ", "Ḥalībun", "دودھ / Milk"),
        Triple("خُبْزٌ", "Khubzun", "روٹی / Bread"),
        Triple("دَفْتَرٌ", "Daftarun", "کاپی / Notebook"),
        Triple("ذَهَبٌ", "Dhahabun", "سونا / Gold"),
        Triple("رَجُلٌ", "Rajulun", "آدمی / Man"),

        // Tier 2 items
        Triple("قَلَمٌ", "Qalamun", "قلم / Pen"),
        Triple("كِتَابٌ", "Kitābun", "کتاب / Book"),
        Triple("مَكْتَبٌ", "Maktabun", "میز / Desk"),
        Triple("مَدْرَسَةٌ", "Madrasatun", "اسکول / School"),
        Triple("مُعَلِّمٌ", "Mu'allimun", "استاد / Teacher"),
        Triple("طَالِبٌ", "Ṭālibun", "طالب علم / Student"),
        Triple("سَبُّورَةٌ", "Sabbūratun", "تختی / Blackboard"),
        Triple("حَقِيبَةٌ", "Ḥaqībatun", "بیگ / Schoolbag"),
        Triple("مَسْجِدٌ", "Masjidun", "مسجد / Mosque"),
        Triple("بَابٌ", "Bābun", "دروازہ / Door"),

        // Tier 3 & 4 items (Phrases & Sentences)
        Triple("الْوَلَدُ نَشِيطٌ", "Al-waladu nashīṭun", "لڑکا چست ہے / The boy is active"),
        Triple("كَتَبَ الطَّالِبُ", "Kataba aṭ-ṭālibu", "طالب علم نے لکھا / The student wrote"),
        Triple("قَرَأَ الْوَلَدُ", "Qara'a al-waladu", "لڑکے نے پڑھا / The boy read"),
        Triple("شَرِبَ الْمَاءَ", "Shariba al-mā'a", "اس نے پانی پیا / He drank water"),
        Triple("فِي الْمَدْرَسَةِ", "Fī al-madrasati", "اسکول میں / In the school"),
        Triple("عَلَى الْمَكْتَبِ", "’Alā al-maktabi", "میز پر / On the desk"),
        Triple("الْبَابُ مَفْتُوحٌ", "Al-bābu maftūḥun", "دروازہ کھلا ہے / The door is open"),
        Triple("الشَّمْسُ طَالِعَةٌ", "Ash-shamsu ṭāli'atun", "سورج طلوع ہے / The sun is rising"),
        Triple("الْقَمَرُ مُنِيرٌ", "Al-qamaru munīrun", "چاند روشن ہے / The moon is shining"),
        Triple("الْحَمْدُ لِلَّهِ", "Al-ḥamdu lillāhi", "تمام تعریفیں اللہ کے لیے ہیں / All praise is for Allah")
    )

    fun getQuestionsForLevel(level: Int): List<QuizQuestion> {
        val questions = mutableListOf<QuizQuestion>()
        val baseOffset = ((level - 1) * 3) % masterVocabBank.size

        // Build exactly 10 questions for this level
        for (i in 0 until QUESTIONS_PER_LEVEL) {
            val item = masterVocabBank[(baseOffset + i) % masterVocabBank.size]
            val isSentenceBuilder = (i % 2 == 1) // Alternate between sentence builder and word choice

            if (isSentenceBuilder && item.first.contains(" ")) {
                val words = item.first.split(" ")
                val distractor = if (i % 2 == 0) "فِي" else "عَلَى"
                questions.add(
                    QuizQuestion(
                        id = "L${level}_Q${i + 1}",
                        levelNumber = level,
                        type = QuestionType.SENTENCE_BUILDER,
                        promptUrdu = "الفاظ جوڑ کر جملہ بنائیں: '${item.third}'",
                        promptEnglish = "Assemble the sentence: '${item.third}'",
                        targetArabic = item.first,
                        arabicWordsPool = (words + distractor).shuffled(),
                        correctAnswer = item.first,
                        options = emptyList(),
                        transliteration = item.second,
                        nahwExplanation = "تركيب جملة عربية فصيحة، مطابقة بين المبتدأ والخبر أو الفعل والفاعل۔",
                        sarfExplanation = "صيغ وأوزان عربية صحيحة۔",
                        qaidaTip = "قاعدہ: عربی میں جملہ بنانے کے لیے الفاظ کی نحوی ترتیب کا لحاظ رکھا جاتا ہے۔"
                    )
                )
            } else {
                val distractors = listOf("گاڑی / Car", "پانی / Water", "سیب / Apple", "گھر / House")
                    .filter { it != item.third }
                    .take(3)
                val options = (distractors + item.third).shuffled()

                questions.add(
                    QuizQuestion(
                        id = "L${level}_Q${i + 1}",
                        levelNumber = level,
                        type = QuestionType.WORD_CHOICE,
                        promptUrdu = "عربی لفظ '${item.first}' کا صحیح معنی کیا ہے؟",
                        promptEnglish = "What does '${item.first}' mean?",
                        targetArabic = item.first,
                        correctAnswer = item.third,
                        options = options,
                        transliteration = item.second,
                        nahwExplanation = "اسم/فعل/حرف معرب، علامة الإعراب تظهر على آخره۔",
                        sarfExplanation = "ثلاثي مجرد، حروف أصلية وميزان صرفي۔",
                        qaidaTip = "قاعدہ: ہر عربی کلمے کا ایک مادہ اور معنی ہوتا ہے جس سے بات واضح ہوتی ہے۔"
                    )
                )
            }
        }

        return questions
    }

    fun getLevelInfo(levelNumber: Int, userCompletedLevels: Set<Int>, userLevelStars: Map<Int, Int>, highestUnlocked: Int): LevelInfo {
        val (tierNum, tierTitle) = getTierForLevel(levelNumber)
        val isCompleted = levelNumber in userCompletedLevels
        val isUnlocked = levelNumber <= highestUnlocked || isCompleted
        val stars = userLevelStars[levelNumber] ?: (if (isCompleted) 3 else 0)

        return LevelInfo(
            levelNumber = levelNumber,
            tierNumber = tierNum,
            tierTitle = tierTitle,
            title = getLevelTitle(levelNumber),
            description = "مستوى تدريبي تفاعلي ممتع يحتوي على 10 كلمات وجمل مع النحو والصرف.",
            stars = stars,
            isUnlocked = isUnlocked,
            isCompleted = isCompleted,
            questions = getQuestionsForLevel(levelNumber)
        )
    }
}
