package com.jayvijay.growtherapy.ui.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Videocam
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.feature.navigation.NavDestination
import com.jayvijay.growtherapy.ui.atoms.GrowIcon
import com.jayvijay.growtherapy.ui.atoms.GrowImage
import com.jayvijay.growtherapy.ui.atoms.GrowText
import com.jayvijay.growtherapy.ui.controls.GrowIconButton
import com.jayvijay.growtherapy.ui.controls.GrowTonalButton
import com.jayvijay.growtherapy.ui.theme.GrowOpacity
import com.jayvijay.growtherapy.ui.theme.GrowSize
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import kotlin.math.hypot

@Composable
fun GrowNavigationBar(
  destination: NavDestination,
  onMenu: () -> Unit,
  onBack: () -> Unit,
  onBook: () -> Unit,
  onProfile: () -> Unit,
  modifier: Modifier = Modifier,
) {
  if (destination.isRoot) {
    RootNavigationBar(
      destination = destination,
      onMenu = onMenu,
      onBook = onBook,
      onProfile = onProfile,
      modifier = modifier,
    )
  } else {
    NonRootNavigationBar(
      destination = destination,
      onBack = onBack,
      modifier = modifier,
    )
  }
}

@Composable
private fun RootNavigationBar(
  destination: NavDestination,
  onMenu: () -> Unit,
  onBook: () -> Unit,
  onProfile: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val title = stringResource(destination.title)
  val openNavigation = stringResource(R.string.open_navigation)

  Row(
    modifier.fillMaxWidth().heightIn(min = GrowSize.topBar).padding(horizontal = GrowSpacing.md),
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.spacedBy(GrowSpacing.sm),
  ) {
    GrowTonalButton(onMenu, Modifier.semantics { contentDescription = openNavigation }) {
      GrowIcon(Icons.Outlined.Menu, null)
      Spacer(Modifier.width(GrowSpacing.sm))
      GrowText(title, style = MaterialTheme.typography.labelLarge)
    }
    Spacer(Modifier.weight(1f))
    RootTopBarActions(
      destination = destination,
      onBook = onBook,
      onProfile = onProfile,
    )
  }
}

@Composable
private fun RootTopBarActions(
  destination: NavDestination,
  onBook: () -> Unit,
  onProfile: () -> Unit,
) {
  when (destination) {
    NavDestination.Appointments -> {
      GrowIconButton(
        Icons.Outlined.Add,
        stringResource(R.string.new_appointment),
        onBook,
      )
    }

    NavDestination.Messages -> {
      MessagesTopBarActions(onProfile = onProfile)
    }

    NavDestination.Coach -> {
      Surface(
        shape = CircleShape,
        color = MaterialTheme.colorScheme.surfaceContainerHighest,
      ) {
        GrowText(
          stringResource(R.string.session_hour),
          Modifier.padding(
            horizontal = GrowSpacing.md,
            vertical = GrowSpacing.sm,
          ),
          style = MaterialTheme.typography.labelLarge,
        )
      }
    }

    else -> {}
  }
}

@Composable
private fun MessagesTopBarActions(onProfile: () -> Unit) {
  var conversationMenuExpanded by remember { mutableStateOf(false) }

  Box(
    Modifier.size(GrowSize.touchTarget)
      .clip(MaterialTheme.shapes.large)
      .clickable(onClick = onProfile),
    contentAlignment = Alignment.Center,
  ) {
    GrowImage(
      R.drawable.gail_portrait,
      stringResource(R.string.gail),
      Modifier.size(GrowSize.avatar).clip(MaterialTheme.shapes.large),
    )
  }

  Box {
    GrowIconButton(
      Icons.Outlined.MoreVert,
      stringResource(R.string.conversation_options),
      onClick = { conversationMenuExpanded = true },
    )

    DropdownMenu(
      expanded = conversationMenuExpanded,
      onDismissRequest = { conversationMenuExpanded = false },
    ) {
      DropdownMenuItem(
        text = { GrowText(stringResource(R.string.get_help)) },
        onClick = { conversationMenuExpanded = false },
      )
      DropdownMenuItem(
        text = { GrowText(stringResource(R.string.end_chat)) },
        onClick = { conversationMenuExpanded = false },
      )
    }
  }
}

