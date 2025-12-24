package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.CatSearch

@Composable
fun SearchArea(
    modifier: Modifier = Modifier,
    catSearch: CatSearch,
    breedList: List<String>,
    genderList: List<String>,
    ageList: List<String>,
    regionList: List<String> = listOf("Any region"),
    isLoggedIn: Boolean = false,
    currentDistance: Float = 50f,
    onSearchChanged: (CatSearch) -> Unit = {},
    onDistanceChange: (Float) -> Unit = {}
){
    var dialogIsOpen by rememberSaveable { mutableStateOf(false) }

    Card(
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Row {
            ButtonSpinner(
                items = breedList,
                selectedItem = catSearch.breed,
                modifier = Modifier.weight(1f).padding(8.dp),
                itemClick = { onSearchChanged(catSearch.copy(breed = it)) }
            )
            ButtonSpinner(
                items = genderList,
                selectedItem = catSearch.gender,
                modifier = Modifier.weight(1f).padding(8.dp),
                itemClick = { onSearchChanged(catSearch.copy(gender = it)) }
            )
        }

        Row {
            ButtonSpinner(
                items = ageList,
                selectedItem = catSearch.ageRange,
                modifier = Modifier.weight(1f).padding(8.dp),
                itemClick = { onSearchChanged(catSearch.copy(ageRange = it)) }
            )
            ButtonSpinner(
                items = regionList,
                selectedItem = catSearch.region,
                modifier = Modifier.weight(1f).padding(8.dp),
                itemClick = { onSearchChanged(catSearch.copy(region = it)) }
            )
        }

        if (isLoggedIn) {
            Row {
                OutlinedButton(
                    onClick = { dialogIsOpen = true },
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.distance, currentDistance.toInt()),
                        fontSize = 16.sp
                    )
                }
            }
        }

        if (dialogIsOpen) {
            DistanceDialog(
                distance = currentDistance.toInt(),
                dialogIsOpen = dialogIsOpen,
                dialogOpen = { isOpen -> dialogIsOpen = isOpen },
                changeDistance = { newDist -> onDistanceChange(newDist.toFloat()) }
            )
        }
    }
}