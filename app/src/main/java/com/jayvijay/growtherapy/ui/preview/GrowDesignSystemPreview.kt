package com.jayvijay.growtherapy.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowImage
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowButton
import com.jayvijay.growtherapy.ui.controls.GrowSegments
import com.jayvijay.growtherapy.ui.controls.GrowTonalButton
import com.jayvijay.growtherapy.ui.controls.VerificationBadge
import com.jayvijay.growtherapy.ui.theme.GrowSize
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import com.jayvijay.growtherapy.ui.theme.GrowTheme

@Preview(name = "Grow foundations and controls", widthDp = 412, heightDp = 916)
@Preview(
  name = "Grow foundations and controls — dark",
  widthDp = 412,
  heightDp = 916,
  uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun GrowDesignSystemPreview() {
  GrowTheme {
    Surface {
      Column(
        Modifier.padding(GrowSpacing.lg),
        verticalArrangement = Arrangement.spacedBy(GrowSpacing.md),
      ) {
        GrowImage(
          R.drawable.grow_logo,
          "Grow Therapy",
          Modifier.size(GrowSize.providerPortrait),
        )
        GrowText("Care, in a quieter frame.", style = MaterialTheme.typography.displaySmall)
        GrowText("A little support, every day.")

        GrowButton(onClick = {}) {
          GrowText(
            "Continue",
            style = MaterialTheme.typography.labelLarge,
          )
        }
        GrowButton(onClick = {}, enabled = false) {
          GrowText(
            "Disabled",
            style = MaterialTheme.typography.labelLarge,
          )
        }
        GrowTonalButton(onClick = {}) {
          GrowText(
            "Supporting action",
            style = MaterialTheme.typography.labelLarge,
          )
        }

        OutlinedTextField("Thank you for today.", {}, label = { GrowText("Message Gail") })
        GrowSegments(listOf("Upcoming", "Past"), 0, {})
        VerificationBadge()
      }
    }
  }
}
