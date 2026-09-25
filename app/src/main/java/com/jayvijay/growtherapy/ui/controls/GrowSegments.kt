package com.jayvijay.growtherapy.ui.controls

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jayvijay.growtherapy.ui.atoms.GrowText

@Composable
fun GrowSegments(
  labels: List<String>,
  selectedIndex: Int,
  onSelect: (Int) -> Unit,
  modifier: Modifier = Modifier,
) {
  SingleChoiceSegmentedButtonRow(modifier) {
    labels.forEachIndexed { index, label ->
      SegmentedButton(
        selected = index == selectedIndex,
        onClick = { onSelect(index) },
        shape = SegmentedButtonDefaults.itemShape(index, labels.size),
      ) {
        GrowText(label, style = MaterialTheme.typography.labelLarge)
      }
    }
  }
}
