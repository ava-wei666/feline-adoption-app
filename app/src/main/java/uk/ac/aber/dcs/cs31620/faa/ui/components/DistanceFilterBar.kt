package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun DistanceFilterBar(
    currentDistance: Float,
    onDistanceChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Search Radius", style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "${currentDistance.roundToInt()} km",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Slider(
                value = currentDistance,
                onValueChange = { newValue ->
                    onDistanceChange(newValue)
                },
                valueRange = 0f..50f,
                steps = 49,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}