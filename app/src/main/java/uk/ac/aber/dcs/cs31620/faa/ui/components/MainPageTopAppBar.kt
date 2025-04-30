package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme

@Composable
fun MainPageTopAppBar(
    onClick: () -> Unit = {}
){
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        /*colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            navigationIconContentColor =
                MaterialTheme.colorScheme.onPrimary
        ),*/
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription =
                        stringResource(R.string.nav_drawer_menu)
                )
            }
        }
    )
}

@Composable
@Preview
fun MainPageTopAppBarPreview(){
    FAATheme(dynamicColor = false) {
        MainPageTopAppBar()
    }
}