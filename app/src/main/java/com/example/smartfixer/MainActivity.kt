package com.example.smartfixer

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.smartfixer.data.local.PastFixEntity
import com.example.smartfixer.navigation.Screen
import com.example.smartfixer.ui.theme.SmartFixerTheme
import com.example.smartfixer.util.ImageUtil
import java.io.File

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val profileViewModel: ProfileViewModel = viewModel()
            val profile by profileViewModel.profile.collectAsState()

            SmartFixerTheme(darkTheme = profile.darkModeEnabled) {
                SmartFixerApp(profileViewModel = profileViewModel)
            }
        }
    }
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: @Composable () -> Unit
)

@Composable
fun SmartFixerApp(profileViewModel: ProfileViewModel) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val diagnosisViewModel: DiagnosisViewModel = viewModel()
    val pastFixesViewModel: PastFixesViewModel = viewModel()
    val diagnosisState by diagnosisViewModel.uiState.collectAsState()
    val selectedImageUri by diagnosisViewModel.selectedImageUri.collectAsState()
    val lastCategory by diagnosisViewModel.lastCategory.collectAsState()
    val profile by profileViewModel.profile.collectAsState()

    // Camera temp URI
    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let { diagnosisViewModel.setSelectedImageUri(it) }
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { diagnosisViewModel.setSelectedImageUri(it) }
    }

    val videoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { diagnosisViewModel.setSelectedImageUri(it) }
    }

    val bottomNavItems = listOf(
        BottomNavItem(Screen.Home, "Home") { Icon(Icons.Default.Home, contentDescription = "Home") },
        BottomNavItem(Screen.PastFixes, "Past Fixes") { Icon(Icons.Default.Build, contentDescription = "Past Fixes") },
        BottomNavItem(Screen.ContactPro, "Contact Pro") { Icon(Icons.Default.Phone, contentDescription = "Contact Pro") },
        BottomNavItem(Screen.Profile, "Profile") { Icon(Icons.Default.Person, contentDescription = "Profile") }
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.route in bottomNavItems.map { it.screen.route }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            icon = item.icon,
                            label = { Text(item.label) },
                            selected = currentDestination?.hierarchy?.any { it.route == item.screen.route } == true,
                            onClick = {
                                navController.navigate(item.screen.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    onDiagnose = { issueText ->
                        val imageUri = selectedImageUri
                        var base64: String? = null
                        var mediaType: String? = null
                        if (imageUri != null) {
                            base64 = ImageUtil.uriToBase64(context, imageUri)
                            mediaType = "image/jpeg"
                        }
                        diagnosisViewModel.diagnose(issueText, base64, mediaType)
                        navController.navigate(Screen.Results.route)
                    },
                    onCameraClick = {
                        val imagesDir = File(context.cacheDir, "images")
                        imagesDir.mkdirs()
                        val imageFile = File(imagesDir, "camera_${System.currentTimeMillis()}.jpg")
                        val uri = FileProvider.getUriForFile(
                            context,
                            "${context.packageName}.fileprovider",
                            imageFile
                        )
                        cameraImageUri.value = uri
                        cameraLauncher.launch(uri)
                    },
                    onGalleryClick = {
                        galleryLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onVideoClick = {
                        videoLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.VideoOnly)
                        )
                    },
                    selectedImageUri = selectedImageUri,
                    onRemoveImage = { diagnosisViewModel.clearSelectedImage() }
                )
            }
            composable(Screen.Results.route) {
                when (val state = diagnosisState) {
                    is DiagnosisUiState.Loading -> {
                        LoadingScreen()
                    }
                    is DiagnosisUiState.Success -> {
                        ResultsScreen(
                            result = state.result,
                            onContinueDebugging = { navController.popBackStack() },
                            onDone = {
                                diagnosisViewModel.resetState()
                                navController.popBackStack(Screen.Home.route, inclusive = false)
                            }
                        )
                    }
                    is DiagnosisUiState.Error -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(24.dp)
                            ) {
                                Text(
                                    text = "Error: ${state.message}",
                                    color = MaterialTheme.colorScheme.error,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(Modifier.height(24.dp))
                                Button(onClick = {
                                    diagnosisViewModel.resetState()
                                    navController.popBackStack(Screen.Home.route, inclusive = false)
                                }) {
                                    Text("Try Again")
                                }
                                Spacer(Modifier.height(12.dp))
                                OutlinedButton(onClick = {
                                    diagnosisViewModel.resetState()
                                    navController.popBackStack(Screen.Home.route, inclusive = false)
                                }) {
                                    Text("Go Back")
                                }
                            }
                        }
                    }
                    is DiagnosisUiState.Idle -> {
                        LaunchedEffect(Unit) {
                            navController.popBackStack()
                        }
                    }
                }
            }
            composable(Screen.Profile.route) {
                ProfileScreen(
                    profile = profile,
                    onSaveProfile = { profileViewModel.saveProfile(it) }
                )
            }
            composable(Screen.PastFixes.route) {
                PastFixesScreen(
                    pastFixes = pastFixesViewModel.pastFixes,
                    onFixClick = { fixId ->
                        navController.navigate(Screen.PastFixDetail.createRoute(fixId))
                    }
                )
            }
            composable(Screen.ContactPro.route) {
                ContactProScreen(lastDiagnosisCategory = lastCategory)
            }
            composable(
                route = Screen.PastFixDetail.route,
                arguments = listOf(navArgument("fixId") { type = NavType.LongType })
            ) { backStackEntry ->
                val fixId = backStackEntry.arguments?.getLong("fixId") ?: 0L
                val fix by produceState<PastFixEntity?>(initialValue = null, fixId) {
                    value = pastFixesViewModel.getFixById(fixId)
                }
                PastFixDetailScreen(
                    fix = fix,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
