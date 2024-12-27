package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto

@Serializable
data class BouquetDetailsResponse(
    val id: Long,
    val name: String,
    val companyName: String,
    val bouquetPhotos: List<BouquetPhoto>,
    val price: Double,
    val bouquetStyle: String,
    val flowerVarietyInfo: List<FlowerVarietyInfo>,
    val additionalElements: List<AdditionalElements>
)

@Serializable
data class FlowerVarietyInfo(
    val id: Long,
    val name: String,
    val image: String,
    val quantity: Int
)

@Serializable
data class AdditionalElements(
    val id: Long,
    val name: String,
    val quantity: Int,
    val color: String
)
