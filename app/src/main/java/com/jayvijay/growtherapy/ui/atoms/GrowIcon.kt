package com.jayvijay.growtherapy.ui.atoms

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.jayvijay.growtherapy.ui.theme.GrowSize

@Composable
fun GrowIcon(
  imageVector: ImageVector,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  tint: Color = LocalContentColor.current,
) {
  Icon(
    imageVector = imageVector,
    contentDescription = contentDescription,
    modifier = modifier.size(GrowSize.icon),
    tint = tint,
  )
}
