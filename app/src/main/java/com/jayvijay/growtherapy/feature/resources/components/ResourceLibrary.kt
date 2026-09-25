package com.jayvijay.growtherapy.feature.resources.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.cards.CareListItem
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

private val ResourcePlaceholderHeight = 368.dp

@Composable
fun ResourceLibrary(
  query: String,
  onResource: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val worry = stringResource(R.string.worry_title)
  val breathing = stringResource(R.string.breathing)
  val anxiety = stringResource(R.string.anxiety)

  val search = query.trim()

  val showWorry = worry.replace('\n', ' ').contains(search, ignoreCase = true)
  val showBreathing = breathing.contains(search, ignoreCase = true)
  val showAnxiety = anxiety.contains(search, ignoreCase = true)

  Column(modifier, verticalArrangement = Arrangement.spacedBy(GrowSpacing.lg)) {
    if (showWorry) {
      SelectedForYouSection(onResource = onResource)
    }

    if (showBreathing || showAnxiety) {
      JumpBackSection(
        showBreathing = showBreathing,
        showAnxiety = showAnxiety,
        breathing = breathing,
        anxiety = anxiety,
        onResource = onResource,
      )
    }

    if (!showWorry && !showBreathing && !showAnxiety) {
      GrowText(
        stringResource(R.string.no_resources),
        Modifier.padding(horizontal = GrowSpacing.md),
      )
    }
  }
}

@Composable
private fun SelectedForYouSection(onResource: () -> Unit) {
  Column {
    GrowText(
      stringResource(R.string.selected_for_you),
      Modifier.padding(horizontal = GrowSpacing.md).semantics { heading() },
      style = MaterialTheme.typography.titleLarge,
    )

    BoxWithConstraints(Modifier.fillMaxWidth()) {
      val cardWidth = maxWidth - GrowSpacing.section

      LazyRow(
        contentPadding = PaddingValues(horizontal = GrowSpacing.md),
        horizontalArrangement = Arrangement.spacedBy(GrowSpacing.md),
      ) {
        item { FeaturedResourceCard(onResource, Modifier.width(cardWidth)) }
        item {
          Surface(
            Modifier.width(cardWidth).height(ResourcePlaceholderHeight),
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHighest,
          ) {}
        }
      }
    }

    Spacer(Modifier.height(GrowSpacing.sm))
  }
}

@Composable
private fun JumpBackSection(
  showBreathing: Boolean,
  showAnxiety: Boolean,
  breathing: String,
  anxiety: String,
  onResource: () -> Unit,
) {
  Column(Modifier.padding(horizontal = GrowSpacing.md)) {
    GrowText(
      stringResource(R.string.jump_back),
      Modifier.semantics { heading() },
      style = MaterialTheme.typography.titleLarge,
    )

    Spacer(Modifier.height(GrowSpacing.md))

    if (showBreathing) {
      CareListItem(
        breathing,
        stringResource(R.string.exercise_by),
        onResource,
        onResource,
        thumbnail = R.drawable.resource_breathing,
      )
    }

    if (showAnxiety) {
      CareListItem(
        anxiety,
        stringResource(R.string.handout_by),
        onResource,
        onResource,
        thumbnail = R.drawable.resource_anxiety,
      )
    }
  }
}
