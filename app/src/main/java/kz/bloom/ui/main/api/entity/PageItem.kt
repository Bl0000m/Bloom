package kz.bloom.ui.main.api.entity

data class PageItem(
    val imageUrl: String,
    val isWhite: Boolean,
    val index: Int,
    val categoryName: String? = null
)