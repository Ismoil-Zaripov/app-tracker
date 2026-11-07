package uz.learningtracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uz.learningtracker.R
import uz.learningtracker.domain.ThemeMode
import uz.learningtracker.ui.components.AppBottomNavBar
import uz.learningtracker.ui.components.AppTopBar
import uz.learningtracker.ui.components.ThemeBottomSheet
import uz.learningtracker.ui.screens.BugungiYozuvScreen
import uz.learningtracker.ui.screens.DashboardScreen
import uz.learningtracker.ui.screens.HisobotScreen
import uz.learningtracker.ui.screens.TarixScreen

@Composable
fun AppNavHost(
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    val navController = rememberNavController()
    var showThemeSheet by remember { mutableStateOf(false) }

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val screenTitle = when (currentRoute) {
        AppScreen.Bugun.route -> stringResource(R.string.screen_bugun)
        AppScreen.Dashboard.route -> stringResource(R.string.screen_dashboard)
        AppScreen.Tarix.route -> stringResource(R.string.screen_tarix)
        AppScreen.Hisobot.route -> stringResource(R.string.screen_hisobot)
        else -> stringResource(R.string.app_name)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = screenTitle,
                onThemeClick = { showThemeSheet = true },
            )
        },
        bottomBar = {
            AppBottomNavBar(navController = navController)
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreen.Bugun.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(AppScreen.Bugun.route) { BugungiYozuvScreen() }
            composable(AppScreen.Dashboard.route) { DashboardScreen() }
            composable(AppScreen.Tarix.route) { TarixScreen() }
            composable(AppScreen.Hisobot.route) { HisobotScreen() }
        }
    }

    if (showThemeSheet) {
        ThemeBottomSheet(
            currentMode = themeMode,
            onThemeSelect = { mode ->
                onThemeChange(mode)
                showThemeSheet = false
            },
            onDismiss = { showThemeSheet = false },
        )
    }
}
