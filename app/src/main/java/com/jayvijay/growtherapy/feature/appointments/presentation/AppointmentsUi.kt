package com.jayvijay.growtherapy.feature.appointments.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.feature.appointments.components.AppointmentOverview
import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.jayvijay.growtherapy.feature.navigation.NavigationShell
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject

@CircuitInject(AppointmentsScreen::class, SingletonComponent::class)
class AppointmentsUi @Inject constructor() : Ui<AppointmentsScreen.State> {
  @Composable
  override fun Content(
    state: AppointmentsScreen.State,
    modifier: Modifier,
  ) {
    val events = state.eventSink

    NavigationShell(
      destination = NavDestination.Appointments,
      onNavigate = { events(AppointmentsScreen.Event.NavigateTo(it)) },
      onBack = { events(AppointmentsScreen.Event.Back) },
      onBook = { events(AppointmentsScreen.Event.NavigateToBooking) },
      onProfile = { events(AppointmentsScreen.Event.ShowNotice(AppointmentsNotice.Profile)) },
      modifier = modifier,
    ) {
      Column(
        Modifier.weight(1f)
          .fillMaxWidth()
          .padding(
            start = GrowSpacing.md,
            end = GrowSpacing.md,
            top = GrowSpacing.md,
            bottom = GrowSpacing.md,
          )
      ) {
        AppointmentOverview(
          showPast = state.showPast,
          hasTopics = state.topics.isNotBlank(),
          onSelectPast = { events(AppointmentsScreen.Event.SelectPast(it)) },
          onTopics = { events(AppointmentsScreen.Event.EditTopics) },
          onRecap = { events(AppointmentsScreen.Event.NavigateToRecap) },
        )
      }
    }

    state.notice?.let { notice ->
      AppointmentsNoticeDialog(notice, state.topicsDraft, events)
    }
  }
}

@Composable
private fun AppointmentsNoticeDialog(
  notice: AppointmentsNotice,
  topicsDraft: String,
  events: (AppointmentsScreen.Event) -> Unit,
) {
  AlertDialog(
    onDismissRequest = { events(AppointmentsScreen.Event.DismissNotice) },
    title = { GrowText(stringResource(notice.title)) },
    text = {
      if (notice == AppointmentsNotice.Topics) {
        OutlinedTextField(
          topicsDraft,
          { events(AppointmentsScreen.Event.ChangeTopics(it)) },
          label = { GrowText(stringResource(R.string.topics_hint)) },
          minLines = 3,
          maxLines = 8,
        )
      } else {
        GrowText(stringResource(notice.body))
      }
    },
    confirmButton = {
      TextButton({
        events(
          if (notice == AppointmentsNotice.Topics) {
            AppointmentsScreen.Event.SaveTopics
          } else {
            AppointmentsScreen.Event.DismissNotice
          }
        )
      }) {
        GrowText(
          stringResource(
            if (notice == AppointmentsNotice.Topics) {
              R.string.save_draft
            } else {
              R.string.okay
            }
          )
        )
      }
    },
    dismissButton = {
      if (notice == AppointmentsNotice.Topics) {
        TextButton({ events(AppointmentsScreen.Event.DismissNotice) }) {
          GrowText(stringResource(R.string.close))
        }
      }
    },
  )
}
