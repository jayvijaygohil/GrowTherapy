package com.jayvijay.growtherapy.feature.navigation

import androidx.annotation.StringRes
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.feature.appointments.presentation.AppointmentsScreen
import com.jayvijay.growtherapy.feature.messages.presentation.MessagesScreen
import com.slack.circuit.runtime.screen.ParcelableScreen

enum class NavDestination(
  @param:StringRes val title: Int,
  val isRoot: Boolean = true,
) {
  Appointments(R.string.appointments),
  Messages(R.string.messages),
  Resources(R.string.resources),
  Coach(R.string.coach),
  Booking(R.string.book_with_gail, false),
  Recap(R.string.session_recap, false),
}

fun NavDestination.toScreen(): ParcelableScreen {
  return when (this) {
    NavDestination.Appointments -> AppointmentsScreen
    NavDestination.Messages -> MessagesScreen
    NavDestination.Resources ->
      com.jayvijay.growtherapy.feature.resources.presentation.ResourcesScreen
    NavDestination.Coach -> com.jayvijay.growtherapy.feature.coach.presentation.CoachScreen
    NavDestination.Booking -> com.jayvijay.growtherapy.feature.booking.presentation.BookingScreen
    NavDestination.Recap -> com.jayvijay.growtherapy.feature.recap.presentation.RecapScreen
  }
}
