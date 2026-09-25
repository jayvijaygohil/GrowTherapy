package com.jayvijay.growtherapy.ui.conversation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.inputs.MessageComposer
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun ConversationComposer(
  isCoach: Boolean,
  draft: String,
  onDraftChange: (String) -> Unit,
  onSend: () -> Unit,
  onAccessory: () -> Unit,
  onCrisis: () -> Unit,
  modifier: Modifier = Modifier,
) {
  Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
    MessageComposer(
      draft,
      onDraftChange,
      stringResource(if (isCoach) R.string.coach_placeholder else R.string.message_gail),
      onSend,
      onAccessory,
      isCoach = isCoach,
    )

    if (isCoach) {
      GrowText(
        stringResource(R.string.coach_notice),
        Modifier.fillMaxWidth().padding(vertical = GrowSpacing.md),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
      )
    } else {
      TextButton(onCrisis) {
        GrowText(
          stringResource(R.string.emergency),
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
          textAlign = TextAlign.Center,
        )
      }
    }
  }
}
