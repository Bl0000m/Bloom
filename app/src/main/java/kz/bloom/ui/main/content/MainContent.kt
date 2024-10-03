package kz.bloom.ui.main.content

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.captionBar
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import kz.bloom.R
import kz.bloom.ui.main.VM.ImageListViewModel
import kz.bloom.ui.main.data.entity.ImageListState
import kz.bloom.ui.main.data.entity.ImageItem

@Composable
fun MainContent(vm: ImageListViewModel) {
    val listState: LazyListState = rememberLazyListState()
    val snapFlingBehavior = rememberSnapFlingBehavior(lazyListState = listState)
    val state = vm.state.observeAsState(ImageListState())
    Column {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = listState,
            flingBehavior = snapFlingBehavior
        ) {
            items(state.value.images) { item ->
                ImageItemView(item, modifier = Modifier)
            }
        }
    }
}

@Composable
fun ImageItemView(item: ImageItem, modifier: Modifier) {
    Log.d("behold", item.imageUrl)
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
        Icon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 98.dp),
            painter = painterResource(id = R.drawable.ic_bloom_main),
            tint = if (!item.isWhite) Color.Black else Color.White,
            contentDescription = null
        )
        if (item.index != 4) {
            Column(modifier = Modifier.align(Alignment.BottomCenter)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 21.dp)
                        .padding(bottom = 30.dp)
                        .border(
                            BorderStroke(
                                width = 1.dp,
                                color = if (!item.isWhite) Color.Black else Color.White
                            )
                        )
                    //.clickable {  }
                ) {
                    Text(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 8.dp),
                        text = "ПОИСК",
                        color = if (!item.isWhite) Color.Black else Color.White
                    )
                }
                if (item.index == 0) {
                    BottomNavBar(modifier = Modifier)
                }
            }
        } else {
            Text(
                modifier = Modifier.align(Alignment.Center),
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
}

@Composable
fun BottomNavBar(modifier: Modifier = Modifier) {
    Surface(modifier = modifier
        .fillMaxWidth()
        .height(76.dp),
        color = Color.White
    ) {
        Row(modifier = Modifier
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
fun LastImageItemView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Icon(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 98.dp),
            painter = painterResource(id = R.drawable.ic_bloom_main),
            tint = Color.Black,
            contentDescription = null
        )
    }
}

@Composable
fun getSystemBarsHeight(): Dp {
    val statusBarHeight = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navigationBarHeight = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
    val captionBarHeight = WindowInsets.captionBar.asPaddingValues().calculateBottomPadding()

    val totalBarsHeight = statusBarHeight + navigationBarHeight + captionBarHeight

    return totalBarsHeight
}
