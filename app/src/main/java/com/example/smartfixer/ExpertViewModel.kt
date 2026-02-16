package com.example.smartfixer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartfixer.data.local.AppDatabase
import com.example.smartfixer.data.local.ExpertEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class ExpertUiState {
    object Idle : ExpertUiState()
    object Saving : ExpertUiState()
    object Success : ExpertUiState()
    data class Error(val message: String) : ExpertUiState()
}

class ExpertViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).expertDao()

    val allExperts: Flow<List<ExpertEntity>> = dao.getAllExperts()

    private val _uiState = MutableStateFlow<ExpertUiState>(ExpertUiState.Idle)
    val uiState: StateFlow<ExpertUiState> = _uiState

    fun getExpertsByCategory(category: String): Flow<List<ExpertEntity>> {
        return dao.getExpertsByCategory(category)
    }

    fun insertExpert(expert: ExpertEntity) {
        viewModelScope.launch {
            _uiState.value = ExpertUiState.Saving
            try {
                dao.insertExpert(expert)
                _uiState.value = ExpertUiState.Success
            } catch (e: Exception) {
                _uiState.value = ExpertUiState.Error(e.message ?: "Failed to save expert")
            }
        }
    }

    fun resetState() {
        _uiState.value = ExpertUiState.Idle
    }

    fun seedDummyExperts() {
        viewModelScope.launch {
            if (dao.getExpertCount() > 0) return@launch
            dummyExperts.forEach { dao.insertExpert(it) }
        }
    }
}

