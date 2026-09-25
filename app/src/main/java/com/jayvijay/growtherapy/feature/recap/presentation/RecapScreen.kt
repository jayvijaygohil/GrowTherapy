package com.jayvijay.growtherapy.feature.recap.presentation

import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.ParcelableScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data object RecapScreen : ParcelableScreen {

  data class State(
    val notice: RecapNotice? = null,
    val eventSink: (Event) -> Unit = {},
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data class NavigateTo(val destination: NavDestination) : Event

    data object Back : Event

    data class ShowNotice(val notice: RecapNotice) : Event

    data object DismissNotice : Event
  }
}
