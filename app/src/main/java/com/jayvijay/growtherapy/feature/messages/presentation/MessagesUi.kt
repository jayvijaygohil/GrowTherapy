package com.jayvijay.growtherapy.feature.messages.presentation

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

@CircuitInject(MessagesScreen::class, SingletonComponent::class)
class MessagesUi @Inject constructor() : Ui<MessagesScreen.State> {
  @Composable
  override fun Content(
    state: MessagesScreen.State,
    modifier: Modifier,
  ) {
    val events = state.eventSink

    NavigationShell(
      destination = NavDestination.Messages,
      onNavigate = { events(MessagesScreen.Event.NavigateTo(it)) },
      onBack = { events(MessagesScreen.Event.Back) },
      onBook = { events(MessagesScreen.Event.NavigateTo(NavDestination.Booking)) },
      onProfile = { events(MessagesScreen.Event.ShowNotice(MessagesNotice.Profile)) },
      modifier = modifier,
      bottomBar = {
        ConversationComposer(
          isCoach = false,
          draft = state.draft,
          onDraftChange = { events(MessagesScreen.Event.EditDraft(it)) },
          onSend = { events(MessagesScreen.Event.Send) },
          onAccessory = { events(MessagesScreen.Event.ShowNotice(MessagesNotice.Attachment)) },
          onCrisis = { events(MessagesScreen.Event.ShowNotice(MessagesNotice.Crisis)) },
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
        Conversation(isCoach = false)
      }
    }

    state.notice?.let { notice ->
      MessagesNoticeDialog(notice, events)
    }
  }
}

@Composable
private fun MessagesNoticeDialog(
  notice: MessagesNotice,
  events: (MessagesScreen.Event) -> Unit,
) {
  AlertDialog(
    onDismissRequest = { events(MessagesScreen.Event.DismissNotice) },
    title = { GrowText(stringResource(notice.title)) },
    text = { GrowText(stringResource(notice.body)) },
    confirmButton = {
      TextButton({ events(MessagesScreen.Event.DismissNotice) }) {
        GrowText(stringResource(R.string.okay))
      }
    },
  )
}