private val dummyExperts = listOf(
    ExpertEntity(
        name = "Mike's Plumbing Services",
        category = "plumbing",
        phone = "(555) 101-2001",
        email = "mike@mikesplumbing.com",
        hourlyRate = 85.0,
        description = "Full-service residential plumbing. Specializing in leak detection, pipe repair, and fixture installation.",
        availability = "Mon-Sat 7AM-6PM",
        rating = 4.8f,
        yearsExperience = 15,
        certifications = "Licensed Master Plumber, EPA Lead-Safe Certified",
        serviceArea = "Metro area, 30-mile radius"
    ),
    ExpertEntity(
        name = "Rodriguez Plumbing Co.",
        category = "plumbing",
        phone = "(555) 101-2002",
        email = "info@rodriguezplumbing.com",
        hourlyRate = 75.0,
        description = "Family-owned plumbing business. Emergency services available 24/7 for burst pipes and sewage backups.",
        availability = "24/7 Emergency Available",
        rating = 4.6f,
        yearsExperience = 22,
        certifications = "Licensed Plumber, Backflow Prevention Certified",
        serviceArea = "City and suburbs"
    ),
    ExpertEntity(
        name = "BrightSpark Electric",
        category = "electrical",
        phone = "(555) 201-3001",
        email = "service@brightspark.com",
        hourlyRate = 95.0,
        description = "Commercial and residential electrical work. Panel upgrades, EV charger installation, and smart home wiring.",
        availability = "Mon-Fri 8AM-5PM",
        rating = 4.9f,
        yearsExperience = 12,
        certifications = "Licensed Electrician, NFPA 70E Certified",
        serviceArea = "Tri-county area"
    ),
    ExpertEntity(
        name = "Sarah Chen Electric",
        category = "electrical",
        phone = "(555) 201-3002",
        email = "sarah@chenelectric.com",
        hourlyRate = 90.0,
        description = "Specializing in residential rewiring, code compliance inspections, and lighting design.",
        availability = "Mon-Sat 8AM-6PM",
        rating = 4.7f,
        yearsExperience = 10,
        certifications = "Master Electrician, OSHA 30 Certified",
        serviceArea = "Metro area"
    ),
    ExpertEntity(
        name = "Cool Breeze HVAC",
        category = "hvac",
        phone = "(555) 301-4001",
        email = "info@coolbreezehvac.com",
        hourlyRate = 100.0,
        description = "Complete HVAC solutions including installation, maintenance, and repair of heating and cooling systems.",
        availability = "Mon-Fri 7AM-7PM, Sat 8AM-2PM",
        rating = 4.8f,
        yearsExperience = 18,
        certifications = "NATE Certified, EPA 608 Universal",
        serviceArea = "50-mile radius"
    ),
    ExpertEntity(
        name = "Texas Climate Control",
        category = "hvac",
        phone = "(555) 301-4002",
        email = "support@texasclimate.com",
        hourlyRate = 110.0,
        description = "High-efficiency system installations and ductwork. Free estimates on new system installs.",
        availability = "Mon-Sat 7AM-8PM",
        rating = 4.5f,
        yearsExperience = 25,
        certifications = "NATE Certified, ACCA Member",
        serviceArea = "Greater metro area"
    ),
    ExpertEntity(
        name = "Summit Roofing Pros",
        category = "roofing",
        phone = "(555) 401-5001",
        email = "jobs@summitroofing.com",
        hourlyRate = 80.0,
        description = "Roof repair, replacement, and inspection. Experienced with shingle, tile, and metal roofing.",
        availability = "Mon-Fri 7AM-5PM",
        rating = 4.7f,
        yearsExperience = 20,
        certifications = "GAF Certified Installer, OSHA 10",
        serviceArea = "City and surrounding counties"
    ),
    ExpertEntity(
        name = "All Weather Roofing",
        category = "roofing",
        phone = "(555) 401-5002",
        email = "info@allweatherroof.com",
        hourlyRate = 85.0,
        description = "Storm damage specialists. Insurance claim assistance and emergency tarping services.",
        availability = "Mon-Sat 6AM-6PM",
        rating = 4.6f,
        yearsExperience = 14,
        certifications = "CertainTeed SELECT ShingleMaster, Licensed Roofer",
        serviceArea = "Regional, 40-mile radius"
    ),
    ExpertEntity(
        name = "Jack of All Trades Handyman",
        category = "general",
        phone = "(555) 501-6001",
        email = "jack@jackalltrades.com",
        hourlyRate = 55.0,
        description = "General home repairs, furniture assembly, drywall patching, painting, and minor plumbing/electrical.",
        availability = "Mon-Sun 8AM-6PM",
        rating = 4.5f,
        yearsExperience = 8,
        certifications = "Insured and Bonded",
        serviceArea = "Local metro area"
    ),
    ExpertEntity(
        name = "HomeHelper Services",
        category = "general",
        phone = "(555) 501-6002",
        email = "hello@homehelper.com",
        hourlyRate = 60.0,
        description = "Reliable handyman services for all your home needs. Specializing in kitchen and bathroom updates.",
        availability = "Mon-Fri 9AM-5PM",
        rating = 4.4f,
        yearsExperience = 6,
        certifications = "Insured, BBB A+ Rated",
        serviceArea = "City limits"
    ),
    ExpertEntity(
        name = "QuickKey Locksmith",
        category = "locksmith",
        phone = "(555) 601-7001",
        email = "dispatch@quickkey.com",
        hourlyRate = 70.0,
        description = "Residential and automotive locksmith. Lock rekeying, smart lock installation, and emergency lockout service.",
        availability = "24/7",
        rating = 4.6f,
        yearsExperience = 11,
        certifications = "ALOA Certified, Licensed Locksmith",
        serviceArea = "Metro area, mobile service"
    ),
    ExpertEntity(
        name = "SecureHome Locksmiths",
        category = "locksmith",
        phone = "(555) 601-7002",
        email = "info@securehome.com",
        hourlyRate = 75.0,
        description = "Home security specialists. Lock upgrades, access control systems, and safe installation.",
        availability = "Mon-Sat 8AM-8PM, Emergency 24/7",
        rating = 4.7f,
        yearsExperience = 16,
        certifications = "CML Certified Master Locksmith",
        serviceArea = "Tri-city area"
    )
)
