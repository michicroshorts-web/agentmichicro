package com.example.minimalphone

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.minimalphone.model.AppInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LauncherViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = AppRepository(app)

    private val _allApps = MutableStateFlow<List<AppInfo>>(emptyList())
    val allApps: StateFlow<List<AppInfo>> = _allApps.asStateFlow()

    private val _whitelist = MutableStateFlow<Set<String>>(emptySet())
    val whitelist: StateFlow<Set<String>> = _whitelist.asStateFlow()

    init {
        refresh()
    }

    /** Reload installed apps + whitelist. Called on resume so newly installed apps show up. */
    fun refresh() {
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO) { repo.loadAllApps() }
            val wl = withContext(Dispatchers.IO) { repo.getWhitelist() }
            _allApps.value = apps
            _whitelist.value = wl
        }
    }

    fun toggle(packageName: String, allowed: Boolean) {
        repo.setAllowed(packageName, allowed)
        _whitelist.value = repo.getWhitelist()
    }

    fun launchIntentFor(packageName: String) = repo.launchIntentFor(packageName)
}
