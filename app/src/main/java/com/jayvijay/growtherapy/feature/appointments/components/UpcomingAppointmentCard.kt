package com.jayvijay.growtherapy.feature.appointments.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowButton
import com.jayvijay.growtherapy.ui.portrait.GailPortrait
import com.jayvijay.growtherapy.ui.theme.GrowSize
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun UpcomingAppointmentCard(
  onTopics: () -> Unit,
  modifier: Modifier = Modifier,
  hasTopics: Boolean = false,
) {
  OutlinedCard(
    modifier,
    shape = MaterialTheme.shapes.extraLarge,
    colors = CardDefaults.outlinedCardColors(containerColor = MaterialTheme.colorScheme.surface),
  ) {
    Column(Modifier.fillMaxWidth().padding(GrowSpacing.lg)) {
      Box(Modifier.fillMaxWidth()) {
        GrowText(
          stringResource(R.string.upcoming_date),
          Modifier.padding(end = GrowSpacing.xl, bottom = GrowSpacing.md),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }

      GrowText(
        stringResource(R.string.intake),
        Modifier.semantics { heading() },
        style = MaterialTheme.typography.headlineLarge,
      )

      Spacer(Modifier.height(GrowSpacing.lg))

      GailPortrait(
        contentDescription = stringResource(R.string.gail),
        modifier = Modifier.size(GrowSize.portrait),
      )

      Spacer(Modifier.height(28.dp))

      GrowButton(onTopics, Modifier.fillMaxWidth()) {
        GrowText(
          stringResource(if (hasTopics) R.string.topics_saved else R.string.add_topics),
          style = MaterialTheme.typography.titleMedium,
        )
      }

      Spacer(Modifier.height(GrowSpacing.sm))

      GrowText(
        stringResource(R.string.join_notice),
        Modifier.fillMaxWidth(),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
      )

      Spacer(Modifier.height(GrowSpacing.sm))
    }
  }
}
