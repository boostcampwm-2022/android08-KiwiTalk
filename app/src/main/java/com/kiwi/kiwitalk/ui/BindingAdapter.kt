package com.kiwi.kiwitalk.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.kiwi.kiwitalk.R
import com.kiwi.kiwitalk.util.Const
import io.getstream.chat.android.client.models.Channel
import java.util.*

@BindingAdapter("loadImageByUri")
fun setImage(imageView: ImageView, uri: String?) {
    Glide.with(imageView.context)
        .load(uri)
        .placeholder(R.drawable.ic_baseline_cloud_sync_24)
        .error(R.drawable.logo_splash_light)
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
        textView.text = Const.EMPTY_STRING
        return
    }

    var diffTime = (curTime - regTime) / 1000
    textView.text = if (diffTime < Const.SEC) {
        "방금 전"
    } else if (Const.SEC.let { diffTime /= it; diffTime } < Const.MIN) {
        "${diffTime}분 전"
    } else if (Const.MIN.let { diffTime /= it; diffTime } < Const.HOUR) {
        "${diffTime}시간 전"
    } else if (Const.HOUR.let { diffTime /= it; diffTime } < Const.DAY) {
        "${diffTime}일 전"
    } else if (Const.DAY.let { diffTime /= it; diffTime } < Const.MONTH) {
        "${diffTime}달 전"
    } else {
        "${diffTime}년 전"
    }
}

@BindingAdapter("setChatMemberCount")
fun setChatMemberCount(textView: TextView, chat: Channel) {
    val now = chat.memberCount
    val max = (chat.extraData["max_member_count"] as? String ?: "0").toInt()
    textView.text =
        textView.context.resources.getString(R.string.tv_chatList_member_count, now, max)
}