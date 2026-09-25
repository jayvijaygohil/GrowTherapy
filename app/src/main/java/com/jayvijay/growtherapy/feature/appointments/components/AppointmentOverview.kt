package com.jayvijay.growtherapy.feature.appointments.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import com.jayvijay.growtherapy.R
import com.jayvijay.growtherapy.ui.cards.CareListItem
import com.jayvijay.growtherapy.ui.controls.GrowSegments
import com.jayvijay.growtherapy.ui.theme.GrowSpacing
import kotlin.math.roundToInt

private const val DATE_CYCLE_COUNT = 3
private const val SESSION_ITEM_COUNT = 30

@Composable
fun AppointmentOverview(
  showPast: Boolean,
  hasTopics: Boolean,
  onSelectPast: (Boolean) -> Unit,
  onTopics: () -> Unit,
  onRecap: () -> Unit,
  modifier: Modifier = Modifier,
) {
  val upcomingListState = rememberLazyListState()
  val pastListState = rememberLazyListState()

  var cardHeightPx by remember { mutableFloatStateOf(0f) }
  var cardOffsetPx by remember { mutableFloatStateOf(0f) }

  val nestedScrollConnection = remember {
    object : NestedScrollConnection {
      override fun onPreScroll(
        available: Offset,
        source: NestedScrollSource,
      ): Offset {
        if (available.y >= 0f) {
          return Offset.Zero
        }

        val previousOffset = cardOffsetPx
        cardOffsetPx = (previousOffset + available.y).coerceIn(-cardHeightPx, 0f)
        return Offset(x = 0f, y = cardOffsetPx - previousOffset)
      }

      override fun onPostScroll(
        consumed: Offset,
        available: Offset,
        source: NestedScrollSource,
      ): Offset {
        if (available.y <= 0f) {
          return Offset.Zero
        }

        val previousOffset = cardOffsetPx
        cardOffsetPx = (previousOffset + available.y).coerceIn(-cardHeightPx, 0f)
        return Offset(x = 0f, y = cardOffsetPx - previousOffset)
      }
    }
  }

  Column(modifier.fillMaxSize().nestedScroll(nestedScrollConnection)) {
    UpcomingAppointmentCard(
      onTopics,
      Modifier.fillMaxWidth()
        .graphicsLayer {
          alpha =
            if (cardHeightPx > 0f) {
              (1f + cardOffsetPx / cardHeightPx).coerceIn(0f, 1f)
            } else {
              1f
            }
        }
        .clipToBounds()
        .layout { measurable, constraints ->
          val card =
            measurable.measure(constraints.copy(minHeight = 0, maxHeight = Constraints.Infinity))
          val newHeight = card.height.toFloat()
          if (cardHeightPx != newHeight) {
            val collapsedFraction = if (cardHeightPx > 0f) -cardOffsetPx / cardHeightPx else 0f
            cardHeightPx = newHeight
            cardOffsetPx = -newHeight * collapsedFraction
          }
          val offset = cardOffsetPx.roundToInt()
          layout(card.width, (card.height + offset).coerceAtLeast(0)) {
            card.placeRelative(0, offset)
          }
        }
        .padding(bottom = GrowSpacing.lg),
      hasTopics,
    )
    GrowSegments(
      listOf(stringResource(R.string.upcoming), stringResource(R.string.past)),
      if (showPast) 1 else 0,
      { onSelectPast(it == 1) },
      Modifier.fillMaxWidth(),
    )
    AppointmentSegmentContent(
      showPast = showPast,
      upcomingListState = upcomingListState,
      pastListState = pastListState,
      onRecap = onRecap,
    )
  }
}

@Composable
private fun AppointmentSegmentContent(
  showPast: Boolean,
  upcomingListState: LazyListState,
  pastListState: LazyListState,
  onRecap: () -> Unit,
) {
  val motionScheme = MaterialTheme.motionScheme
  AnimatedContent(
    targetState = showPast,
    modifier = Modifier.fillMaxSize().clipToBounds(),
    transitionSpec = {
      val direction = if (targetState) 1 else -1
      (slideInHorizontally(animationSpec = motionScheme.defaultSpatialSpec()) { width ->
        direction * width
      } + fadeIn(animationSpec = motionScheme.defaultEffectsSpec())) togetherWith
        (slideOutHorizontally(animationSpec = motionScheme.fastSpatialSpec()) { width ->
          -direction * width
        } + fadeOut(animationSpec = motionScheme.fastEffectsSpec()))
    },
    label = "appointmentSegment",
  ) { pastSelected ->
    LazyColumn(
      modifier = Modifier.fillMaxSize(),
      state = if (pastSelected) pastListState else upcomingListState,
    ) {
      items(
        count = SESSION_ITEM_COUNT,
        key = { it },
        contentType = { "historical_session" },
      ) { index ->
        val date =
          when (index % DATE_CYCLE_COUNT) {
            0 -> R.string.may_31
            1 -> R.string.may_14
            else -> R.string.may_2
          }

        CareListItem(
          stringResource(R.string.session_gail),
          stringResource(date),
          onRecap,
          onRecap,
          Modifier.fillMaxWidth(),
        )
      }
    }
  }
}
