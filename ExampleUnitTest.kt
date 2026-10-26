package com.example

import com.example.data.engine.ArabicGrammarEngine
import com.example.data.model.PartOfSpeech
import com.example.data.repository.ArabicCurriculumRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

    @Test
    fun testArabicGrammarEngine_harfDetection() {
        val analysis = ArabicGrammarEngine.analyzeWord("فِي")
        assertEquals(PartOfSpeech.HARF, analysis.partOfSpeech)
        assertTrue(analysis.urduMeaning.contains("میں"))
        assertTrue(analysis.nahw.contains("حَرْفُ جَرٍّ"))
    }

    @Test
    fun testArabicGrammarEngine_verbDetection() {
        val analysis = ArabicGrammarEngine.analyzeWord("كَتَبَ")
        assertEquals(PartOfSpeech.FIL, analysis.partOfSpeech)
        assertTrue(analysis.sarf.contains("ك-ت-ب"))
    }

    @Test
    fun testArabicGrammarEngine_nounDetection() {
        val analysis = ArabicGrammarEngine.analyzeWord("الْحَمْدُ")
        assertEquals(PartOfSpeech.ISM, analysis.partOfSpeech)
        assertTrue(analysis.nahw.contains("مُبْتَدَأٌ"))
    }

    @Test
    fun testCurriculumRepository_1000LevelsSupported() {
        assertEquals(1000, ArabicCurriculumRepository.TOTAL_LEVELS)

        // Test level 1 (Alphabet) - exactly 10 words
        val l1Questions = ArabicCurriculumRepository.getQuestionsForLevel(1)
        assertEquals(10, l1Questions.size)

        // Test level 8 (Classroom Sentence Building) - exactly 10 words
        val l8Questions = ArabicCurriculumRepository.getQuestionsForLevel(8)
        assertEquals(10, l8Questions.size)

        // Test Level 500 & 1000 - exactly 10 words
        val l500Questions = ArabicCurriculumRepository.getQuestionsForLevel(500)
        assertEquals(10, l500Questions.size)

        val l1000Questions = ArabicCurriculumRepository.getQuestionsForLevel(1000)
        assertEquals(10, l1000Questions.size)
    }
}
