package com.jayvijay.growtherapy.feature.coach.presentation

import androidx.annotation.StringRes
import com.jayvijay.growtherapy.R

enum class CoachNotice(
  @param:StringRes val title: Int,
  @param:StringRes val body: Int,
) {
  Message(R.string.message_unavailable, R.string.message_unavailable_body),
  Voice(R.string.voice_unavailable, R.string.voice_unavailable_body),
}
