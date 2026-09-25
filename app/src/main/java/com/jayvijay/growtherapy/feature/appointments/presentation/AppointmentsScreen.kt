package com.jayvijay.growtherapy.feature.appointments.presentation

import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.ParcelableScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data object AppointmentsScreen : ParcelableScreen {

  data class State(
    val showPast: Boolean = false,
    val topics: String = "",
    val topicsDraft: String = "",
    val notice: AppointmentsNotice? = null,
    val eventSink: (Event) -> Unit = {},
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data class NavigateTo(val destination: NavDestination) : Event

    data object Back : Event

    data class SelectPast(val selected: Boolean) : Event

    data object EditTopics : Event

    data class ChangeTopics(val value: String) : Event

    data object SaveTopics : Event

    data object NavigateToBooking : Event

    data object NavigateToRecap : Event

    data class ShowNotice(val notice: AppointmentsNotice) : Event

    data object DismissNotice : Event
  }
}
