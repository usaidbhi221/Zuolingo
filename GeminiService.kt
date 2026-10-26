package com.example.service

import android.util.Log
import com.example.BuildConfig
import com.example.data.engine.ArabicGrammarEngine
import com.example.data.model.ArabicAnalysis
import com.example.data.model.PartOfSpeech
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiService {
    private const val TAG = "GeminiService"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    suspend fun queryAiBrain(prompt: String): Pair<String, ArabicAnalysis?> = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Throwable) {
            ""
        }

        // If no valid key or placeholder, use the offline Grammar Engine
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY" || apiKey.length < 10) {
            val local = ArabicGrammarEngine.analyzeWord(prompt)
            val responseText = buildExplanationText(local)
            return@withContext Pair(responseText, local)
        }

        try {
            val systemPrompt = """
                You are 'Zuolingo AI Brain', the ultimate Arabic tutor specializing in Nahw (نحو), Sarf (صرف), and vocabulary.
                When given any Arabic word, letter (harf), or sentence:
                Provide a complete, structured grammatical breakdown suitable for learners:
                1. Word / اللفظ
                2. Part of Speech / نوع الكلمة (اِسْم / فِعْل / حَرْف)
                3. Meaning in Urdu & English / المعنى
                4. Nahw / النحو (I'rab, role like Fa'il, Maf'ool, Mubtada, Khabar, Harf Jarr, Majroor)
                5. Sarf / الصرف (Root/حروف اصلیہ, Baab, Seegheh/Tense)
                6. Qaida / القاعدة (Easy rule explanation so even a child or beginner understands)
                7. Examples / أمثلة مع الترجمة
                Format your reply cleanly with emojis, Arabic vocalized text (with tashkeel), and easy Urdu & English explanations.
            """.trimIndent()

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().put("text", "Please analyze this Arabic word or question: $prompt"))
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().put("text", systemPrompt))
                    })
                })
            }

            val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(requestBody)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val bodyString = response.body?.string() ?: ""
                val responseJson = JSONObject(bodyString)
                val candidates = responseJson.optJSONArray("candidates")
                val firstCandidate = candidates?.optJSONObject(0)
                val content = firstCandidate?.optJSONObject("content")
                val parts = content?.optJSONArray("parts")
                val text = parts?.optJSONObject(0)?.optString("text")

                if (!text.isNullOrBlank()) {
                    val local = ArabicGrammarEngine.analyzeWord(prompt)
                    return@withContext Pair(text, local)
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Gemini API call failed, falling back to local grammar engine: ${e.message}")
        }

        // Seamless fallback
        val local = ArabicGrammarEngine.analyzeWord(prompt)
        val responseText = buildExplanationText(local)
        Pair(responseText, local)
    }

    private fun buildExplanationText(analysis: ArabicAnalysis): String {
        return buildString {
            append("✨ **تحليل كلمة: ${analysis.word}** (${analysis.transliteration})\n\n")
            append("🏷️ **النوع (Part of Speech):** ${analysis.partOfSpeech.arabicTitle} (${analysis.partOfSpeech.urduTitle} / ${analysis.partOfSpeech.englishTitle})\n\n")
            append("📖 **المعنى (Meaning):**\n")
            append("• اردو: ${analysis.urduMeaning}\n")
            append("• English: ${analysis.englishMeaning}\n\n")
            append("📚 **النحو والإعراب (Nahw / Syntax):**\n")
            append("${analysis.nahw}\n\n")
            append("🔬 **الصرف والاشتقاق (Sarf / Morphology):**\n")
            append("${analysis.sarf}\n\n")
            append("💡 **القاعدة الميسرة (Rule / Kaeda):**\n")
            append("${analysis.qaida}\n\n")
            if (analysis.examples.isNotEmpty()) {
                append("🌟 **أمثلة في جمل (Examples):**\n")
                analysis.examples.forEach { (arb, trans) ->
                    append("• $arb ➔ $trans\n")
                }
            }
        }
    }
}
