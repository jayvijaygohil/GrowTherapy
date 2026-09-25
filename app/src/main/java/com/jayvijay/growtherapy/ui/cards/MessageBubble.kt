package com.jayvijay.growtherapy.ui.cards

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun MessageBubble(
  text: String,
  modifier: Modifier = Modifier,
) {
  Card(
    modifier,
    shape = MaterialTheme.shapes.extraLarge,
    colors =
      CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
      ),
  ) {
    GrowText(text, Modifier.padding(horizontal = GrowSpacing.md, vertical = GrowSpacing.md))
  }
}
