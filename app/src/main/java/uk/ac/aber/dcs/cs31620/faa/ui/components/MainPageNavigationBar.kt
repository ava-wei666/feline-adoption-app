package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Pets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import uk.ac.aber.dcs.cs31620.faa.R

@Composable
fun MainPageNavigationBar() {
    var homeSelected = rememberSaveable { mutableStateOf(true) }
    NavigationBar {
        // Home tab. We could use a loop but for two items seems overkill
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = (
                            if (homeSelected.value)
                                Icons.Filled.Home
                            else
                                Icons.Outlined.Home
                            ),
                    contentDescription = stringResource(id = R.string.home)
                )
            },
            label = { Text(stringResource(id = R.string.home)) },
            selected = homeSelected.value,
            onClick = { homeSelected.value = true }
        )

        // Cats tab
        NavigationBarItem(
            icon = {
                Icon(
                    imageVector = (
                            if (homeSelected.value)
                                Icons.Filled.Pets
                            else
                                Icons.Outlined.Pets
                            ),
                    contentDescription = stringResource(id = R.string.cats)
                )
            },
            label = { Text(stringResource(id = R.string.cats)) },
            selected = !homeSelected.value,
            onClick = { homeSelected.value = false }
        )
    }
}