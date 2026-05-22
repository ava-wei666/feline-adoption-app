package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.faa.R
import uk.ac.aber.dcs.cs31620.faa.model.Cat
import androidx.core.net.toUri

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CatCard(
    modifier: Modifier = Modifier,
    cat: Cat,
    selectAction: (Cat) -> Unit = {}
){
    Card(modifier = modifier.fillMaxSize()) {
        ConstraintLayout {
            val (imageRef, nameRef) = createRefs()

            GlideImage(
                model = cat.imagePath.toUri(),
                contentDescription = stringResource(R.string.cat_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .height(190.dp)
                    .padding(4.dp)
                    .constrainAs(imageRef) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .clickable { selectAction(cat) }
            )

            Text(
                text = cat.name,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(8.dp)
                    .constrainAs(nameRef) {
                        start.linkTo(parent.start)
                        top.linkTo(imageRef.bottom)
                        bottom.linkTo(parent.bottom)
                    }
            )
        }
    }
}