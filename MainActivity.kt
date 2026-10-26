package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.ui.AppScreen
import com.example.ui.DashboardTab
import com.example.ui.ZuolingoViewModel
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.QuizScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.theme.ZuolingoTheme

class MainActivity : ComponentActivity() {

    private val viewModel: ZuolingoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val userProfile by viewModel.userProfile.collectAsState()
            val currentScreen by viewModel.currentScreen.collectAsState()

            val customColor = Color(userProfile.customColorHex.toULong())

            ZuolingoTheme(
                themeMode = userProfile.themeMode,
                customColor = customColor
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Crossfade(targetState = currentScreen, label = "screen_crossfade") { screen ->
                        when (screen) {
                            is AppScreen.Splash -> {
                                SplashScreen(
                                    onSplashFinished = {
                                        viewModel.navigateToDashboard(DashboardTab.HOME)
                                    }
                                )
                            }

                            is AppScreen.Dashboard -> {
                                DashboardScreen(
                                    viewModel = viewModel,
                                    onStartQuiz = { levelNumber ->
                                        viewModel.startQuiz(levelNumber)
                                    }
                                )
                            }

                            is AppScreen.Quiz -> {
                                BackHandler {
                                    viewModel.exitQuiz()
                                }
                                QuizScreen(
                                    viewModel = viewModel,
                                    onExitQuiz = {
                                        viewModel.exitQuiz()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
