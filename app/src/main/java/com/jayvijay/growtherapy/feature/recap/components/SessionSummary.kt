package com.jayvijay.growtherapy.feature.recap.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowImage
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowIconButton
import com.jayvijay.growtherapy.ui.theme.GrowContentColors
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun SessionSummary(
  onPlay: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier, verticalArrangement = Arrangement.spacedBy(GrowSpacing.lg)) {
    GrowText(
      stringResource(R.string.recap_heading),
      Modifier.semantics { heading() },
      style = MaterialTheme.typography.displaySmall,
    )

    GrowText(stringResource(R.string.recap_body))

    Spacer(Modifier.height(GrowSpacing.xs))

    KeyMoments(onPlay)
  }
}

@Composable
fun KeyMoments(
  onPlay: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Box(modifier.fillMaxWidth().height(340.dp)) {
    GrowImage(
      R.drawable.key_moments_rear,
      null,
      Modifier.matchParentSize()
        .padding(start = GrowSpacing.lg)
        .clip(MaterialTheme.shapes.extraLarge),
    )

    GrowImage(
      R.drawable.key_moments_middle,
      null,
      Modifier.matchParentSize()
        .padding(start = 12.dp, end = 12.dp)
        .clip(MaterialTheme.shapes.extraLarge),
    )

    Box(
      Modifier.matchParentSize().padding(end = GrowSpacing.lg).clip(MaterialTheme.shapes.extraLarge)
    ) {
      GrowImage(R.drawable.key_moments, null, Modifier.matchParentSize())

      Row(
        Modifier.align(Alignment.BottomStart).fillMaxWidth().padding(GrowSpacing.lg),
        verticalAlignment = Alignment.Bottom,
      ) {
        Column(
          Modifier.weight(1f),
          verticalArrangement = Arrangement.spacedBy(GrowSpacing.xs),
        ) {
          GrowText(
            stringResource(R.string.key_moments),
            style = MaterialTheme.typography.headlineSmall,
            color = GrowContentColors.onImage,
          )
          GrowText(
            stringResource(R.string.moment_count),
            color = GrowContentColors.onImage,
          )
        }

        Surface(
          shape = CircleShape,
          color = MaterialTheme.colorScheme.secondaryContainer,
        ) {
          GrowIconButton(
            Icons.Outlined.PlayArrow,
            stringResource(R.string.play_moments),
            onPlay,
          )
        }
      }
    }
  }
}