@Composable
private fun NonRootNavigationBar(
  destination: NavDestination,
  onBack: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val title = stringResource(destination.title)
  Box(modifier.fillMaxWidth().heightIn(min = GrowSize.topBar)) {
    GrowIconButton(
      Icons.AutoMirrored.Outlined.ArrowBackIos,
      stringResource(R.string.back),
      onBack,
      Modifier.align(Alignment.CenterStart).padding(start = GrowSpacing.xs),
    )
    Column(
      Modifier.align(Alignment.Center).padding(horizontal = GrowSize.touchTarget),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      GrowText(title, style = MaterialTheme.typography.titleMedium)
      if (destination == NavDestination.Recap) {
        GrowText(
          stringResource(R.string.generated_by_grow),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
      }
    }
  }
}

@Composable
fun GrowNavigationOverlay(
  selected: NavDestination,
  onSelect: (NavDestination) -> Unit,
  onDismiss: () -> Unit,
) {
  Dialog(
    onDismiss,
    properties =
      DialogProperties(
        usePlatformDefaultWidth = false,
        decorFitsSystemWindows = false,
      ),
  ) {
    var revealed by remember { mutableStateOf(false) }
    val revealProgress by
      animateFloatAsState(
        targetValue = if (revealed) 1f else 0f,
        animationSpec = MaterialTheme.motionScheme.slowEffectsSpec(),
        label = "navigationReveal",
      )
    val window = (LocalView.current.parent as? DialogWindowProvider)?.window
    SideEffect {
      window?.setWindowAnimations(0)
      window?.setDimAmount(GrowOpacity.navigationScrim)
      revealed = true
    }
    Box(
      Modifier.fillMaxSize()
        .clickable(
          interactionSource = remember { MutableInteractionSource() },
          indication = null,
          onClick = onDismiss,
        )
        .safeDrawingPadding()
        .padding(horizontal = GrowSpacing.md, vertical = GrowSpacing.sm)
    ) {
      Surface(
        modifier =
          Modifier.width(GrowSize.navigationWidth).drawWithCache {
            val revealPath = Path()
            val fullRadius = hypot(size.width, size.height)
            onDrawWithContent {
              val radius = fullRadius * revealProgress.coerceIn(0f, 1f)
              revealPath.reset()
              revealPath.addOval(Rect(center = Offset.Zero, radius = radius))
              clipPath(revealPath) { this@onDrawWithContent.drawContent() }
            }
          },
        onClick = {},
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainer,
      ) {
        NavigationDrawerContent(
          selected = selected,
          onSelect = onSelect,
          onDismiss = onDismiss,
        )
      }
    }
  }
}

@Composable
private fun NavigationDrawerContent(
  selected: NavDestination,
  onSelect: (NavDestination) -> Unit,
  onDismiss: () -> Unit,
) {
  Column(Modifier.padding(horizontal = 20.dp, vertical = GrowSpacing.sm)) {
    GrowIconButton(
      Icons.Outlined.Close,
      stringResource(R.string.close_navigation),
      onDismiss,
    )
    Spacer(Modifier.size(GrowSpacing.md))
    NavDestination.entries
      .filter { it.isRoot }
      .forEach { destination ->
        NavigationDrawerItem(
          label = {
            GrowText(
              stringResource(destination.title),
              style = MaterialTheme.typography.labelLarge,
            )
          },
          selected = destination == selected,
          onClick = { onSelect(destination) },
          icon = {
            GrowIcon(
              when (destination) {
                NavDestination.Appointments -> Icons.Outlined.Videocam
                NavDestination.Resources -> Icons.Outlined.BookmarkBorder
                else -> Icons.AutoMirrored.Filled.Chat
              },
              null,
            )
          },
          colors =
            NavigationDrawerItemDefaults.colors(
              selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
              unselectedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            ),
        )
      }
    Spacer(Modifier.size(GrowSpacing.sm))
  }
}
