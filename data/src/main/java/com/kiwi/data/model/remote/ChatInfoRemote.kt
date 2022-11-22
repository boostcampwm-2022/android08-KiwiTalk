package com.kiwi.data.model.remote

import io.getstream.chat.android.client.models.*
import io.getstream.chat.android.client.utils.SyncStatus
import java.util.*

/**
 * getStream의 Channel을 그대로 복사해온 것.
 * 자료형 참고할 때만 쓰고 프로덕션 코드에서는 삭제해야한다.
 * */
data class ChatInfoRemote(
    var cid: String = "",
    var id: String = "",
    var type: String = "",
    var name: String = "",
    var image: String = "",
    var watcherCount: Int = 0,
    var frozen: Boolean = false,
    var lastMessageAt: Date? = null,
    var createdAt: Date? = null,
    var deletedAt: Date? = null,
    var updatedAt: Date? = null,
    var syncStatus: SyncStatus = SyncStatus.COMPLETED,
    var memberCount: Int = 0,
    var messages: List<Message> = mutableListOf(),
    var members: List<Member> = mutableListOf(),
    var watchers: List<User> = mutableListOf(),
    var read: List<ChannelUserRead> = mutableListOf(),
    var config: Config = Config(),
    var createdBy: User = User(),
    var unreadCount: Int? = null,
    val team: String = "",
    var hidden: Boolean? = null,
    var hiddenMessagesBefore: Date? = null,
    val cooldown: Int = 0,
    var pinnedMessages: List<Message> = mutableListOf(),
    var ownCapabilities: Set<String> = setOf(),
    var membership: Member? = null,
    var extraData: MutableMap<String, Any> = mutableMapOf(),
)