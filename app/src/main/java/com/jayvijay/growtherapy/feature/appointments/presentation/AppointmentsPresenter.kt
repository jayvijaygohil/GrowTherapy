package com.jayvijay.growtherapy.feature.appointments.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.jayvijay.growtherapy.feature.navigation.toScreen
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.Navigator
import com.slack.circuit.runtime.presenter.Presenter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.components.SingletonComponent

class AppointmentsPresenter
@AssistedInject
constructor(@Assisted private val navigator: Navigator) : Presenter<AppointmentsScreen.State> {

  @Composable
  override fun present(): AppointmentsScreen.State {
    var showPast by rememberSaveable { mutableStateOf(false) }
    var topics by rememberSaveable { mutableStateOf("") }
    var topicsDraft by rememberSaveable { mutableStateOf("") }
    var notice by rememberSaveable { mutableStateOf<AppointmentsNotice?>(null) }

    return AppointmentsScreen.State(
      showPast = showPast,
      topics = topics,
      topicsDraft = topicsDraft,
      notice = notice,
    ) { event ->
      when (event) {
        is AppointmentsScreen.Event.NavigateTo -> {
          if (event.destination != NavDestination.Appointments) {
            if (event.destination.isRoot) {
              navigator.resetRoot(
                event.destination.toScreen(),
                Navigator.StateOptions.SaveAndRestore,
              )
            } else {
              navigator.goTo(event.destination.toScreen())
            }
          }
        }

        AppointmentsScreen.Event.Back -> {
          navigator.pop()
        }

        is AppointmentsScreen.Event.SelectPast -> {
          showPast = event.selected
        }

        AppointmentsScreen.Event.EditTopics -> {
          topicsDraft = topics
          notice = AppointmentsNotice.Topics
        }

        is AppointmentsScreen.Event.ChangeTopics -> {
          topicsDraft = event.value
        }

        AppointmentsScreen.Event.SaveTopics -> {
          topics = topicsDraft.trim()
          notice = null
        }

        AppointmentsScreen.Event.NavigateToBooking -> {
          navigator.goTo(com.jayvijay.growtherapy.feature.booking.presentation.BookingScreen)
        }

        AppointmentsScreen.Event.NavigateToRecap -> {
          navigator.goTo(com.jayvijay.growtherapy.feature.recap.presentation.RecapScreen)
        }

        is AppointmentsScreen.Event.ShowNotice -> {
          notice = event.notice
        }

        AppointmentsScreen.Event.DismissNotice -> {
          notice = null
        }
      }
    }
  }

  @AssistedFactory
  @CircuitInject(AppointmentsScreen::class, SingletonComponent::class)
  interface Factory {
    fun create(navigator: Navigator): AppointmentsPresenter
  }
}
