package com.jayvijay.growtherapy.ui.cards

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowImage
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowIconButton
import com.jayvijay.growtherapy.ui.theme.GrowSize

@Composable
fun CareListItem(
  title: String,
  overline: String,
  onClick: () -> Unit,
  onOptions: () -> Unit,
  modifier: Modifier = Modifier,
  @DrawableRes thumbnail: Int? = null,
) {
  ListItem(
    overlineContent = {
      GrowText(
        overline,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    },
    modifier = modifier.clickable(onClick = onClick),
    leadingContent =
      thumbnail?.let { image ->
        {
          GrowImage(
            image,
            null,
            Modifier.size(GrowSize.thumbnail).clip(MaterialTheme.shapes.small),
          )
        }
      },
    trailingContent = {
      GrowIconButton(
        Icons.Outlined.MoreVert,
        stringResource(
          if (thumbnail == null) {
            R.string.session_options
          } else {
            R.string.resource_options
          }
        ),
        onOptions,
      )
    },
  ) {
    GrowText(title)
  }
}
