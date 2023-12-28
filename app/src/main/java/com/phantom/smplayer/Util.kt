package com.phantom.smplayer

fun formatToHHmmss(hours: Long, minutes: Long, seconds: Long): String {
    if (hours.toInt() == 0) return String.format("%02d:%02d", minutes, seconds)
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

fun convertMillisecondsToHHmmss(milliseconds: Long): String {
    val totalSeconds = milliseconds / 1000
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return formatToHHmmss(hours, minutes, seconds)
}