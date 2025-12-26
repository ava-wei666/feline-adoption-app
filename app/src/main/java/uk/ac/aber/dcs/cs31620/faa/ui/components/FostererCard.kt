package uk.ac.aber.dcs.cs31620.faa.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import uk.ac.aber.dcs.cs31620.faa.model.Fosterer

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun FostererCard(
    fosterer: Fosterer,
    modifier: Modifier = Modifier,
    distance: String = "", // 新增距离参数
    selectAction: (Fosterer) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { selectAction(fosterer) },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            GlideImage(
                model = fosterer.imageResId, // 匹配你的 Fosterer.kt
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = fosterer.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                // ✅ 对应图 2：显示距离
                if (distance.isNotEmpty()) {
                    Text(
                        text = distance,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}