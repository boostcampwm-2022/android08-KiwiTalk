package com.kiwi.data.mapper

import java.util.Date

object DateFormatter {
    private const val SEC = 60
    private const val MIN = 60
    private const val HOUR = 24
    private const val DAY = 30
    private const val MONTH = 12

    fun Date.formatTimeString(): String {
        val regTime: Long = this.time
        val curTime = System.currentTimeMillis()

        var diffTime = (curTime - regTime) / 1000
        var msg: String = ""
        if (diffTime < SEC) {
            msg = "방금 전"
        } else if (SEC.let { diffTime /= it; diffTime } < MIN) {
            msg = diffTime.toString() + "분 전"
        } else if (MIN.let { diffTime /= it; diffTime } < HOUR) {
            msg = diffTime.toString() + "시간 전"
        } else if (HOUR.let { diffTime /= it; diffTime } < DAY) {
            msg = diffTime.toString() + "일 전"
        } else if (DAY.let { diffTime /= it; diffTime } < MONTH) {
            msg = diffTime.toString() + "달 전"
        } else {
            msg = diffTime.toString() + "년 전"
        }
        return msg
    }
}