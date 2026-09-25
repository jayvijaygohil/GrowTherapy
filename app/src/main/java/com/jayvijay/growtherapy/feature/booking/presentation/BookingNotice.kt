package com.jayvijay.growtherapy.feature.booking.presentation

import androidx.annotation.StringRes
import com.jayvijay.growtherapy.R

enum class BookingNotice(
  @param:StringRes val title: Int,
  @param:StringRes val body: Int,
) {
  Booking(R.string.booking_unavailable, R.string.booking_unavailable_body),
  Profile(R.string.gail, R.string.profile_body),
}
