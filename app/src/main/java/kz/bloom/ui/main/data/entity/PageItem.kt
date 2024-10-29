package kz.bloom.ui.main.data.entity

data class PageItem(
    val imageUrl: String,
    val isWhite: Boolean,
    val index: Int,
    val categoryName: String? = null
)