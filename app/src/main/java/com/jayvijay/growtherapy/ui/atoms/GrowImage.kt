package com.jayvijay.growtherapy.ui.atoms

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun GrowImage(
  @DrawableRes resource: Int,
  contentDescription: String?,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.Crop,
) {
  Image(
    painter = painterResource(resource),
    contentDescription = contentDescription,
    modifier = modifier,
    contentScale = contentScale,
  )
}
