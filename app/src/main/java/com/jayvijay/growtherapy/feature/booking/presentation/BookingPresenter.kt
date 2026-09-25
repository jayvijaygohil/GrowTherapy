package com.jayvijay.growtherapy.feature.booking.presentation

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

class BookingPresenter @AssistedInject constructor(@Assisted private val navigator: Navigator) :
  Presenter<BookingScreen.State> {

  @Composable
  override fun present(): BookingScreen.State {
    var isVirtual by rememberSaveable { mutableStateOf(true) }
    var notice by rememberSaveable { mutableStateOf<BookingNotice?>(null) }

    return BookingScreen.State(
      isVirtual = isVirtual,
      notice = notice,
    ) { event ->
      when (event) {
        is BookingScreen.Event.NavigateTo -> {
          if (event.destination != NavDestination.Booking) {
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

        BookingScreen.Event.Back -> {
          navigator.pop()
        }

        is BookingScreen.Event.SelectVirtual -> {
          isVirtual = event.selected
        }

        is BookingScreen.Event.ShowNotice -> {
          notice = event.notice
        }

        BookingScreen.Event.DismissNotice -> {
          notice = null
        }
      }
    }
  }

  @AssistedFactory
  @CircuitInject(BookingScreen::class, SingletonComponent::class)
  interface Factory {
    fun create(navigator: Navigator): BookingPresenter
  }
}
