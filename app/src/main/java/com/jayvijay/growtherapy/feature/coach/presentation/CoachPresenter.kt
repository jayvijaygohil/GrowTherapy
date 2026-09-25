package com.jayvijay.growtherapy.feature.coach.presentation

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

class CoachPresenter @AssistedInject constructor(@Assisted private val navigator: Navigator) :
  Presenter<CoachScreen.State> {

  @Composable
  override fun present(): CoachScreen.State {
    var draft by rememberSaveable { mutableStateOf("") }
    var notice by rememberSaveable { mutableStateOf<CoachNotice?>(null) }

    return CoachScreen.State(
      draft = draft,
      notice = notice,
    ) { event ->
      when (event) {
        is CoachScreen.Event.NavigateTo -> {
          if (event.destination != NavDestination.Coach) {
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

        CoachScreen.Event.Back -> {
          navigator.pop()
        }

        is CoachScreen.Event.EditDraft -> {
          draft = event.value
        }

        CoachScreen.Event.Send -> {
          if (draft.isNotBlank()) {
            notice = CoachNotice.Message
          }
        }

        is CoachScreen.Event.ShowNotice -> {
          notice = event.notice
        }

        CoachScreen.Event.DismissNotice -> {
          notice = null
        }
      }
    }
  }

  @AssistedFactory
  @CircuitInject(CoachScreen::class, SingletonComponent::class)
  interface Factory {
    fun create(navigator: Navigator): CoachPresenter
  }
}
