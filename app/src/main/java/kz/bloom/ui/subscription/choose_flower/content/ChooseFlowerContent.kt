package kz.bloom.ui.subscription.choose_flower.content

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent
import kz.bloom.ui.subscription.choose_flower.store.ChooseFlowerStore.BouquetDTO

@Composable
fun ChooseFlowerContent(modifier: Modifier = Modifier, component: ChooseFlowerComponent) {
    val model = component.model.subscribeAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
    ) {
        items(model.value.bouquetsInfo) { bouquet ->
            FlowerItemCard(
                bouquetInfo = bouquet,
                onClick = { component.flowerConsidered(it) }
            )
        }
    }
}

@Composable
fun FlowerItemCard(
    bouquetInfo: BouquetDTO,
    onClick:(bouquetDTO: BouquetDTO) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f)
            .clickable { onClick(bouquetInfo) },
        shape = RectangleShape
    ) {
        Column {
            AsyncImage(
                model = bouquetInfo.bouquetPhotos.first().url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = bouquetInfo.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = bouquetInfo.price.toString(),
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}