package com.pss.amazingchart

data class DayChartBar(
    val actualSleep : CharacteristicBar,
    val estimatedSleep : CharacteristicBar,
    val date: String
)

data class CharacteristicBar(
    val topTimeStamp : Long,
    val bottomTimeStamp : Long
)