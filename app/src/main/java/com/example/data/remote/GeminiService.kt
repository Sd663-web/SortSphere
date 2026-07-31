package com.example.data.remote

import com.example.BuildConfig
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

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun askCSAI(prompt: String, contextDSName: String? = null): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY
        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext "Gemini API key is not configured yet. Please configure your API key in the Secrets panel in AI Studio."
        }

        val systemPrompt = "You are an expert Computer Science Professor specializing in Data Structures, Algorithms, Time/Space Complexity (Big-O analysis), and memory performance. Provide concise, clear, accurate explanations tailored for CS students with markdown code snippets when applicable."
        val fullPrompt = if (!contextDSName.isNullOrEmpty()) {
            "Data Structure Context: $contextDSName\n\nStudent Question: $prompt"
        } else {
            prompt
        }

        try {
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val jsonBody = JSONObject().apply {
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", systemPrompt)))
                })
                put("contents", JSONArray().put(JSONObject().apply {
                    put("parts", JSONArray().put(JSONObject().put("text", fullPrompt)))
                }))
            }

            val requestBody = jsonBody.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    val errText = response.body?.string() ?: ""
                    return@withContext "API Request Failed (Code ${response.code}): ${response.message}"
                }

                val responseBodyStr = response.body?.string() ?: ""
                val jsonResponse = JSONObject(responseBodyStr)
                val candidates = jsonResponse.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val candidate = candidates.getJSONObject(0)
                    val content = candidate.optJSONObject("content")
                    val parts = content?.optJSONArray("parts")
                    if (parts != null && parts.length() > 0) {
                        return@withContext parts.getJSONObject(0).optString("text", "No answer returned.")
                    }
                }
                return@withContext "No response content returned from Gemini."
            }
        } catch (e: Exception) {
            return@withContext "Error connecting to AI Tutor: ${e.localizedMessage ?: e.message}"
        }
    }
}
