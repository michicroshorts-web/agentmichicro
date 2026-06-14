package com.example.minimalphone

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.minimalphone.ui.HomeScreen
import com.example.minimalphone.ui.SettingsScreen

class MainActivity : ComponentActivity() {

    private val viewModel: LauncherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MinimalPhoneApp(viewModel) }
    }

    override fun onResume() {
        super.onResume()
        // Pick up newly installed / removed apps when returning to the home screen.
        viewModel.refresh()
    }
}

private enum class Screen { Home, Settings }

@Composable
private fun MinimalPhoneApp(viewModel: LauncherViewModel) {
    MaterialTheme(colorScheme = darkColorScheme()) {
        Surface(modifier = Modifier.fillMaxSize(), color = Color.Black) {
            var screen by remember { mutableStateOf(Screen.Home) }
            val allApps by viewModel.allApps.collectAsState()
            val whitelist by viewModel.whitelist.collectAsState()
            val context = LocalContext.current

            when (screen) {
                Screen.Home -> HomeScreen(
                    apps = allApps.filter { whitelist.contains(it.packageName) },
                    onLaunch = { pkg ->
                        viewModel.launchIntentFor(pkg)?.let { context.startActivity(it) }
                    },
                    onOpenSettings = { screen = Screen.Settings },
                )

                Screen.Settings -> SettingsScreen(
                    apps = allApps,
                    whitelist = whitelist,
                    onToggle = viewModel::toggle,
                    onBack = { screen = Screen.Home },
                )
            }
        }
    }
}
