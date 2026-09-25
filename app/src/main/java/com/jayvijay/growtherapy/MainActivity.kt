package com.jayvijay.growtherapy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.jayvijay.growtherapy.feature.appointments.presentation.AppointmentsScreen
import com.jayvijay.growtherapy.ui.theme.GrowTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.Circuit
import com.slack.circuit.foundation.CircuitCompositionLocals
import com.slack.circuit.foundation.NavigableCircuitContent
import com.slack.circuit.foundation.rememberCircuitNavigator
import com.slack.circuit.sharedelements.SharedElementTransitionLayout
import com.slack.circuitx.android.rememberAndroidScreenAwareNavigator
import com.slack.circuitx.gesturenavigation.GestureNavigationDecorationFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

  @Inject lateinit var circuit: Circuit

  @OptIn(ExperimentalSharedTransitionApi::class)
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    setContent {
      GrowTheme {
        CircuitCompositionLocals(circuit) {
          val backStack = rememberSaveableBackStack(root = AppointmentsScreen)
          val navigator = rememberCircuitNavigator(backStack) { finish() }
          val androidNavigator = rememberAndroidScreenAwareNavigator(navigator, this@MainActivity)

          val decoratorFactory =
            remember(circuit) {
              GestureNavigationDecorationFactory(fallback = circuit.animatedNavDecoratorFactory)
            }

          SharedElementTransitionLayout(modifier = Modifier.fillMaxSize()) {
            NavigableCircuitContent(
              navigator = androidNavigator,
              backStack = backStack,
              decoratorFactory = decoratorFactory,
            )
          }
        }
      }
    }
  }
}
