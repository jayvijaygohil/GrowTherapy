package com.jayvijay.growtherapy.ui.portrait

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.atoms.GrowImage
import com.slack.circuit.sharedelements.SharedElementTransitionScope
import com.slack.circuit.sharedelements.SharedElementTransitionScope.AnimatedScope.Navigation

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun GailPortrait(
  contentDescription: String?,
  modifier: Modifier = Modifier,
) {
  if (SharedElementTransitionScope.isAvailable) {
    SharedElementTransitionScope {
      val navigationScope = findAnimatedScope(Navigation)

      val portraitModifier =
        if (navigationScope != null) {
          modifier.sharedElement(
            sharedContentState =
              rememberSharedContentState(key = "appointment-booking:gail-portrait"),
            animatedVisibilityScope = navigationScope,
          )
        } else {
          modifier
        }

      GrowImage(
        R.drawable.gail_portrait,
        contentDescription,
        portraitModifier.clip(MaterialTheme.shapes.large),
      )
    }
  } else {
    GrowImage(
      R.drawable.gail_portrait,
      contentDescription,
      modifier.clip(MaterialTheme.shapes.large),
    )
  }
}
