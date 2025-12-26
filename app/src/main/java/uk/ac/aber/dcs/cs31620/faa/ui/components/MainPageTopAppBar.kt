package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageTopAppBar(
    onClick: () -> Unit = {},
    adopterViewModel: AdopterViewModel = viewModel(),
    onProfileClick: () -> Unit = {} // ✅ 确保这里有回调
) {
    val user by adopterViewModel.user.observeAsState()

    CenterAlignedTopAppBar(
        title = { Text(stringResource(id = R.string.app_name)) },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Filled.Menu, contentDescription = stringResource(R.string.nav_drawer_menu))
            }
        },
        actions = {
            if (user != null) {
                IconButton(onClick = onProfileClick) { // ✅ 点击执行跳转
                    Image(
                        painter = painterResource(id = user!!.imageResId),
                        contentDescription = "User Profile",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(32.dp).clip(CircleShape).border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    )
                }
            }
        }
    )
}