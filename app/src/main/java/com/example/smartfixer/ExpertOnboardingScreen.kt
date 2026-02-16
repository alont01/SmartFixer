package com.example.smartfixer

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.smartfixer.data.local.ExpertEntity

private val categoryOptions = listOf(
    "plumbing" to "Plumbing",
    "electrical" to "Electrical",
    "hvac" to "HVAC",
    "roofing" to "Roofing",
    "general" to "General / Handyman",
    "locksmith" to "Locksmith"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpertOnboardingScreen(
    onRegister: (ExpertEntity) -> Unit,
    onBack: () -> Unit,
    uiState: ExpertUiState,
    onResetState: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf(categoryOptions[0]) }
    var categoryExpanded by remember { mutableStateOf(false) }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var hourlyRate by remember { mutableStateOf("") }
    var availability by remember { mutableStateOf("") }
    var serviceArea by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var yearsExperience by remember { mutableStateOf("") }
    var certifications by remember { mutableStateOf("") }

    var nameError by remember { mutableStateOf(false) }
    var phoneError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState) {
        when (uiState) {
            is ExpertUiState.Success -> {
                snackbarHostState.showSnackbar("Registration successful!")
                onResetState()
                onBack()
            }
            is ExpertUiState.Error -> {
                snackbarHostState.showSnackbar("Error: ${uiState.message}")
                onResetState()
            }
            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Register as Expert") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(8.dp))

            // Basic Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Basic Information",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it; nameError = false },
                        label = { Text("Business / Full Name") },
                        isError = nameError,
                        supportingText = if (nameError) {{ Text("Name is required") }} else null,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    ExposedDropdownMenuBox(
                        expanded = categoryExpanded,
                        onExpandedChange = { categoryExpanded = !categoryExpanded }
                    ) {
                        OutlinedTextField(
                            value = selectedCategory.second,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Specialty") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryExpanded) },
                            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
                        )
                        ExposedDropdownMenu(
                            expanded = categoryExpanded,
                            onDismissRequest = { categoryExpanded = false }
                        ) {
                            categoryOptions.forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.second) },
                                    onClick = {
                                        selectedCategory = option
                                        categoryExpanded = false
                                    }
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; phoneError = false },
                        label = { Text("Phone Number") },
                        isError = phoneError,
                        supportingText = if (phoneError) {{ Text("Phone is required") }} else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; emailError = false },
                        label = { Text("Email Address") },
                        isError = emailError,
                        supportingText = if (emailError) {{ Text("Email is required") }} else null,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Service Details Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Service Details",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = hourlyRate,
                        onValueChange = { hourlyRate = it },
                        label = { Text("Hourly Rate (\$)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = availability,
                        onValueChange = { availability = it },
                        label = { Text("Availability (e.g., Mon-Fri 8AM-5PM)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = serviceArea,
                        onValueChange = { serviceArea = it },
                        label = { Text("Service Area") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description of Services") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 3,
                        maxLines = 5
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // Experience Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Experience",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = yearsExperience,
                        onValueChange = { yearsExperience = it },
                        label = { Text("Years of Experience") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                    Spacer(Modifier.height(8.dp))

                    OutlinedTextField(
                        value = certifications,
                        onValueChange = { certifications = it },
                        label = { Text("Certifications (comma-separated)") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        maxLines = 3
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    nameError = name.isBlank()
                    phoneError = phone.isBlank()
                    emailError = email.isBlank()

                    if (!nameError && !phoneError && !emailError) {
                        onRegister(
                            ExpertEntity(
                                name = name.trim(),
                                category = selectedCategory.first,
                                phone = phone.trim(),
                                email = email.trim(),
                                hourlyRate = hourlyRate.toDoubleOrNull() ?: 0.0,
                                description = description.trim(),
                                availability = availability.trim(),
                                rating = 5.0f,
                                yearsExperience = yearsExperience.toIntOrNull() ?: 0,
                                certifications = certifications.trim(),
                                serviceArea = serviceArea.trim()
                            )
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState !is ExpertUiState.Saving
            ) {
                if (uiState is ExpertUiState.Saving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(Modifier.width(8.dp))
                }
                Text("Register")
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
