package kz.bloom.ui.main.content

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kz.bloom.R
import kz.bloom.ui.main.VM.ImageListViewModel
import kz.bloom.ui.main.data.entity.ImageListState
import kz.bloom.ui.main.data.entity.ImageItem

@Composable
fun MainContent(vm: ImageListViewModel) {
    val state = vm.state.observeAsState(ImageListState())
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(state.value.images) { item ->
            ImageItemView(item, modifier = Modifier)
        }
    }
}

@Composable
fun ImageItemView(item: ImageItem, modifier: Modifier) {
    Log.d("behold", item.imageUrl)
    Box(
        modifier = modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = LocalConfiguration.current.screenHeightDp.dp),
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
        if (item.index == 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 21.dp)
                    .padding(bottom = 30.dp)
                    .border(BorderStroke(width = 1.dp, color = if (!item.isWhite) Color.Black else Color.White))
                //.clickable {  }
            ) {
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 8.dp),
                    text = "ПОИСК",
                    color = Color.Black
                )
            }
        }
    }
}
