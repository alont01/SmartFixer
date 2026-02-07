package com.example.smartfixer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartfixer.ui.theme.SmartFixerTheme

data class PastFix(
    val title: String,
    val date: String,
    val status: String
)

private val mockPastFixes = listOf(
    PastFix("Leaky Kitchen Faucet", "Jan 15, 2026", "Completed"),
    PastFix("Squeaky Door Hinge", "Jan 8, 2026", "Completed"),
    PastFix("Running Toilet", "Dec 28, 2025", "Completed"),
    PastFix("Cracked Drywall Patch", "Dec 15, 2025", "In Progress"),
    PastFix("Loose Cabinet Handle", "Dec 1, 2025", "Completed"),
    PastFix("Clogged Bathroom Drain", "Nov 20, 2025", "Completed")
)

@Composable
fun PastFixesScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Past Fixes",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(mockPastFixes) { fix ->
                PastFixCard(fix)
            }
            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun PastFixCard(fix: PastFix) {
    val isCompleted = fix.status == "Completed"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isCompleted) Icons.Default.CheckCircle else Icons.Default.Schedule,
                contentDescription = fix.status,
                tint = if (isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = fix.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = fix.date,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            AssistChip(
                onClick = {},
                label = {
                    Text(
                        fix.status,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PastFixesScreenPreview() {
    SmartFixerTheme {
        PastFixesScreen()
    }
}
