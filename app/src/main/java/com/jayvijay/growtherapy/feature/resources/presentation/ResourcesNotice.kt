package com.jayvijay.growtherapy.feature.resources.presentation

import androidx.annotation.StringRes
import com.jayvijay.growtherapy.R

enum class ResourcesNotice(
  @param:StringRes val title: Int,
  @param:StringRes val body: Int,
) {
  Resource(R.string.resource_unavailable, R.string.resource_unavailable_body),
  Voice(R.string.voice_unavailable, R.string.voice_unavailable_body),
}
