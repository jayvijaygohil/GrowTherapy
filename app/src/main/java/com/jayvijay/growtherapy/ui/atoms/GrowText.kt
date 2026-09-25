package com.jayvijay.growtherapy.ui.atoms

import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign

@Composable
fun GrowText(
  text: String,
  modifier: Modifier = Modifier,
  style: TextStyle = MaterialTheme.typography.bodyLarge,
  color: Color = LocalContentColor.current,
  textAlign: TextAlign? = null,
) {
  Text(
    text = text,
    modifier = modifier,
    style = style,
    color = color,
    textAlign = textAlign,
  )
}
