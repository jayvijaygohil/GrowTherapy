package com.jayvijay.growtherapy.feature.recap.presentation

import androidx.annotation.StringRes
import com.jayvijay.growtherapy.R

enum class RecapNotice(
  @param:StringRes val title: Int,
  @param:StringRes val body: Int,
) {
  Audio(R.string.audio_unavailable, R.string.audio_unavailable_body)
}
