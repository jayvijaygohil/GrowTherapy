package com.jayvijay.growtherapy.feature.booking.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.feature.booking.components.BookingAction
import com.jayvijay.growtherapy.feature.booking.components.BookingCalendar
import com.jayvijay.growtherapy.feature.booking.components.ProviderOverview
import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.jayvijay.growtherapy.feature.navigation.NavigationShell
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import com.slack.circuit.codegen.annotations.CircuitInject
import com.slack.circuit.runtime.ui.Ui
import dagger.hilt.components.SingletonComponent
import java.time.LocalDate
import java.time.ZoneOffset
import javax.inject.Inject

private const val INITIAL_CALENDAR_YEAR = 2026
private const val INITIAL_CALENDAR_MONTH = 6
private const val INITIAL_CALENDAR_DAY = 9

@CircuitInject(BookingScreen::class, SingletonComponent::class)
class BookingUi @Inject constructor() : Ui<BookingScreen.State> {
  @OptIn(ExperimentalMaterial3Api::class)
  @Composable
  override fun Content(
    state: BookingScreen.State,
    modifier: Modifier,
  ) {
    val events = state.eventSink

    val calendar =
      rememberDatePickerState(
        initialSelectedDateMillis =
          LocalDate.of(INITIAL_CALENDAR_YEAR, INITIAL_CALENDAR_MONTH, INITIAL_CALENDAR_DAY)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
      )

    NavigationShell(
      destination = NavDestination.Booking,
      onNavigate = { events(BookingScreen.Event.NavigateTo(it)) },
      onBack = { events(BookingScreen.Event.Back) },
      onBook = { events(BookingScreen.Event.ShowNotice(BookingNotice.Booking)) },
      onProfile = { events(BookingScreen.Event.ShowNotice(BookingNotice.Profile)) },
      modifier = modifier,
      bottomBar = {
        BookingAction(
          dateMillis = calendar.selectedDateMillis,
          isVirtual = state.isVirtual,
          onBook = { events(BookingScreen.Event.ShowNotice(BookingNotice.Booking)) },
          modifier = Modifier.padding(GrowSpacing.md),
        )
      },
    ) {
      Column(
        Modifier.weight(1f)
          .fillMaxWidth()
          .verticalScroll(rememberScrollState())
          .padding(
            start = GrowSpacing.md,
            end = GrowSpacing.md,
            top = GrowSpacing.md,
            bottom = GrowSpacing.md,
          )
      ) {
        ProviderOverview(
          onProfile = { events(BookingScreen.Event.ShowNotice(BookingNotice.Profile)) }
        )
        Spacer(Modifier.height(GrowSpacing.xl))
        BookingCalendar(
          state = calendar,
          isVirtual = state.isVirtual,
          onSelectVirtual = { events(BookingScreen.Event.SelectVirtual(it)) },
        )
      }
    }

    state.notice?.let { notice ->
      BookingNoticeDialog(notice, events)
    }
  }
}

@Composable
private fun BookingNoticeDialog(
  notice: BookingNotice,
  events: (BookingScreen.Event) -> Unit,
) {
  AlertDialog(
    onDismissRequest = { events(BookingScreen.Event.DismissNotice) },
    title = { GrowText(stringResource(notice.title)) },
    text = { GrowText(stringResource(notice.body)) },
    confirmButton = {
      TextButton({ events(BookingScreen.Event.DismissNotice) }) {
        GrowText(stringResource(R.string.okay))
      }
    },
  )
}
