package com.jayvijay.growtherapy.feature.recap.presentation

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
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.jayvijay.growtherapy.feature.navigation.NavigationShell
import com.jayvijay.growtherapy.feature.recap.components.SessionSummary
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(RecapScreen::class, SingletonComponent::class)
class RecapUi @Inject constructor() : Ui<RecapScreen.State> {
  @Composable
  override fun Content(
    state: RecapScreen.State,
    modifier: Modifier,
  ) {
    val events = state.eventSink

    NavigationShell(
      destination = NavDestination.Recap,
      onNavigate = { events(RecapScreen.Event.NavigateTo(it)) },
      onBack = { events(RecapScreen.Event.Back) },
      onBook = { events(RecapScreen.Event.NavigateTo(NavDestination.Booking)) },
      modifier = modifier,
    ) {
      Column(
        Modifier.weight(1f)
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(
            start = GrowSpacing.lg,
            end = GrowSpacing.lg,
            top = GrowSpacing.md,
            bottom = GrowSpacing.md,
          )
      ) {
        SessionSummary(onPlay = { events(RecapScreen.Event.ShowNotice(RecapNotice.Audio)) })
      }
    }

    state.notice?.let { notice ->
      RecapNoticeDialog(notice, events)
    }
  }
}

@Composable
private fun RecapNoticeDialog(
  notice: RecapNotice,
  events: (RecapScreen.Event) -> Unit,
) {
  AlertDialog(
    onDismissRequest = { events(RecapScreen.Event.DismissNotice) },
    title = { GrowText(stringResource(notice.title)) },
    text = { GrowText(stringResource(notice.body)) },
    confirmButton = {
      TextButton({ events(RecapScreen.Event.DismissNotice) }) {
        GrowText(stringResource(R.string.okay))
      }
    },
  )
}
