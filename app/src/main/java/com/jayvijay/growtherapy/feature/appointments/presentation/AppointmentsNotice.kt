package com.jayvijay.growtherapy.feature.appointments.presentation

import androidx.annotation.StringRes
import com.jayvijay.growtherapy.R

enum class AppointmentsNotice(
  @param:StringRes val title: Int,
  @param:StringRes val body: Int,
) {
  Topics(R.string.add_topics, R.string.topics_hint),
  Profile(R.string.gail, R.string.profile_body),
}
