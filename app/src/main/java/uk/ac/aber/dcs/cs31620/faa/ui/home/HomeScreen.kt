package uk.ac.aber.dcs.cs31620.faa.ui.home

import android.app.Application
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.datasource.FaaRepository
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import uk.ac.aber.dcs.cs31620.faa.model.CatsViewModel
import uk.ac.aber.dcs.cs31620.faa.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.faa.ui.theme.FAATheme
import java.time.LocalDateTime
import kotlin.collections.get
import kotlin.random.Random
import androidx.core.net.toUri
import uk.ac.aber.dcs.cs31620.faa.model.AdopterViewModel
import androidx.navigation.NavHostController


@Composable
fun HomeScreenTopLevel(
    navController: NavHostController,
    catsViewModel: CatsViewModel = viewModel(),
    adopterViewModel: AdopterViewModel
)
{
    val recentCats by catsViewModel.recentCats.observeAsState(listOf())

    HomeScreen(
        navController = navController,
        recentCats = recentCats,
        adopterViewModel = adopterViewModel
    )
}

/**
 * Represents the home screen. For this version we only have a
 * top app bar, navigation bar and empty content.
 * @author Chris Loftus
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    recentCats: List<Cat>,
    adopterViewModel: AdopterViewModel
) {
    val coroutineScope = rememberCoroutineScope()
    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        adopterViewModel = adopterViewModel
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            HomeScreenContent(modifier = Modifier.padding(8.dp), recentCats)
        }
    }
}

@Composable
private fun HomeScreenContent(
    modifier: Modifier = Modifier,
    recentCats: List<Cat>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.home_picture),
            contentDescription = stringResource(R.string.home_picture_image),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.welcome),
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(R.string.featured_cat_title),
            fontSize = 18.sp
        )

        FeaturedCat(Modifier.fillMaxWidth(), recentCats)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun FeaturedCat(
    modifier: Modifier = Modifier,
    recentCats: List<Cat>
) {
    if (recentCats.isNotEmpty()){
        val catPos = Random.nextInt(recentCats.size)
        val catImage = recentCats[catPos].imagePath

        if (catImage.isNotEmpty()) {
            GlideImage(
                model = catImage.toUri(),
                contentDescription = stringResource(R.string.featured_cat_description),
                contentScale = ContentScale.Crop,
                modifier = modifier
            )
        }
    }

}


@Composable
@Preview
fun HomeScreenPreview(){
    FAATheme(dynamicColor = false) {
        val navController = rememberNavController()
        HomeScreen(navController = navController, listOf(),
        adopterViewModel = androidx.lifecycle.viewmodel.compose.viewModel())
    }
}