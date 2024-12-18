package kz.bloom.ui.subscription.choose_flower.content

import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent
import kz.bloom.ui.subscription.choose_flower.component.ChooseFlowerComponent.FlowerItem

@Composable
fun ChooseFlowerContent(modifier: Modifier = Modifier, component: ChooseFlowerComponent) {
    val model = component.model.subscribeAsState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.fillMaxSize(),
    ) {
        
    }
}

@Composable
fun FlowerItemCard(flower: FlowerItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.7f),
        shape = RectangleShape
    ) {
        Column {
            Image(
                painter = painterResource(id = flower.imageRes),
                contentDescription = flower.title,
                modifier = Modifier
                    .fillMaxWidth(),
                contentScale = ContentScale.Crop
            )
            Text(
                text = flower.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = flower.price,
                fontSize = 12.sp,
                color = Color.Gray,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}