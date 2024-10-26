package screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import io.github.koalaplot.core.Symbol
import io.github.koalaplot.core.line.LinePlot
import io.github.koalaplot.core.style.LineStyle
import io.github.koalaplot.core.util.ExperimentalKoalaPlotApi
import io.github.koalaplot.core.xygraph.DefaultPoint
import io.github.koalaplot.core.xygraph.XYGraph
import io.github.koalaplot.core.xygraph.autoScaleXRange
import io.github.koalaplot.core.xygraph.autoScaleYRange
import io.github.koalaplot.core.xygraph.rememberLinearAxisModel
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class ThermometerData(
    val id: String,
    val times: List<String>,
    val temperatures: List<Int>
)

class ChartScreen(
    private val navigator: Navigator,
    private val thermometersData: List<ThermometerData>
) : Screen {

    private val colors = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Magenta,
        Color.Cyan,
        Color(0xFFFFA500), // Orange
        Color(0xFF800080), // Purple
        Color(0xFF008080), // Teal
        Color(0xFF800000), // Maroon
        Color(0xFF808000)  // Olive
    )

    @OptIn(ExperimentalKoalaPlotApi::class)
    @Composable
    override fun Content() {
        Box(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("Hőmérséklet Diagram")

                // Legend
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    thermometersData.forEachIndexed { index, data ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(end = 4.dp)
                                    .background(colors[index % colors.size])
                            )
                            Text(data.id)
                        }
                    }
                }

                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

                // Create data points for all thermometers
                val allDataPoints = thermometersData.mapIndexed { index, thermometer ->
                    val points = buildList {
                        thermometer.times.zip(thermometer.temperatures).forEach { (timeStr, temp) ->
                            val epochSeconds = LocalDateTime.parse(timeStr, formatter)
                                .toEpochSecond(ZoneOffset.UTC)
                            add(DefaultPoint(epochSeconds.toFloat(), temp.toFloat()))
                        }
                    }
                    points to colors[index % colors.size]
                }

                // Find global min/max for axes
                val allPoints = allDataPoints.flatMap { it.first }

                Box(modifier = Modifier.weight(1f)) {
                    XYGraph(
                        xAxisModel = rememberLinearAxisModel(allPoints.autoScaleXRange()),
                        yAxisModel = rememberLinearAxisModel(allPoints.autoScaleYRange()),
                        xAxisTitle = "Idő",
                        yAxisTitle = "Hőmérséklet (°C)",
                        modifier = Modifier.fillMaxSize()
                    ) {
                        allDataPoints.forEach { (points, color) ->
                            LinePlot(
                                points,
                                lineStyle = LineStyle(SolidColor(color)),
                                symbol = {
                                    Symbol(
                                        fillBrush = SolidColor(color),
                                        outlineBrush = SolidColor(Color.Black),
                                        size = 8.dp
                                    )
                                }
                            )
                        }
                    }
                }

                Button(
                    onClick = { navigator.pop() },
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(text = "Vissza")
                }
            }
        }
    }
}