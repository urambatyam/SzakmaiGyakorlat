package screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import initialization.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.swing.JFileChooser
import javax.swing.SwingUtilities


class FolderPathScreen : Screen{

    private val thread =  CoroutineScope(Dispatchers.Main)

    @Composable
    override  fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var isTherePath by remember { mutableStateOf(false) }
        var folderPath by remember { mutableStateOf("") }
        LaunchedEffect(Unit){
            folderPath = ApplicationComponent.coreComponent.appPreferences.getFolderPath() ?: ""
            if(folderPath.isNotEmpty()){
                isTherePath = true
            }
        }
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            if (!isTherePath){
                Text("A kezdéshez válasza ki az adatokat tartalmazó mappát!")
            }else{
                Text("Mappa megváltoztatása.")
            }
            Button(
                onClick = {
                    chooseFolder { selectedPath ->
                        folderPath = selectedPath
                        thread.launch{
                            ApplicationComponent.coreComponent.appPreferences.setFolderPath(selectedPath)
                        }
                    }
                }
            ){
                Text(text = "Tallózás")
            }
            if (isTherePath){
                Text("A kiválaszott mappa: $folderPath")
            }
            Button(
                onClick = {
                    if (isTherePath){
                        navigator.push(
                            HomeScreen(navigator)
                        )
                    }
                }
            ){
                Text(text = "Kezdés")
            }
        }


    }


    private fun chooseFolder(onFolderSelected: (String) -> Unit) {
        SwingUtilities.invokeLater {
            val fileChooser = JFileChooser().apply {
                fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
                dialogTitle = "Válasszon mappát"
            }

            val result = fileChooser.showOpenDialog(null)
            if (result == JFileChooser.APPROVE_OPTION) {
                val selectedFolder = fileChooser.selectedFile
                onFolderSelected(selectedFolder.absolutePath)
            }
        }
    }
}