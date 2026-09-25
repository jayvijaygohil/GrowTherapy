package com.jayvijay.growtherapy.ui.controls

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jayvijay.growtherapy.ui.theme.GrowSize

@Composable
fun GrowButton(
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  enabled: Boolean = true,
  content: @Composable RowScope.() -> Unit,
) {
  Button(
    onClick = onClick,
    modifier = modifier.heightIn(min = GrowSize.action),
    enabled = enabled,
    content = content,
  )
}
