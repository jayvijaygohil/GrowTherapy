package com.jayvijay.growtherapy.feature.resources.presentation

import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.slack.circuit.runtime.CircuitUiEvent
import com.slack.circuit.runtime.CircuitUiState
import com.slack.circuit.runtime.screen.ParcelableScreen
import kotlinx.parcelize.Parcelize

@Parcelize
data object ResourcesScreen : ParcelableScreen {

  data class State(
    val query: String = "",
    val notice: ResourcesNotice? = null,
    val eventSink: (Event) -> Unit = {},
  ) : CircuitUiState

  sealed interface Event : CircuitUiEvent {
    data class NavigateTo(val destination: NavDestination) : Event

    data object Back : Event

    data class Search(val value: String) : Event

    data class ShowNotice(val notice: ResourcesNotice) : Event

    data object DismissNotice : Event
  }
}
