package com.jayvijay.growtherapy.ui.atoms

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GrowDivider(modifier: Modifier = Modifier) {
  HorizontalDivider(
    modifier = modifier,
    color = MaterialTheme.colorScheme.outlineVariant,
  )
}
