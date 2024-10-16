package kz.bloom.ui.main.content

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kotlinx.coroutines.delay
import kz.bloom.R
import kz.bloom.ui.main.VM.ImageListViewModel
import kz.bloom.ui.main.data.entity.ImageListState
import kz.bloom.ui.main.data.entity.PageItem

@Composable
fun MainContent(vm: ImageListViewModel) {
    val listState: LazyListState = rememberLazyListState()
    val state = vm.state.observeAsState(ImageListState())
    var isLogoWhite by remember { mutableStateOf(false) }
    var isAutoScrollEnabled by remember { mutableStateOf(true) }
    var currentItemIndex by remember { mutableStateOf(0) }

    var isScrolling by remember { mutableStateOf(false) }
    var isAutoScrolling by remember { mutableStateOf(false) }

    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)


    val boxAlpha by animateFloatAsState(
        targetValue = if (isScrolling) 0f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    val pagerAlpha by animateFloatAsState(
        targetValue = if (isScrolling) 0f else 1f,
        animationSpec = tween(durationMillis = 300)
    )

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { scrolling ->
                isScrolling = scrolling
            }
    }

    LaunchedEffect(listState) {
        while (isAutoScrollEnabled) {
            delay(4000L)

            val totalItemsCount = listState.layoutInfo.totalItemsCount

            if (currentItemIndex >= totalItemsCount - 1) {
                isAutoScrollEnabled = false
            } else {
                isAutoScrolling = true
                currentItemIndex++
                listState.animateScrollToItem(currentItemIndex)
                isAutoScrolling = false
            }
        }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (isScrolling && !isAutoScrolling) {
                    isAutoScrollEnabled = false
                }
            }
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.firstVisibleItemScrollOffset }
            .collect { scrollOffset ->
                val firstVisibleItemIndex = listState.firstVisibleItemIndex
                val itemSize = listState.layoutInfo.visibleItemsInfo.firstOrNull()?.size ?: 0
                val isScrolledPastHalfway = scrollOffset >= itemSize / 1.1

                isLogoWhite = if (isScrolledPastHalfway) {
                    state.value.pages.getOrNull(firstVisibleItemIndex + 1)?.isWhite ?: false
                } else {
                    state.value.pages.getOrNull(firstVisibleItemIndex)?.isWhite ?: false
                }
            }
    }
    val pageCount = state.value.pages.size - 2
    val categoryNames = state.value.pages.map { it.categoryName.orEmpty().trim() }
        .filterIndexed { index, name ->
            index != 0 && index != state.value.pages.size - 1 && name.isNotEmpty()
        }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            flingBehavior = snapFlingBehavior
        ) {
            items(state.value.pages) { item ->
                PageItemView(
                    item = item,
                    modifier = Modifier,
                    boxAlpha = boxAlpha,
                    pagerAlpha = pagerAlpha,
                    pageCount = pageCount,
                    categoryNames = categoryNames,
                    isContentWhite = isLogoWhite
                )
            }
        }

        Icon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(horizontal = 41.dp)
                .padding(top = 98.dp),
            painter = painterResource(id = R.drawable.ic_bloom_main),
            tint = if (!isLogoWhite) Color.Black else Color.White,
            contentDescription = null
        )

        BottomNavBar(
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun PageItemView(
    item: PageItem,
    modifier: Modifier,
    boxAlpha: Float,
    pagerAlpha: Float,
    pageCount: Int,
    categoryNames: List<String>,
    isContentWhite: Boolean
) {
    Box(modifier = modifier.fillMaxSize()) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(
                    height = (LocalConfiguration.current.screenHeightDp.dp +
                            getSystemBarsHeight())
                ),
            model = item.imageUrl,
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
        )
        if (item.index != 4) {
            PageContent(
                modifier = Modifier.align(Alignment.BottomEnd),
                boxAlpha = boxAlpha,
                item = item,
                pagerAlpha = pagerAlpha,
                pageCount = pageCount,
                categoryNames = categoryNames,
                isContentWhite = isContentWhite
            )
        } else {
            LastPageContent(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun BottomNavBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 50.dp,
                    bottom = 38.dp,
                    top = 14.dp
                ),
            horizontalArrangement = Arrangement.spacedBy(45.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(size = 24.dp),
                painter = painterResource(id = R.drawable.ic_home),
                tint = Color.Black,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.size(size = 24.dp),
                painter = painterResource(id = R.drawable.ic_search),
                tint = Color.Black,
                contentDescription = null
            )
            Text(
                modifier = Modifier.width(width = 50.dp),
                text = "МЕНЮ",
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
            Icon(
                modifier = Modifier.size(size = 24.dp),
                painter = painterResource(id = R.drawable.ic_bag),
                tint = Color.Black,
                contentDescription = null
            )
            Icon(
                modifier = Modifier.size(size = 24.dp),
                painter = painterResource(id = R.drawable.ic_user),
                tint = Color.Black,
                contentDescription = null
            )
        }
    }
}

