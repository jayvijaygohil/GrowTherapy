package com.jayvijay.growtherapy.feature.resources.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowImage
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.theme.GrowContentColors
import com.jayvijay.growtherapy.ui.theme.GrowOpacity
import com.jayvijay.growtherapy.ui.theme.GrowSize
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun FeaturedResourceCard(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Card(
    onClick,
    modifier = modifier.heightIn(min = 368.dp),
    shape = MaterialTheme.shapes.extraLarge,
  ) {
    Box(Modifier.fillMaxWidth()) {
      GrowImage(R.drawable.resource_worry, null, Modifier.matchParentSize())

      Box(
        Modifier.matchParentSize()
          .background(MaterialTheme.colorScheme.scrim.copy(alpha = GrowOpacity.artworkScrim))
      )

      Column(
        Modifier.fillMaxWidth().heightIn(min = 368.dp).padding(GrowSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(GrowSpacing.md),
      ) {
        GrowText(
          stringResource(R.string.handout),
          style = MaterialTheme.typography.labelMedium,
          color = GrowContentColors.onImage,
        )
        GrowText(
          stringResource(R.string.worry_title),
          style = MaterialTheme.typography.headlineLarge,
          color = GrowContentColors.onImage,
        )
        GrowText(stringResource(R.string.worry_subtitle), color = GrowContentColors.onImage)

        Spacer(Modifier.weight(1f))

        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(GrowSpacing.md),
        ) {
          GrowImage(
            R.drawable.gail_portrait,
            null,
            Modifier.size(GrowSize.avatar).clip(MaterialTheme.shapes.large),
          )
          GrowText(
            stringResource(R.string.shared_by),
            style = MaterialTheme.typography.bodyMedium,
            color = GrowContentColors.onImage,
          )
        }
      }
    }
  }
}
