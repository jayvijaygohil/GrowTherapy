package com.jayvijay.growtherapy.feature.coach.presentation

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
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.conversation.Conversation
import com.jayvijay.growtherapy.ui.conversation.ConversationComposer
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(CoachScreen::class, SingletonComponent::class)
class CoachUi @Inject constructor() : Ui<CoachScreen.State> {
  @Composable
  override fun Content(
    state: CoachScreen.State,
    modifier: Modifier,
  ) {
    val events = state.eventSink

    NavigationShell(
      destination = NavDestination.Coach,
      onNavigate = { events(CoachScreen.Event.NavigateTo(it)) },
      onBack = { events(CoachScreen.Event.Back) },
      onBook = { events(CoachScreen.Event.NavigateTo(NavDestination.Booking)) },
      modifier = modifier,
      bottomBar = {
        ConversationComposer(
          isCoach = true,
          draft = state.draft,
          onDraftChange = { events(CoachScreen.Event.EditDraft(it)) },
          onSend = { events(CoachScreen.Event.Send) },
          onAccessory = { events(CoachScreen.Event.ShowNotice(CoachNotice.Voice)) },
          onCrisis = {},
          modifier =
            Modifier.padding(
              horizontal = GrowSpacing.lg,
              vertical = GrowSpacing.sm,
            ),
        )
      },
    ) {
      Column(
        Modifier.weight(1f)
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(
            start = GrowSpacing.lg,
            end = GrowSpacing.lg,
            top = GrowSpacing.lg,
            bottom = GrowSpacing.md,
          )
      ) {
        Conversation(isCoach = true)
      }
    }

    state.notice?.let { notice ->
      CoachNoticeDialog(notice, events)
    }
  }
}

@Composable
private fun CoachNoticeDialog(
  notice: CoachNotice,
  events: (CoachScreen.Event) -> Unit,
) {
  AlertDialog(
    onDismissRequest = { events(CoachScreen.Event.DismissNotice) },
    title = { GrowText(stringResource(notice.title)) },
    text = { GrowText(stringResource(notice.body)) },
    confirmButton = {
      TextButton({ events(CoachScreen.Event.DismissNotice) }) {
        GrowText(stringResource(R.string.okay))
      }
    },
  )
}
