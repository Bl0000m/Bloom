package kz.bloom.ui.main.content

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
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
    Box(modifier = modifier.fillMaxWidth()) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(height = LocalConfiguration.current.screenHeightDp.dp)
            ,
            model = item.imageUrl,
            contentScale = ContentScale.FillBounds,
            contentDescription = null,
        )
        Text(text = "Index: ${item.index}")
    }
}
