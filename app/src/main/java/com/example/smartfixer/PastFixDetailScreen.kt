package com.example.smartfixer

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.smartfixer.data.DiagnosisResult
import com.example.smartfixer.data.local.PastFixEntity
import kotlinx.serialization.json.Json

@Composable
fun PastFixDetailScreen(
    fix: PastFixEntity?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (fix == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(modifier = Modifier.size(48.dp))
        }
        return
    }

    val tools: List<String> = try {
        Json.decodeFromString(fix.toolsJson)
    } catch (_: Exception) {
        emptyList()
    }

    val steps: List<String> = try {
        Json.decodeFromString(fix.stepsJson)
    } catch (_: Exception) {
        emptyList()
    }

    val result = DiagnosisResult(
        title = fix.title,
        difficulty = fix.difficulty,
        estimatedTime = fix.estimatedTime,
        tools = tools,
        steps = steps,
        category = fix.category
    )

    ResultsScreen(
        result = result,
        onContinueDebugging = onBack,
        onDone = onBack,
        modifier = modifier
    )
}
