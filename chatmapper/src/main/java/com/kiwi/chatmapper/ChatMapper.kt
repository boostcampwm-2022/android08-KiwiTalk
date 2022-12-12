package com.kiwi.chatmapper

import com.kiwi.chatmapper.ChatConst.EMPTY_STRING
import com.kiwi.chatmapper.ChatConst.STRING_SPACE
import com.kiwi.chatmapper.ChatKey.CHAT_ADDRESS
import com.kiwi.chatmapper.ChatKey.CHAT_DESCRIPTION
import com.kiwi.chatmapper.ChatKey.CHAT_KEYWORDS
import com.kiwi.chatmapper.ChatKey.CHAT_MAX_MEMBER_COUNT
import java.util.*

object ChatMapper {
    private const val SEC = 60
    private const val MIN = 60
    private const val HOUR = 24
    private const val DAY = 30
    private const val MONTH = 12

    fun getDateOfLastMassage(lastDate: Date?): String {
        val regTime = lastDate?.time
        val curTime = System.currentTimeMillis()

        if (regTime == null) return EMPTY_STRING

        var diffTime = (curTime - regTime) / 1000
        return if (diffTime < SEC) {
            "방금 전"
        } else if (SEC.let { diffTime /= it; diffTime } < MIN) {
            "${diffTime}분 전"
        } else if (MIN.let { diffTime /= it; diffTime } < HOUR) {
            "${diffTime}시간 전"
        } else if (HOUR.let { diffTime /= it; diffTime } < DAY) {
            "${diffTime}일 전"
        } else if (DAY.let { diffTime /= it; diffTime } < MONTH) {
            "${diffTime}달 전"
        } else {
            "${diffTime}년 전"
        }
    }

    fun getMaxMemberCount(extraData: Map<String, Any?>): Int {
        return (extraData[CHAT_MAX_MEMBER_COUNT] as? String ?: "0").toInt()
    }

    fun getTrimAddress(extraData: Map<String, Any?>): String {
        val fullAddress = extraData[CHAT_ADDRESS]
        return if (fullAddress !is String) {
            EMPTY_STRING
        } else {
            fullAddress.split(STRING_SPACE)
                .filter { it.last() in listOf('시', '도', '구', '군', '동', '읍', '면') }
                .joinToString(STRING_SPACE)
        }
    }

    fun getKeywords(extraData: Map<String, Any?>): List<String> {
        val list = extraData[CHAT_KEYWORDS] as? List<*>
        return list?.filterIsInstance<String>().orEmpty()
    }

    fun getDescription(extraData: Map<String, Any?>): String {
        return (extraData[CHAT_DESCRIPTION] as? String) ?: EMPTY_STRING
    }
}