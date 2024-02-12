package com.pss.amazingchart

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pss.amazingchart.ui.theme.AmazingChartTheme
import java.text.SimpleDateFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AmazingChartTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AmazingChart()
                }
            }
        }
    }
}

@Composable
fun AmazingChart(
    modifier: Modifier = Modifier,
    timeInterval: Int = 10,
    topBlank: Int = 0,
    bottomBlank: Int = 0,
    chartDataList: List<DayChartBar> = listOf(
        DayChartBar(
            //10:30, 8:30
            actualSleep = CharacteristicBar(
                topTimeStamp = 1707701407000,
                bottomTimeStamp = 1707780607000
            ),
            //16:30, 12:30
            estimatedSleep = CharacteristicBar(
                topTimeStamp = 1707723007000,
                bottomTimeStamp = 1707708607000
            ),
            //02.09
            "1707479354"
        )
    )
) {

    val colors = listOf(Color.Black, Color.Red, Color.Blue, Color.Green)
    val textMeasurer = rememberTextMeasurer()
    val textToDraw = "30:30"
    val textStyle = TextStyle(
        fontSize = 15.sp,
        color = Color.Black,
        background = Color.Red.copy(alpha = 0.2f)
    )
    val textLayoutResult = remember(textToDraw) {
        textMeasurer.measure(textToDraw, textStyle)
    }
    val sdf = SimpleDateFormat("HHmm")
    val topList = mutableListOf<Int>()
    val bottomList = mutableListOf<Int>()

    //가장 높은 시간과 가장 낮은 시간 계산
    for (item in chartDataList) {
        val highTime = if (sdf.format(item.actualSleep.topTimeStamp)
                .toInt() > sdf.format(item.estimatedSleep.topTimeStamp).toInt())
            sdf.format(item.actualSleep.topTimeStamp).toInt()
        else sdf.format(item.estimatedSleep.topTimeStamp).toInt()

        val lowTime = if (sdf.format(item.actualSleep.bottomTimeStamp)
                .toInt() > sdf.format(item.estimatedSleep.bottomTimeStamp).toInt())
            sdf.format(item.estimatedSleep.bottomTimeStamp).toInt()
        else sdf.format(item.actualSleep.bottomTimeStamp).toInt()

        topList.add(highTime)
        bottomList.add(lowTime)
    }
    topList.sortByDescending { it }
    bottomList.sortByDescending { it }

    Log.d("carly","top : ${topList[0]}, bottom : ${bottomList[0]}")

    Canvas(modifier = Modifier.fillMaxSize()) {
        for (y in 1..5) {

            drawText(
                textMeasurer = textMeasurer,
                text = textToDraw,
                style = textStyle,
                topLeft = Offset(
                    x = 0.dp.toPx(),
                    y = ((y * 100).dp.toPx()) - (textLayoutResult.size.height / 2)
                )
            )

            drawLine(
                Color.Black,
                start = Offset(50.dp.toPx(), (y * 100).dp.toPx()),
                end = Offset(400.dp.toPx(), (y * 100).dp.toPx()),
                strokeWidth = 5.dp.toPx(),
                cap = StrokeCap.Square,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 40f), 0f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AmazingChartTheme {
        AmazingChart()
    }
}