package uz.learningtracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import uz.learningtracker.domain.ThemeMode
import uz.learningtracker.ui.navigation.AppNavHost
import uz.learningtracker.ui.theme.LearningTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as LearningTrackerApp).container
        enableEdgeToEdge()
        setContent {
            val themeMode by container.themeRepo.themeMode.collectAsState(ThemeMode.SYSTEM)
            val systemDark = isSystemInDarkTheme()
            val isDark = when (themeMode) {
                ThemeMode.SYSTEM -> systemDark
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
            }
            LearningTrackerTheme(darkTheme = isDark) {
                AppNavHost(
                    container = container,
                    themeMode = themeMode,
                    onThemeChange = { mode ->
                        lifecycleScope.launch { container.themeRepo.setThemeMode(mode) }
                    },
                )
            }
        }
    }
}
