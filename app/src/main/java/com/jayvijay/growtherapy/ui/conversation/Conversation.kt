package com.jayvijay.growtherapy.ui.conversation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowDivider
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.cards.MessageBubble
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun Conversation(
  isCoach: Boolean,
  modifier: Modifier = Modifier,
) {
  Column(modifier, verticalArrangement = Arrangement.spacedBy(GrowSpacing.md)) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(GrowSpacing.sm),
    ) {
      GrowDivider(Modifier.weight(1f))
      GrowText(
        stringResource(R.string.timestamp),
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
      GrowDivider(Modifier.weight(1f))
    }

    GrowText(stringResource(if (isCoach) R.string.coach_greeting else R.string.welcome_message))

    MessageBubble(
      stringResource(if (isCoach) R.string.coach_reply else R.string.thanks_message),
      Modifier.align(Alignment.End).padding(start = GrowSpacing.section),
    )

    if (isCoach) {
      GrowText(stringResource(R.string.coach_response))
    }
  }
}
