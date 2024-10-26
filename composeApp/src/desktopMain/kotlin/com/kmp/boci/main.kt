package com.kmp.boci

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import initialization.ApplicationComponent
import localDatabase.getDao


fun main() = application {
    ApplicationComponent.init(getDao())
    Window(
        onCloseRequest = ::exitApplication,
        title = "Boci",
    ) {
        App()
    }
}