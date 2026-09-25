package com.jayvijay.growtherapy.feature.booking.presentation

import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.ParcelableScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data object BookingScreen : ParcelableScreen {

  data class State(
    val isVirtual: Boolean = true,
    val notice: BookingNotice? = null,
    val eventSink: (Event) -> Unit = {},
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data class NavigateTo(val destination: NavDestination) : Event

    data object Back : Event

    data class SelectVirtual(val selected: Boolean) : Event

    data class ShowNotice(val notice: BookingNotice) : Event

    data object DismissNotice : Event
  }
}
