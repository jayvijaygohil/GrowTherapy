package com.jayvijay.growtherapy.ui.controls

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.FilledTonalButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun GrowTonalButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable RowScope.() -> Unit,
) {
  FilledTonalButton(
    onClick = onClick,
    modifier = modifier,
    contentPadding = PaddingValues(horizontal = GrowSpacing.md, vertical = GrowSpacing.sm),
    content = content,
  )
}
