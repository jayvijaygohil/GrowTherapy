package com.jayvijay.growtherapy.feature.messages.presentation

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

class MessagesPresenter @AssistedInject constructor(@Assisted private val navigator: Navigator) :
  Presenter<MessagesScreen.State> {

  @Composable
  override fun present(): MessagesScreen.State {
    var draft by rememberSaveable { mutableStateOf("") }
    var notice by rememberSaveable { mutableStateOf<MessagesNotice?>(null) }

    return MessagesScreen.State(
      draft = draft,
      notice = notice,
    ) { event ->
      when (event) {
        is MessagesScreen.Event.NavigateTo -> {
          if (event.destination != NavDestination.Messages) {
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

        MessagesScreen.Event.Back -> {
          navigator.pop()
        }

        is MessagesScreen.Event.EditDraft -> {
          draft = event.value
        }

        MessagesScreen.Event.Send -> {
          if (draft.isNotBlank()) {
            notice = MessagesNotice.Message
          }
        }

        is MessagesScreen.Event.ShowNotice -> {
          notice = event.notice
        }

        MessagesScreen.Event.DismissNotice -> {
          notice = null
        }
      }
    }
  }

  @AssistedFactory
  @CircuitInject(MessagesScreen::class, SingletonComponent::class)
  interface Factory {
    fun create(navigator: Navigator): MessagesPresenter
  }
}
