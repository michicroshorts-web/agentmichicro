package com.example.minimalphone

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.AlarmClock
import android.provider.MediaStore
import android.provider.Settings
import com.example.minimalphone.model.AppInfo

/**
 * Reads the installed apps and stores which ones are allowed on the minimal home screen.
 * The whitelist is persisted in SharedPreferences — no extra dependencies needed.
 */
class AppRepository(context: Context) {

    private val appContext = context.applicationContext
    private val pm: PackageManager = appContext.packageManager
    private val prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    /** Every launchable app on the device, minus this launcher itself, sorted by label. */
    fun loadAllApps(): List<AppInfo> {
        val intent = Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER)
        return pm.queryIntentActivities(intent, 0)
            .map {
                AppInfo(
                    label = it.loadLabel(pm).toString(),
                    packageName = it.activityInfo.packageName,
                )
            }
            .filter { it.packageName != appContext.packageName }
            .distinctBy { it.packageName }
            .sortedBy { it.label.lowercase() }
    }

    /** Allowed package names. On first run this is seeded with the essential system apps. */
    fun getWhitelist(): Set<String> {
        if (!prefs.contains(KEY_WHITELIST)) {
            val defaults = defaultEssentials()
            saveWhitelist(defaults)
            return defaults
        }
        return prefs.getStringSet(KEY_WHITELIST, emptySet()) ?: emptySet()
    }

    fun setAllowed(packageName: String, allowed: Boolean) {
        val current = getWhitelist().toMutableSet()
        if (allowed) current.add(packageName) else current.remove(packageName)
        saveWhitelist(current)
    }

    fun launchIntentFor(packageName: String): Intent? =
        pm.getLaunchIntentForPackage(packageName)

    private fun saveWhitelist(packages: Set<String>) {
        prefs.edit().putStringSet(KEY_WHITELIST, packages).apply()
    }

    /** Resolve the default "essential" apps: dialer, messaging, camera, clock, settings, contacts. */
    private fun defaultEssentials(): Set<String> {
        val intents = listOf(
            Intent(Intent.ACTION_DIAL),
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_MESSAGING),
            Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA),
            Intent(AlarmClock.ACTION_SHOW_ALARMS),
            Intent(Settings.ACTION_SETTINGS),
            Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_CONTACTS),
        )
        return intents
            .mapNotNull { pm.resolveActivity(it, 0)?.activityInfo?.packageName }
            .toSet()
    }

    companion object {
        private const val PREFS = "minimal_phone_prefs"
        private const val KEY_WHITELIST = "whitelist"
    }
}
