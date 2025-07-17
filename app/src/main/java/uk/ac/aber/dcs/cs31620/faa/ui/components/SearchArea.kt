package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.layout.Row
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

/**
 * Displays a search area consisting of two rows of filter
 * dropdown buttons.
 * Row 1 has filters for breed and gender.
 * Row 2 has a filter for age and an outline button for the search distance.
 * @param modifier To configure the search Card
 * @param breedList The list of breeds to display in the dropdown
 * @param updateBreed The lambda to run if a breed is selected
 * @param genderList The list of genders to display in the dropdown
 * @param updateGender The lambda to run if a gender is selected
 * @param ageList The list of age ranges to display in the dropdown
 * @param updateAge The lambda to run if an age range is selected
 * @param proximity The distance an adopter is willing to travel
 * @param updateProximity The lambda to run if the distance is changed
 */
@Composable
fun SearchArea(
    modifier: Modifier = Modifier,
    catSearch: CatSearch,
    breedList: List<String>,
    genderList: List<String>,
    ageList: List<String>,
    updateSearch: (CatSearch) -> Unit = {}
){
    var dialogIsOpen by rememberSaveable {mutableStateOf(false)}
    Card(
        shape = RectangleShape,
        elevation =
            CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
    ) {
        Row {
            ButtonSpinner(
                items = breedList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                itemClick = {
                    updateSearch(
                        CatSearch(
                            breed = it,
                            gender = catSearch.gender,
                            ageRange = catSearch.ageRange,
                            distance = catSearch.distance
                        )
                    )
                }
            )

            ButtonSpinner(
                items = genderList,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp, end = 8.dp),
                itemClick = {
                    updateSearch(
                        CatSearch(
                            breed = catSearch.breed,
                            gender = it,
                            ageRange = catSearch.ageRange,
                            distance = catSearch.distance
                        )
                    )
                }
            )
        }

        // Second row
        Row {
            ButtonSpinner(
                items = ageList,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                itemClick = {
                    updateSearch(
                        CatSearch(
                            breed = catSearch.breed,
                            gender = catSearch.gender,
                            ageRange = it,
                            distance = catSearch.distance
                        )
                    )
                }
            )

            OutlinedButton(
                onClick = {
                    // To be defined in section 2
                    dialogIsOpen = true
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, bottom = 8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.distance, catSearch.distance),
                    fontSize = 16.sp
                )
            }
            DistanceDialog(
                distance = catSearch.distance,
                dialogIsOpen = dialogIsOpen,
                dialogOpen = { isOpen ->
                    dialogIsOpen = isOpen
                },
                changeDistance = {
                    updateSearch(
                        CatSearch(
                            breed = catSearch.breed,
                            gender = catSearch.gender,
                            ageRange = catSearch.ageRange,
                            distance = it
                        )
                    )
                }
            )
        }
    }
}