@Composable
private fun PageContent(
    modifier: Modifier = Modifier,
    boxAlpha: Float,
    item: PageItem,
    pagerAlpha: Float,
    pageCount: Int,
    categoryNames: List<String>,
    isContentWhite: Boolean
) {
    Box(modifier = modifier.fillMaxHeight()) {
        if (item.index != 4 && item.index != 0) {
            PagerIndicatorContent(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(bottom = 160.dp)
                    .padding(end = 21.dp)
                    .alpha(pagerAlpha)
                    .heightIn(min = 220.dp, max = 300.dp),
                pageCount = pageCount,
                categoryNames = categoryNames,
                item = item,
                isContentWhite = isContentWhite
            )
        }
        SearchButtonContent(
            modifier = Modifier.align(Alignment.BottomCenter),
            boxAlpha = boxAlpha,
            item = item
        )
    }
}

@Composable
private fun LastPageContent(modifier: Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            modifier = modifier.align(Alignment.Center),
            text = "АКТИВИРУЙ СЧАСТЬЕ - ПОЛУЧИ ЦВЕТЫ",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = 195.dp,
                    start = 73.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = "ПОЛИТИКА КОНФИДЕНЦИАЛЬНОСТИ",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp),
                color = Color.Black
            )
            VerticalDivider(
                modifier = Modifier.height(7.dp),
                thickness = 1.dp,
                color = Color.Black,
            )
            Text(
                text = "УСЛОВИЯ ПОКУПКИ",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 8.sp),
                color = Color.Black
            )
        }
    }
}

@Composable
private fun SearchButtonContent(modifier: Modifier = Modifier, boxAlpha: Float, item: PageItem) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 21.dp)
            .padding(bottom = 117.dp)
            .alpha(boxAlpha)
            .border(
                BorderStroke(
                    width = 1.dp,
                    color = if (!item.isWhite) Color.Black else Color.White
                )
            )
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 8.dp),
            text = "ПОИСК",
            color = if (!item.isWhite) Color.Black else Color.White
        )
    }
}

@Composable
private fun PagerIndicatorContent(
    modifier: Modifier = Modifier,
    pageCount: Int,
    categoryNames: List<String>,
    item: PageItem,
    isContentWhite: Boolean
) {
    val categoryName = categoryNames.getOrNull(item.index - 1) ?: ""

    Column(
        modifier = modifier
            .fillMaxHeight(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(
            modifier = Modifier
                .vertical()
                .rotate(-90f),
            text = categoryName,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = if (isContentWhite) Color.White else Color.Black
        )
        VerticalPagerIndicator(
            modifier = Modifier
                .padding(horizontal = 4.dp),
            currentPage = item.index - 1,
            pageCount = pageCount,
            isContentWhite = isContentWhite
        )
    }
}

@Composable
private fun VerticalPagerIndicator(
    modifier: Modifier = Modifier,
    pageCount: Int,
    currentPage: Int,
    isContentWhite: Boolean,
    activeColor: Color = if (isContentWhite) Color.White else Color.Black,
    inactiveColor: Color = Color.Gray,
    selectedIndicatorSize: Dp = 6.dp,
    indicatorSizeOther: Dp = 4.dp,
    indicatorSpacing: Dp = 8.dp
) {
    Column(
        modifier = modifier.wrapContentHeight(),
        verticalArrangement = Arrangement.spacedBy(indicatorSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(pageCount) { page ->
            val isSelected = currentPage == page
            Box(
                modifier = Modifier
                    .size(size = if (isSelected) selectedIndicatorSize else indicatorSizeOther)
                    .background(
                        color = if (isSelected) activeColor else inactiveColor,
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun getSystemBarsHeight(): Dp {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val captionBarHeight = WindowInsets.captionBar.asPaddingValues().calculateBottomPadding()

    val totalBarsHeight = statusBarHeight + navigationBarHeight + captionBarHeight

    return totalBarsHeight
}

fun Modifier.vertical() = layout { measurable, constraints ->
    val placeable = measurable.measure(constraints)
    layout(placeable.height, placeable.width) {
        placeable.place(
            x = -(placeable.width / 2 - placeable.height / 2),
            y = -(placeable.height / 2 - placeable.width / 2)
        )
    }
}
