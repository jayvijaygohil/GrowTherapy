package com.jayvijay.growtherapy.feature.booking.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowButton
import com.jayvijay.growtherapy.ui.controls.GrowSegments
import com.jayvijay.growtherapy.ui.controls.VerificationBadge
import com.jayvijay.growtherapy.ui.portrait.GailPortrait
import com.jayvijay.growtherapy.ui.theme.GrowSize
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Composable
fun ProviderOverview(
  onProfile: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Row(modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(GrowSpacing.md)) {
    GailPortrait(
      contentDescription = null,
      modifier = Modifier.size(GrowSize.providerPortrait),
    )
    Column(Modifier.weight(1f)) {
      FlowRow(
        horizontalArrangement = Arrangement.spacedBy(GrowSpacing.sm),
        verticalArrangement = Arrangement.Center,
      ) {
        GrowText(
          stringResource(R.string.gail),
          style = MaterialTheme.typography.headlineSmall,
        )
        GrowText(
          stringResource(R.string.pronouns),
          Modifier.padding(top = GrowSpacing.sm),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
      VerificationBadge()
      TextButton(onProfile) {
        GrowText(
          stringResource(R.string.view_profile),
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingCalendar(
  state: DatePickerState,
  isVirtual: Boolean,
  onSelectVirtual: (Boolean) -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier) {
    GrowText(
      stringResource(R.string.select_day),
      Modifier.semantics { heading() },
      style = MaterialTheme.typography.titleLarge,
    )
    GrowSegments(
      listOf(stringResource(R.string.virtual), stringResource(R.string.in_person)),
      if (isVirtual) 0 else 1,
      { onSelectVirtual(it == 0) },
      Modifier.fillMaxWidth(),
    )
    Spacer(Modifier.height(GrowSpacing.md))
    DatePicker(
      state,
      modifier = Modifier.fillMaxWidth(),
      title = null,
      headline = null,
      showModeToggle = false,
      colors = DatePickerDefaults.colors(containerColor = MaterialTheme.colorScheme.surface),
    )
  }
}

@Composable
fun BookingAction(
  dateMillis: Long?,
  isVirtual: Boolean,
  onBook: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val locale = LocalConfiguration.current.locales[0]

  val date =
    dateMillis
      ?.let {
        Instant.ofEpochMilli(it)
          .atZone(ZoneOffset.UTC)
          .format(DateTimeFormatter.ofPattern("MMMM d", locale))
      }
      .orEmpty()

  Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(GrowSpacing.sm)) {
    Row(
      Modifier.fillMaxWidth().padding(horizontal = GrowSpacing.sm),
      horizontalArrangement = Arrangement.spacedBy(GrowSpacing.md),
      verticalAlignment = Alignment.CenterVertically,
    ) {
      GrowText(stringResource(R.string.price), style = MaterialTheme.typography.displaySmall)
      GrowText(
        stringResource(
          R.string.booking_summary,
          stringResource(if (isVirtual) R.string.virtual else R.string.in_person),
          date,
        ),
        Modifier.weight(1f),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.End,
      )
    }

    Spacer(Modifier.height(GrowSpacing.xs))

    GrowButton(onBook, Modifier.fillMaxWidth(), enabled = dateMillis != null) {
      GrowText(
        stringResource(R.string.book_now),
        style = MaterialTheme.typography.titleMedium,
      )
    }

    GrowText(
      stringResource(R.string.estimated_cost),
      Modifier.fillMaxWidth(),
      style = MaterialTheme.typography.bodySmall,
      color = MaterialTheme.colorScheme.onSurfaceVariant,
      textAlign = TextAlign.Center,
    )
  }
}
