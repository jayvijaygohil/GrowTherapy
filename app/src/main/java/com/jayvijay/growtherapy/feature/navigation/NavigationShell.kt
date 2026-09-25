package com.jayvijay.growtherapy.feature.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.jayvijay.growtherapy.ui.navigation.GrowNavigationBar
import com.jayvijay.growtherapy.ui.navigation.GrowNavigationOverlay
import com.jayvijay.growtherapy.ui.theme.GrowSize

@Composable
fun NavigationShell(
  destination: NavDestination,
  onNavigate: (NavDestination) -> Unit,
  onBack: () -> Unit,
  onBook: () -> Unit,
  modifier: Modifier = Modifier,
  onProfile: () -> Unit = {},
  bottomBar: @Composable () -> Unit = {},
  content: @Composable ColumnScope.() -> Unit,
) {
  var navigationOpen by rememberSaveable { mutableStateOf(false) }

  Scaffold(
    modifier = modifier.fillMaxSize().imePadding(),
    contentWindowInsets = WindowInsets.safeDrawing,
  ) { padding ->
    Box(
      Modifier.fillMaxSize().padding(padding).consumeWindowInsets(padding),
      contentAlignment = Alignment.TopCenter,
    ) {
      Column(Modifier.widthIn(max = GrowSize.contentMaxWidth).fillMaxSize()) {
        GrowNavigationBar(
          destination = destination,
          onMenu = { navigationOpen = true },
          onBack = onBack,
          onBook = onBook,
          onProfile = onProfile,
        )

        Column(
          Modifier.weight(1f).fillMaxWidth(),
          content = content,
        )

        bottomBar()
      }
    }
  }

  if (navigationOpen) {
    GrowNavigationOverlay(
      selected = destination,
      onSelect = { selectedDestination ->
        navigationOpen = false
        onNavigate(selectedDestination)
      },
      onDismiss = { navigationOpen = false },
    )
  }
}
