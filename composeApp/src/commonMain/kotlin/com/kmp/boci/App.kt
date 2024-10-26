package com.kmp.boci

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import screens.FolderPathScreen


@Composable
@Preview
fun App() {
    MaterialTheme {
        Navigator(FolderPathScreen()){ navigator ->
            SlideTransition(navigator)
        }
    }
}

/*import org.slf4j.LoggerFactory
val logger = LoggerFactory.getLogger("MyLogger")
logger.info("Application started")
logger.warn("This is a warning message")
logger.error("This is an error message")*/