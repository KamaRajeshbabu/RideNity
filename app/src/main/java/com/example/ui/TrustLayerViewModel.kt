package com.example.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.api.*
import com.example.BuildConfig
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64

class TrustLayerViewModel : ViewModel() {
    var trustScoreAnalysis by mutableStateOf<String?>(null)
    var isAnalyzing by mutableStateOf(false)
    var generatedImage by mutableStateOf<Bitmap?>(null)
    var isGeneratingImage by mutableStateOf(false)

    fun evaluateIncident(driverNotes: String, speedLogs: String) {
        isAnalyzing = true
        trustScoreAnalysis = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val prompt = """
                    You are the RideNity TrustLayer Shared Accountability Engine.
                    Analyze this overspeeding incident using high thinking mode.
                    Identify whether the driver, passenger, or both are liable.
                    Speed logs: $speedLogs
                    Driver notes: $driverNotes
                    
                    Provide a concise report with: 
                    1. Determination
                    2. Risk Score (0-100)
                    3. Action
                """.trimIndent()

                val request = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                    generationConfig = GenerationConfig(
                        thinkingConfig = ThinkingConfig(thinkingLevel = "high")
                    )
                )

                val response = RetrofitClient.service.generateContentHighThinking(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = request
                )
                
                val text = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                trustScoreAnalysis = text ?: "Evaluation failed. No answer."
            } catch (e: Exception) {
                trustScoreAnalysis = "Error navigating incident: ${e.message}"
            } finally {
                isAnalyzing = false
            }
        }
    }

    fun generateVehicleAvatar(prompt: String, size: String) {
        isGeneratingImage = true
        generatedImage = null
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val request = GenerateContentRequest(
                    contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                    generationConfig = GenerationConfig(
                        imageConfig = ImageConfig(aspectRatio = "1:1", imageSize = size),
                        responseModalities = listOf("TEXT", "IMAGE")
                    )
                )
                
                val response = RetrofitClient.service.generateImage(
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    request = request
                )
                
                val imagePart = response.candidates.firstOrNull()?.content?.parts?.find { it.inlineData != null }
                val base64Data = imagePart?.inlineData?.data
                if (base64Data != null) {
                    val decodedBytes = Base64.decode(base64Data, Base64.DEFAULT)
                    generatedImage = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isGeneratingImage = false
            }
        }
    }
}
