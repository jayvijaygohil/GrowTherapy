package com.jayvijay.growtherapy.ui.inputs

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowIcon
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowIconButton

@Composable
fun ResourceSearch(
  value: String,
  onValueChange: (String) -> Unit,
  onVoice: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val description = stringResource(R.string.search_resources)

  TextField(
    value = value,
    onValueChange = onValueChange,
    modifier = modifier.fillMaxWidth().semantics { contentDescription = description },
    placeholder = { GrowText(description) },
    leadingIcon = { GrowIcon(Icons.Outlined.Search, null) },
    trailingIcon = {
      if (value.isEmpty()) {
        GrowIconButton(Icons.Outlined.Mic, stringResource(R.string.voice), onVoice)
      } else {
        GrowIconButton(
          Icons.Outlined.Close,
          stringResource(R.string.clear_search),
          { onValueChange("") },
        )
      }
    },
    shape = CircleShape,
    colors =
      TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
      ),
    singleLine = true,
  )
}
