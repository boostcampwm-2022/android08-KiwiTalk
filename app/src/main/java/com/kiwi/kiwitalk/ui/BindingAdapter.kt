package com.kiwi.kiwitalk.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kiwi.data.Const
import com.kiwi.kiwitalk.R
import java.util.*

@BindingAdapter("loadImageByUri")
fun setImage(imageView: ImageView, uri: String?) {
    Glide.with(imageView.context)
        .load(uri)
        .placeholder(R.drawable.ic_baseline_cloud_sync_24)
        .error(R.drawable.logo_splash_transparent)
        .fallback(R.drawable.ic_baseline_cloud_sync_24)
        .fitCenter()
        .into(imageView)
}

@BindingAdapter("setUnreadCount")
fun setUnreadCount(textView: TextView, count: Int?) {
    when (count) {
        0, null -> textView.visibility = View.INVISIBLE
        in 1..999 -> {
            textView.visibility = View.VISIBLE
            textView.text = count.toString()
        }
        else -> {
            textView.visibility = View.VISIBLE
            textView.text = textView.context.getText(R.string.tv_chatList_unreadCountMax)
        }
    }
}

@BindingAdapter("setAddress")
fun setTrimAddress(textView: TextView, fullAddress: Any?) {
    textView.text = if (fullAddress !is String) {
        Const.SPACE
    } else {
        fullAddress.split(Const.SPACE)
            .filter { it.last() in listOf('시', '도', '구', '군', '동', '읍', '면') }
            .joinToString(Const.SPACE)
    }
}

@BindingAdapter("setDateOfLastMassage")
fun setDateOfLastMassage(textView: TextView, lastDate: Date?) {
    val regTime = lastDate?.time
    val curTime = System.currentTimeMillis()

    if (regTime == null) {
        textView.text = "방금 전"
        return
    }

    var diffTime = (curTime - regTime) / 1000
    textView.text = if (diffTime < com.kiwi.kiwitalk.util.Const.SEC) {
        "알 수 없음"
    } else if (com.kiwi.kiwitalk.util.Const.SEC.let { diffTime /= it; diffTime } < com.kiwi.kiwitalk.util.Const.MIN) {
        "${diffTime}분 전"
    } else if (com.kiwi.kiwitalk.util.Const.MIN.let { diffTime /= it; diffTime } < com.kiwi.kiwitalk.util.Const.HOUR) {
        "${diffTime}시간 전"
    } else if (com.kiwi.kiwitalk.util.Const.HOUR.let { diffTime /= it; diffTime } < com.kiwi.kiwitalk.util.Const.DAY) {
        "${diffTime}일 전"
    } else if (com.kiwi.kiwitalk.util.Const.DAY.let { diffTime /= it; diffTime } < com.kiwi.kiwitalk.util.Const.MONTH) {
        "${diffTime}달 전"
    } else {
        "${diffTime}년 전"
    }
}