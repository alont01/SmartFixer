package com.example.smartfixer

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.ElectricalServices
import androidx.compose.material.icons.filled.LocalPhone
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Plumbing
import androidx.compose.material.icons.filled.Roofing
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class ProCategory(
    val label: String,
    val icon: ImageVector,
    val searchQuery: String,
    val categoryKey: String
)

private val proCategories = listOf(
    ProCategory("Plumber", Icons.Default.Plumbing, "plumbers near me", "plumbing"),
    ProCategory("Electrician", Icons.Default.ElectricalServices, "electricians near me", "electrical"),
    ProCategory("HVAC", Icons.Default.Thermostat, "hvac repair near me", "hvac"),
    ProCategory("Roofer", Icons.Default.Roofing, "roofers near me", "roofing"),
    ProCategory("Handyman", Icons.Default.Build, "handyman near me", "general"),
    ProCategory("Locksmith", Icons.Default.Lock, "locksmith near me", "locksmith")
)

@Composable
fun ContactProScreen(
    lastDiagnosisCategory: String? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(Modifier.height(16.dp))

        Text(
            text = "Find a Professional",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "Tap a category to search Google Maps for nearby professionals",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(Modifier.height(20.dp))

        proCategories.forEach { category ->
            val isRecommended = lastDiagnosisCategory != null &&
                    category.categoryKey == lastDiagnosisCategory

            Card(
                onClick = {
                    val geoUri = Uri.parse("geo:0,0?q=${Uri.encode(category.searchQuery)}")
                    val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                    context.startActivity(mapIntent)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = if (isRecommended) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.label,
                        tint = if (isRecommended) MaterialTheme.colorScheme.primary
                               else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = category.label,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        if (isRecommended) {
                            Text(
                                text = "Recommended based on your diagnosis",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
        }

        Spacer(Modifier.height(8.dp))

        // Emergency card
        Card(
            onClick = {
                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:911"))
                context.startActivity(dialIntent)
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Emergency",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(32.dp)
                )
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Emergency",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = "Call emergency services",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
                Icon(
                    imageVector = Icons.Default.LocalPhone,
                    contentDescription = "Call",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}
