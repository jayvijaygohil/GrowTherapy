package com.jayvijay.growtherapy.ui.inputs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Mic
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowIcon
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowIconButton
import com.jayvijay.growtherapy.ui.theme.GrowSize
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun MessageComposer(
  value: String,
  onValueChange: (String) -> Unit,
  placeholder: String,
  onSend: () -> Unit,
  onAccessory: () -> Unit,
  modifier: Modifier = Modifier,
  isCoach: Boolean = false,
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(GrowSpacing.sm),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    OutlinedTextField(
      value = value,
      onValueChange = onValueChange,
      modifier = Modifier.weight(1f).semantics { contentDescription = placeholder },
      placeholder = { GrowText(placeholder) },
      trailingIcon = {
        GrowIconButton(
          icon = if (isCoach) Icons.Outlined.Mic else Icons.Outlined.Add,
          description = stringResource(if (isCoach) R.string.voice else R.string.attach),
          onClick = onAccessory,
        )
      },
      maxLines = 4,
      shape = CircleShape,
      keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default),
    )

    FilledTonalIconButton(
      onClick = onSend,
      modifier = Modifier.size(GrowSize.touchTarget),
    ) {
      GrowIcon(
        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
        contentDescription = stringResource(R.string.send),
      )
    }
  }
}
