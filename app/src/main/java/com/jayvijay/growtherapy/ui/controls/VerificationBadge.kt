package com.jayvijay.growtherapy.ui.controls

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.theme.GrowContentColors
import com.jayvijay.growtherapy.ui.theme.GrowSpacing

@Composable
fun VerificationBadge(modifier: Modifier = Modifier) {
  Surface(
    modifier = modifier,
    shape = CircleShape,
    color = GrowContentColors.verification,
    contentColor = GrowContentColors.onVerification,
  ) {
    GrowText(
      stringResource(R.string.verified),
      Modifier.padding(horizontal = GrowSpacing.md, vertical = GrowSpacing.sm),
      style = MaterialTheme.typography.labelMedium,
    )
  }
}
