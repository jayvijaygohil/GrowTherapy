package com.jayvijay.growtherapy.feature.messages.presentation

import androidx.annotation.StringRes
import com.jayvijay.growtherapy.R

enum class MessagesNotice(
  @param:StringRes val title: Int,
  @param:StringRes val body: Int,
) {
  Message(R.string.message_unavailable, R.string.message_unavailable_body),
  Attachment(R.string.attachment_unavailable, R.string.attachment_unavailable_body),
  Crisis(R.string.emergency, R.string.crisis_body),
  Profile(R.string.gail, R.string.profile_body),
}
