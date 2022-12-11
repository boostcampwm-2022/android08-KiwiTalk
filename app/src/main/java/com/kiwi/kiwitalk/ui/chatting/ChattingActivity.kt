package com.kiwi.kiwitalk.ui.chatting

import android.content.Context
import android.content.Intent
import com.kiwi.kiwitalk.R
import io.getstream.chat.android.ui.message.MessageListActivity
import io.getstream.chat.android.ui.message.MessageListFragment

class ChattingActivity : MessageListActivity() {
    override fun createMessageListFragment(cid: String, messageId: String?): MessageListFragment {
        return MessageListFragment.newInstance(cid) {
            setFragment(MessageListFragment())
            customTheme(R.style.Theme_KiwiTalk_NoActionBar)
            showHeader(false)
            messageId(messageId)
        }
    }

    companion object {
        private const val EXTRA_CID: String = "extra_cid"
        private const val EXTRA_MESSAGE_ID: String = "extra_message_id"

        @JvmStatic
        @JvmOverloads
        fun createIntent(context: Context, cid: String, messageId: String? = null): Intent {
            return Intent(context, MessageListActivity::class.java).apply {
                putExtra(EXTRA_CID, cid)
                putExtra(EXTRA_MESSAGE_ID, messageId)
            }
        }
    }
}