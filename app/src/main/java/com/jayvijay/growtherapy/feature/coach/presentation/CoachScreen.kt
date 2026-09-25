package com.jayvijay.growtherapy.feature.coach.presentation

import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.ParcelableScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data object CoachScreen : ParcelableScreen {

  data class State(
    val draft: String = "",
    val notice: CoachNotice? = null,
    val eventSink: (Event) -> Unit = {},
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data class NavigateTo(val destination: NavDestination) : Event

    data object Back : Event

    data class EditDraft(val value: String) : Event

    data object Send : Event

    data class ShowNotice(val notice: CoachNotice) : Event

    data object DismissNotice : Event
  }
}
