package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.ui.Modifier

/**
 * Represents a top app bar component using M3 CenterAlignedTopAppBar.
 * Has a menu button icon and the app name.
 * @param onClick: provides the behaviour for the menu icon
 * @author Chris Loftus
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageTopAppBar(
    onClick: () -> Unit = {},
    adopterViewModel: AdopterViewModel = viewModel()
) {
    val user by adopterViewModel.user.observeAsState()

    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = stringResource(R.string.nav_drawer_menu)
                )
            }
        },
        actions = {
            if (user != null) {
                IconButton(onClick = { /* switch to the personal center */ }) {
                    Image(
                        painter = painterResource(id = user!!.imageResId),
                        contentDescription = "User Avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                }
            }
        }
    )
}

@Composable
@Preview
fun MainPageTopAppBarPreview() {
    FAATheme(dynamicColor = false) {
        MainPageTopAppBar()
    }
}