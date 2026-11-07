package uz.learningtracker.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import kotlinx.coroutines.launch
import uz.learningtracker.AppContainer
import uz.learningtracker.R
import uz.learningtracker.domain.ThemeMode
import uz.learningtracker.ui.components.AppBottomNavBar
import uz.learningtracker.ui.components.AppTopBar
import uz.learningtracker.ui.components.ThemeBottomSheet
import uz.learningtracker.ui.entry.BugungiYozuvScreen
import uz.learningtracker.ui.entry.EntryViewModel
import uz.learningtracker.ui.screens.DashboardScreen
import uz.learningtracker.ui.screens.HisobotScreen
import uz.learningtracker.ui.screens.TarixScreen
import java.time.LocalDate

@Composable
fun AppNavHost(
    container: AppContainer,
    themeMode: ThemeMode,
    onThemeChange: (ThemeMode) -> Unit,
) {
    val navController = rememberNavController()
    var showThemeSheet by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    val screenTitle = when {
        currentRoute?.startsWith(AppScreen.Bugun.BASE) == true -> stringResource(R.string.screen_bugun)
        currentRoute == AppScreen.Dashboard.route -> stringResource(R.string.screen_dashboard)
        currentRoute == AppScreen.Tarix.route -> stringResource(R.string.screen_tarix)
        currentRoute == AppScreen.Hisobot.route -> stringResource(R.string.screen_hisobot)
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
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { innerPadding ->
        val savedMsg = stringResource(R.string.entry_saved_success)

        NavHost(
            navController = navController,
            startDestination = AppScreen.Bugun.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(
                route = AppScreen.Bugun.route,
                arguments = listOf(
                    navArgument("date") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                ),
            ) { backStack ->
                val dateArg = backStack.arguments?.getString("date")
                val initialDate = dateArg?.let { runCatching { LocalDate.parse(it) }.getOrNull() }
                    ?: LocalDate.now()
                val fromTarix = dateArg != null

                val hoursRequired = stringResource(R.string.error_hours_required)
                val hoursInvalid = stringResource(R.string.error_hours_invalid)
                val tasksRequired = stringResource(R.string.error_tasks_required)
                val tasksInvalid = stringResource(R.string.error_tasks_invalid)
                val topicRequired = stringResource(R.string.error_topic_required)
                val vm: EntryViewModel = viewModel(
                    factory = EntryViewModel.Factory(
                        saveEntry = container.saveEntry,
                        getEntryByDate = container.getEntryByDate,
                        initialDate = initialDate,
                        hoursRequiredError = hoursRequired,
                        hoursInvalidError = hoursInvalid,
                        tasksRequiredError = tasksRequired,
                        tasksInvalidError = tasksInvalid,
                        topicRequiredError = topicRequired,
                    ),
                )
                BugungiYozuvScreen(
                    viewModel = vm,
                    onSaveSuccess = {
                        scope.launch { snackbarHostState.showSnackbar(savedMsg) }
                        if (fromTarix) navController.popBackStack()
                    },
                    onCancelEdit = if (fromTarix) ({ navController.popBackStack() }) else null,
                )
            }
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
