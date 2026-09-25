package com.jayvijay.growtherapy.feature.recap.presentation

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

class RecapPresenter @AssistedInject constructor(@Assisted private val navigator: Navigator) :
  Presenter<RecapScreen.State> {

  @Composable
  override fun present(): RecapScreen.State {
    var notice by rememberSaveable { mutableStateOf<RecapNotice?>(null) }

    return RecapScreen.State(notice = notice) { event ->
      when (event) {
        is RecapScreen.Event.NavigateTo -> {
          if (event.destination != NavDestination.Recap) {
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

        RecapScreen.Event.Back -> {
          navigator.pop()
        }

        is RecapScreen.Event.ShowNotice -> {
          notice = event.notice
        }

        RecapScreen.Event.DismissNotice -> {
          notice = null
        }
      }
    }
  }

  @AssistedFactory
  @CircuitInject(RecapScreen::class, SingletonComponent::class)
  interface Factory {
    fun create(navigator: Navigator): RecapPresenter
  }
}
