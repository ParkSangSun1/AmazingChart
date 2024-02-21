package com.pss.amazingchart

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BottomAppBar
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
                    AmazingChart(
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun AmazingChart(
    modifier: Modifier = Modifier,
    //0.5 = 30분
    timeInterval: Double = 0.5,
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
                .toInt() > sdf.format(item.estimatedSleep.topTimeStamp).toInt()
        )
            sdf.format(item.actualSleep.topTimeStamp).toInt()
        else sdf.format(item.estimatedSleep.topTimeStamp).toInt()

        val lowTime = if (sdf.format(item.actualSleep.bottomTimeStamp)
                .toInt() > sdf.format(item.estimatedSleep.bottomTimeStamp).toInt()
        )
            sdf.format(item.estimatedSleep.bottomTimeStamp).toInt()
        else sdf.format(item.actualSleep.bottomTimeStamp).toInt()

        topList.add(highTime)
        bottomList.add(lowTime)
    }
    topList.sortByDescending { it }
    bottomList.sortByDescending { it }

    Log.d("carly", "top : ${topList[0].toHour()}, bottom : ${bottomList[0].toHour()}")




    Box(modifier = modifier) {
        Canvas(modifier = modifier) {

            //가장 높은 top 값과 가장 낮은 bottom 값의 사이 공간 값
            val interval = topList[0].toHour() - bottomList[0].toHour()

            //interval 값을 사용자가 정의한 간격 높이로 몇 번 그릴지
            val lineNumber = (interval / timeInterval)

            //line 간의 간격
            val lineHeightInterval = this.size.height.dp.value / lineNumber

            Log.d(
                "carly",
                "canvas : ${this.size.width.toDp()}, ${size.height.toDp()}, $lineHeightInterval"
            )

            for (y in 1..lineNumber.toInt()) {

                Log.d("carly", "for : ${lineHeightInterval * y}")
                drawText(
                    textMeasurer = textMeasurer,
                    text = textToDraw,
                    style = textStyle,
                    topLeft = Offset(
                        x = 0.dp.toPx(),
                        y = ((lineHeightInterval * y) - (textLayoutResult.size.height / 2)).toFloat()
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

}

val Float.dpInt: Int
    get() = (this * Resources.getSystem().displayMetrics.density + 0.5f).toInt()

fun Int.toHour(): Int {
    val time = this.toString()
    return if (time.length > 3) time.substring(0, 2).toInt()
    else time.substring(0, 1).toInt()
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AmazingChartTheme {
        AmazingChart()
    }
}