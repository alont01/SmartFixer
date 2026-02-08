package com.example.smartfixer

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.smartfixer.data.local.AppDatabase
import com.example.smartfixer.data.local.PastFixEntity
import kotlinx.coroutines.flow.Flow

class PastFixesViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = AppDatabase.getInstance(application).pastFixDao()

    val pastFixes: Flow<List<PastFixEntity>> = dao.getAllFixes()

    suspend fun getFixById(id: Long): PastFixEntity? = dao.getFixById(id)
}
