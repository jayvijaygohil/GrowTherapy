package com.jayvijay.growtherapy.ui.controls

import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.jayvijay.growtherapy.ui.atoms.GrowIcon
import com.jayvijay.growtherapy.ui.theme.GrowSize

@Composable
fun GrowIconButton(
  icon: ImageVector,
  description: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
) {
  IconButton(
    onClick = onClick,
    modifier = modifier.size(GrowSize.touchTarget),
    enabled = enabled,
  ) {
    GrowIcon(imageVector = icon, contentDescription = description)
  }
}
