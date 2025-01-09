package kz.bloom.ui.subscription.flower_details.content

import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kz.bloom.R
import kz.bloom.ui.main.home_page.content.VerticalPagerIndicator
import kz.bloom.ui.subscription.api.entity.AdditionalElements
import kz.bloom.ui.subscription.api.entity.BouquetDetailsResponse
import kz.bloom.ui.subscription.api.entity.FlowerVarietyInfo
import kz.bloom.ui.subscription.flower_details.component.FlowerDetailsComponent
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto
import kz.bloom.ui.ui_components.PrimaryButton


@Composable
fun FlowerDetailsContent(modifier: Modifier = Modifier, component: FlowerDetailsComponent) {
    val model = component.model.subscribeAsState()

    CustomBottomSheetDialog(
        bouquetInfo = model.value.bouquetDetails,
        bouquetPhotos = model.value.bouquetPhotos,
        onCloseDetails = { component.closeDetails() },
        onChooseClicked = { component.pickBouquet(it) },
        price = model.value.bouquetPrice
    )
}

@Composable
fun CustomBottomSheetDialog(
    bouquetInfo: BouquetDetailsResponse,
    modifier: Modifier = Modifier,
    bouquetPhotos: List<BouquetPhoto>,
    initialHeight: Dp = 150.dp,
    expandedHeight: Dp = 400.dp,
    onCloseDetails: () -> Unit,
    onChooseClicked:(bouquetId: Long) -> Unit,
    price: String
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    var sheetHeight by remember { mutableStateOf(initialHeight) }
    val dragOffset = remember { Animatable(0f) }
    val listState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.background(color = Color.White)) {
            Column(modifier = Modifier.align(Alignment.TopStart)) {
                Row(modifier = Modifier.padding(vertical = 16.dp, horizontal = 21.dp)) {
                    Icon(
                        modifier = Modifier.clickable { onCloseDetails() },
                        painter = painterResource(id = R.drawable.ic_close_square_light),
                        contentDescription = null
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                        .padding(bottom = sheetHeight),
                    state = listState,
                    flingBehavior = snapFlingBehavior
                ) {
                    items(bouquetPhotos) { photo ->
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .height(height = screenHeight - initialHeight),
                            model = photo.url,
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null
                        )
                    }
                }
            }
            VerticalPagerIndicator(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 170.dp, end = 21.dp),
                pageCount = bouquetPhotos.size,
                currentPage = listState.firstVisibleItemIndex,
                isContentWhite = true
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(sheetHeight)
                .align(Alignment.BottomStart)
                .background(color = Color.White)
                .draggable(
                    orientation = Orientation.Vertical,
                    state = rememberDraggableState { delta ->
                        val newHeight = with(density) { sheetHeight.toPx() } - delta
                        sheetHeight = with(density) {
                            newHeight
                                .coerceIn(initialHeight.toPx(), expandedHeight.toPx())
                                .toDp()
                        }
                    },
                    onDragStopped = {
                        val targetHeight = if (sheetHeight > (initialHeight + expandedHeight) / 2) {
                            expandedHeight
                        } else {
                            initialHeight
                        }
                        dragOffset.animateTo(0f)
                        sheetHeight = targetHeight
                    }
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent)
            ) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 5.dp)
                        .background(color = Color.Transparent),
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween
                ) {
                    Spacer(modifier = Modifier.weight(0.5f))
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(3.dp)
                            .background(Color.Gray, RoundedCornerShape(50))
                    )
                    Spacer(modifier = Modifier.weight(0.5f))
                }
                Column(
                    modifier = Modifier
                        .padding(horizontal = 21.dp)
                        .padding(top = 20.dp)
                        .background(color = Color.Transparent)
                ) {
                    Text(
                        text = bouquetInfo.name.uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "$price BLM".uppercase(),
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    PrimaryButton(
                        text = "ВЫБРАТЬ",
                        onClick = { onChooseClicked(bouquetInfo.id) }
                    )
                    Spacer(modifier = Modifier.height(30.dp))
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            text = "Автор: ${bouquetInfo.author}",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                    Spacer(modifier = Modifier.height(35.dp))
                    Text(
                        text = "СОСТАВ БУКЕТА",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        bouquetInfo.flowerVarietyInfo.forEach { item ->
                            FlowerConsistItem(
                                modifier = Modifier,
                                flowerConsist = item
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(35.dp))
                    Text(
                        text = "ДОПОЛНИТЕЛЬНЫЕ ЭЛЕМЕНТЫ",
                        style = MaterialTheme.typography.labelSmall
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Column {
                        bouquetInfo.additionalElements.forEach { item ->
                            AdditionalItemsItem(additional = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AdditionalItemsItem(modifier: Modifier = Modifier, additional: AdditionalElements) {
    Box(modifier = modifier.border(width = 0.5.dp, color = Color.Black)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = additional.name,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "${additional.quantity} шт.",
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Composable
private fun FlowerConsistItem(modifier: Modifier = Modifier, flowerConsist: FlowerVarietyInfo) {
    Box(modifier = modifier.border(width = 0.5.dp, color = Color.Black)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = flowerConsist.name, style = MaterialTheme.typography.bodySmall)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = "${flowerConsist.quantity} шт.", style = MaterialTheme.typography.bodySmall)
        }
    }
}