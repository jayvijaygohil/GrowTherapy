package com.jayvijay.growtherapy.feature.resources.presentation

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

class ResourcesPresenter @AssistedInject constructor(@Assisted private val navigator: Navigator) :
  Presenter<ResourcesScreen.State> {

  @Composable
  override fun present(): ResourcesScreen.State {
    var query by rememberSaveable { mutableStateOf("") }
    var notice by rememberSaveable { mutableStateOf<ResourcesNotice?>(null) }

    return ResourcesScreen.State(
      query = query,
      notice = notice,
    ) { event ->
      when (event) {
        is ResourcesScreen.Event.NavigateTo -> {
          if (event.destination != NavDestination.Resources) {
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

        ResourcesScreen.Event.Back -> {
          navigator.pop()
        }

        is ResourcesScreen.Event.Search -> {
          query = event.value
        }

        is ResourcesScreen.Event.ShowNotice -> {
          notice = event.notice
        }

        ResourcesScreen.Event.DismissNotice -> {
          notice = null
        }
      }
    }
  }

  @AssistedFactory
  @CircuitInject(ResourcesScreen::class, SingletonComponent::class)
  interface Factory {
    fun create(navigator: Navigator): ResourcesPresenter
  }
}
