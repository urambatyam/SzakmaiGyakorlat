package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import initialization.ApplicationComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import localDatabase.models.Data
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.time.LocalDate
import java.time.YearMonth


class HomeScreen(private val navigator: Navigator) : Screen {
    private val logger: Logger = LoggerFactory.getLogger("MyLogger")

    @Composable
    override fun Content() {
        var fileContents by remember { mutableStateOf<List<Data>>(emptyList()) }
        var isLoading by remember { mutableStateOf(true) }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        var folderPath by remember { mutableStateOf("") }

        var idCB by remember { mutableStateOf(true) }
        var datumCB by remember { mutableStateOf(true) }
        var timeCB by remember { mutableStateOf(true) }
        var thermometerCB by remember { mutableStateOf(true) }
        var temperatureCB by remember { mutableStateOf(true) }

        var startDate by remember { mutableStateOf("0") }
        var endDate by remember { mutableStateOf("999999999") }

        var expanded by remember { mutableStateOf(false) }
        var selectedThermometer by remember { mutableStateOf<String?>(null) }

        val thermometerIds by remember { mutableStateOf(mutableListOf<String>()) }
        var selects by remember { mutableStateOf(mutableListOf<String>()) }

        var showDialog by remember { mutableStateOf(false) }



        LaunchedEffect(Unit) {
            folderPath = ApplicationComponent.coreComponent.appPreferences.getFolderPath() ?: ""
            try {
                withContext(Dispatchers.IO) {
                    readFiles(folderPath)
                }
                logger.info("olvasás vége")
                fileContents = ApplicationComponent.coreComponent.localDatabase.getAll().first()
                thermometerIds.addAll(ApplicationComponent.coreComponent.localDatabase.getAllThermometerId().first())
                thermometerIds.add("Összes")
                selects.addAll(thermometerIds)
                isLoading = false
            } catch (e: Exception) {
                errorMessage = "Hiba történt a fájlok beolvasása során: ${e.message}"
                isLoading = false
            }
        }

        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.7f).padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().background(Color.Gray),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    if (idCB) Text("ID", modifier = Modifier.weight(1f))
                    if (datumCB) Text("Dátum", modifier = Modifier.weight(1f))
                    if (timeCB) Text("Idő", modifier = Modifier.weight(1f))
                    if (thermometerCB) Text("Hőmérő", modifier = Modifier.weight(1f))
                    if (temperatureCB) Text("Hőmérséglet", modifier = Modifier.weight(1f))
                }
                when {
                    isLoading -> Text("Betöltés...")
                    errorMessage != null -> Text(errorMessage!!)
                    else -> {
                        LazyColumn {
                            items(
                                fileContents
                                    .filter { it.date.toInt() in startDate.toInt()..endDate.toInt() }
                                    .filter { selects.contains(it.thermometerId) }
                            ) { line ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .background(Color.LightGray),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (idCB) Text(
                                        line.id.toString(),
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (datumCB) Text(line.date, modifier = Modifier.weight(1f))
                                    if (timeCB) Text(line.time, modifier = Modifier.weight(1f))
                                    if (thermometerCB) Text(
                                        line.thermometerId,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (temperatureCB) Text(
                                        line.temperature.toString(),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Column {

                Text("Szürések")
                Text("Megjelenités:")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(8.dp)
                ) {

                        Spacer(modifier = Modifier.height(16.dp))
                        Text("ID")
                        Checkbox(
                            checked = idCB,
                            onCheckedChange = { idCB = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Dátum")
                        Checkbox(
                            checked = datumCB,
                            onCheckedChange = { datumCB = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Idő")
                        Checkbox(
                            checked = timeCB,
                            onCheckedChange = { timeCB = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hőmérő")
                        Checkbox(
                            checked = thermometerCB,
                            onCheckedChange = { thermometerCB = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Hőmérséglet")
                        Checkbox(
                            checked = temperatureCB,
                            onCheckedChange = { temperatureCB = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                }
                Text("időIntervallum:")
                Button(onClick = { showDialog = true }) {
                    Text("Időintervallum választása")
                }

                DateRangePicker(
                    onDateRangeSelected = { start, end ->
                        startDate = start
                        endDate = end
                    },
                    onDismiss = { showDialog = false },
                    showDialog = showDialog
                )

                if (startDate.isNotEmpty() && endDate.isNotEmpty() && endDate != "999999999") {
                    Text("Választott intervallum: $startDate - $endDate")
                }



                Spacer(modifier = Modifier.height(8.dp))

                Text("Hőmérők")
                Row {
                    Text(
                        text = "Válaszd ki a hőmárőt Id alapján!",
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .clickable { expanded = true }
                            .padding(12.dp)
                            .border(1.dp, MaterialTheme.colors.onSurface, shape = MaterialTheme.shapes.small),
                        textAlign = TextAlign.Center
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .padding(12.dp)
                            .fillMaxWidth(0.2f)
                            .heightIn(max = 200.dp)
                    ) {
                        if (thermometerIds.isEmpty()) {
                            DropdownMenuItem(
                                onClick = { expanded = false }
                            ) {
                                Text("Nincsenek Hőmérők")
                            }
                        } else {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(max = 200.dp)
                                    .verticalScroll(rememberScrollState())
                            ) {
                                thermometerIds.forEach { id ->
                                    val isSelected = selects.contains(id)
                                    DropdownMenuItem(
                                        modifier = Modifier.background(
                                            if (isSelected) Color.LightGray else Color.White
                                        ),
                                        onClick = {
                                            if (id == "Összes") {
                                                selects = thermometerIds.toMutableList()
                                            } else {
                                                if (isSelected) {
                                                    selects = selects.filter { it != id }.toMutableList()
                                                } else {
                                                    if (selects.contains("Összes")) {
                                                        selects = mutableListOf(id)
                                                    } else {
                                                        selects = (selects + id).toMutableList()
                                                    }
                                                }
                                            }
                                        }
                                    ) {
                                        Row(
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(text = id)
                                            if (isSelected) {
                                                Text("✓", color = Color.Green)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


                Button(
                    onClick = {
                        // Group the data by thermometer ID
                        val thermometersData = fileContents
                            .filter { it.date.toInt() in startDate.toInt()..endDate.toInt() }
                            .filter { selects.contains(it.thermometerId) }
                            .groupBy { it.thermometerId }
                            .map { (id, data) ->
                                ThermometerData(
                                    id = id,
                                    times = data.map { "${it.date.substring(0,4)}-${it.date.substring(4,6)}-${it.date.substring(6,8)} ${it.time}" },
                                    temperatures = data.map { it.temperature }
                                )
                            }

                        navigator.push(ChartScreen(navigator, thermometersData))
                    }
                ) {
                    Text(text = "Mutasd Diagramban")
                }
                Button(
                    onClick = {
                        navigator.pop()
                    }
                ) {
                    Text(text = "Vissza")
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Kiválasztott mappa: $folderPath")

            }
        }
    }


    private suspend fun readFiles(folderPath: String) {
        val regex = Regex("\\d{8}\\.(temp|txt)")
        logger.info("nem müksik")
        val dates: List<String> = ApplicationComponent.coreComponent.localDatabase.getAllDates().first()
        logger.info("dates")
        dates.take(4).forEach { i ->
            logger.info("szam: $i")
        }
        File(folderPath).walkTopDown().filter { it.isFile }.filter { it.name.matches(regex) }
            .filterNot { file -> dates.toSet().contains(file.name.substringBefore('.')) }
            .forEach { file ->
                logger.info(file.name)
                try {
                    val oneFile = file.readLines()
                    oneFile.forEach {
                        val line = it.split(" ")
                        val d = file.name.substringBefore('.')
                        if (line[2] == "YES") {
                            ApplicationComponent.coreComponent.localDatabase.upsert(
                                Data(
                                    null,
                                    line[1],
                                    d,
                                    line[0],
                                    line[3].toInt()
                                )
                            )
                        }
                    }
                } catch (e: Exception) {
                    println("Hiba a fájl olvasása közben: ${file.name}, ${e.message}")
                }
            }
    }



    @Composable
    fun DateRangePicker(
        onDateRangeSelected: (String, String) -> Unit,
        onDismiss: () -> Unit,
        showDialog: Boolean
    ) {
        // Kezdő és végdátum állapotok
        val today = remember { LocalDate.now() }
        var startDate by remember { mutableStateOf<LocalDate?>(null) }
        var endDate by remember { mutableStateOf<LocalDate?>(null) }

        // Aktuális nézet állapotai
        var selectedYear by remember { mutableStateOf(today.year) }
        var selectedMonth by remember { mutableStateOf(today.monthValue) }
        var currentYearMonth by remember { mutableStateOf(YearMonth.from(today)) }

        // Kiválasztás állapota
        var isSelectingStart by remember { mutableStateOf(true) }

        if (showDialog) {
            Dialog(onDismissRequest = onDismiss) {
                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        // Kiválasztott intervallum megjelenítése
                        Text(
                            text = when {
                                startDate == null -> "Válaszd ki a kezdő dátumot"
                                endDate == null -> "Válaszd ki a végdátumot"
                                else -> "Intervallum: ${formatDate(startDate!!)} - ${formatDate(endDate!!)}"
                            },
                            style = MaterialTheme.typography.subtitle1,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Év és hónap választó sor
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Év választó
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = {
                                    selectedYear--
                                    currentYearMonth = YearMonth.of(selectedYear, selectedMonth)
                                }) {
                                    Text("←")
                                }
                                Text(
                                    text = selectedYear.toString(),
                                    style = MaterialTheme.typography.h6
                                )
                                IconButton(onClick = {
                                    selectedYear++
                                    currentYearMonth = YearMonth.of(selectedYear, selectedMonth)
                                }) {
                                    Text("→")
                                }
                            }

                            // Hónap választó
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(
                                    onClick = {
                                        if (selectedMonth > 1) selectedMonth--
                                        else {
                                            selectedMonth = 12
                                            selectedYear--
                                        }
                                        currentYearMonth = YearMonth.of(selectedYear, selectedMonth)
                                    }
                                ) {
                                    Text("←")
                                }
                                Text(
                                    text = getMonthName(selectedMonth),
                                    style = MaterialTheme.typography.h6
                                )
                                IconButton(
                                    onClick = {
                                        if (selectedMonth < 12) selectedMonth++
                                        else {
                                            selectedMonth = 1
                                            selectedYear++
                                        }
                                        currentYearMonth = YearMonth.of(selectedYear, selectedMonth)
                                    }
                                ) {
                                    Text("→")
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Nap nevek
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            listOf("H", "K", "Sz", "Cs", "P", "Sz", "V").forEach { day ->
                                Text(
                                    text = day,
                                    style = MaterialTheme.typography.body2,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Napok grid
                        val firstDayOfMonth = currentYearMonth.atDay(1).dayOfWeek.value
                        val daysInMonth = currentYearMonth.lengthOfMonth()

                        Column {
                            var dayCount = 1
                            for (week in 0 until 6) {
                                if (dayCount > daysInMonth) break

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    for (dayOfWeek in 1..7) {
                                        if ((week == 0 && dayOfWeek < firstDayOfMonth) || dayCount > daysInMonth) {
                                            Box(modifier = Modifier.height(40.dp).width(60.dp))
                                        } else {
                                            val currentDay = dayCount
                                            val currentDate = LocalDate.of(selectedYear, selectedMonth, currentDay)
                                            val isInRange = startDate != null && endDate != null &&
                                                    (currentDate.isAfter(startDate) && currentDate.isBefore(endDate))
                                            val isSelected = currentDate == startDate || currentDate == endDate

                                            Button(
                                                onClick = {
                                                    when {
                                                        isSelectingStart || startDate != null && endDate != null -> {
                                                            startDate = currentDate
                                                            endDate = null
                                                            isSelectingStart = false
                                                        }
                                                        currentDate.isBefore(startDate) -> {
                                                            startDate = currentDate
                                                            isSelectingStart = false
                                                        }
                                                        else -> {
                                                            endDate = currentDate
                                                            isSelectingStart = true
                                                            // Dátumok formázása és visszaküldése
                                                            onDateRangeSelected(
                                                                formatDateToString(startDate!!),
                                                                formatDateToString(currentDate)
                                                            )
                                                            onDismiss()
                                                        }
                                                    }
                                                },
                                                modifier = Modifier.height(40.dp).width(60.dp),
                                                colors = ButtonDefaults.buttonColors(
                                                    backgroundColor = when {
                                                        isSelected -> MaterialTheme.colors.primary
                                                        isInRange -> MaterialTheme.colors.primaryVariant
                                                        else -> MaterialTheme.colors.surface
                                                    }
                                                )
                                            ) {
                                                Text(currentDay.toString())
                                            }
                                            dayCount++
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Vezérlő gombok
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            TextButton(
                                onClick = {
                                    startDate = null
                                    endDate = null
                                    isSelectingStart = true
                                }
                            ) {
                                Text("Törlés")
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = onDismiss) {
                                Text("Mégse")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "Január"
            2 -> "Február"
            3 -> "Március"
            4 -> "Április"
            5 -> "Május"
            6 -> "Június"
            7 -> "Július"
            8 -> "Augusztus"
            9 -> "Szeptember"
            10 -> "Október"
            11 -> "November"
            12 -> "December"
            else -> ""
        }
    }

    private fun formatDate(date: LocalDate): String {
        return "${date.year}. ${getMonthName(date.monthValue)} ${date.dayOfMonth}."
    }

    private fun formatDateToString(date: LocalDate): String {
        return String.format("%04d%02d%02d", date.year, date.monthValue, date.dayOfMonth)
    }



    // Példa használat
    @Composable
    fun DateRangePickerExample() {
        var showDialog by remember { mutableStateOf(false) }
        var startDate by remember { mutableStateOf("") }
        var endDate by remember { mutableStateOf("") }

        Button(onClick = { showDialog = true }) {
            Text("Időintervallum választása")
        }

        DateRangePicker(
            onDateRangeSelected = { start, end ->
                startDate = start
                endDate = end
            },
            onDismiss = { showDialog = false },
            showDialog = showDialog
        )

        if (startDate.isNotEmpty() && endDate.isNotEmpty()) {
            Text("Választott intervallum: $startDate - $endDate")
        }
    }


}