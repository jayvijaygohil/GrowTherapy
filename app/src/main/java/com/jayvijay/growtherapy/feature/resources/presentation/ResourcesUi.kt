package com.jayvijay.growtherapy.feature.resources.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.jayvijay.growtherapy.feature.navigation.NavigationShell
import com.jayvijay.growtherapy.feature.resources.components.ResourceLibrary
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.inputs.ResourceSearch
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(ResourcesScreen::class, SingletonComponent::class)
class ResourcesUi @Inject constructor() : Ui<ResourcesScreen.State> {
  @Composable
  override fun Content(
    state: ResourcesScreen.State,
    modifier: Modifier,
  ) {
    val events = state.eventSink

    NavigationShell(
      destination = NavDestination.Resources,
      onNavigate = { events(ResourcesScreen.Event.NavigateTo(it)) },
      onBack = { events(ResourcesScreen.Event.Back) },
      onBook = { events(ResourcesScreen.Event.NavigateTo(NavDestination.Booking)) },
      modifier = modifier,
      bottomBar = {
        ResourceSearch(
          value = state.query,
          onValueChange = { events(ResourcesScreen.Event.Search(it)) },
          onVoice = { events(ResourcesScreen.Event.ShowNotice(ResourcesNotice.Voice)) },
          modifier = Modifier.padding(GrowSpacing.md),
        )
      },
    ) {
      Column(
        Modifier.weight(1f)
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(
            start = 0.dp,
            end = 0.dp,
            top = GrowSpacing.md,
            bottom = GrowSpacing.md,
          )
      ) {
        ResourceLibrary(
          query = state.query,
          onResource = { events(ResourcesScreen.Event.ShowNotice(ResourcesNotice.Resource)) },
        )
      }
    }

    state.notice?.let { notice ->
      ResourcesNoticeDialog(notice, events)
    }
  }
}

@Composable
private fun ResourcesNoticeDialog(
  notice: ResourcesNotice,
  events: (ResourcesScreen.Event) -> Unit,
) {
  AlertDialog(
    onDismissRequest = { events(ResourcesScreen.Event.DismissNotice) },
    title = { GrowText(stringResource(notice.title)) },
    text = { GrowText(stringResource(notice.body)) },
    confirmButton = {
      TextButton({ events(ResourcesScreen.Event.DismissNotice) }) {
        GrowText(stringResource(R.string.okay))
      }
    },
  )
}
