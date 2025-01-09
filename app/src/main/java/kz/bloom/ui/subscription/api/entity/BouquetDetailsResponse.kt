package kz.bloom.ui.subscription.api.entity

import kotlinx.serialization.Serializable
import kz.bloom.ui.subscription.order_list.store.BouquetPhoto

@Serializable
data class BouquetDetailsResponse(
    val id: Long,
    val name: String,
    val author: String,
    val bouquetPhotos: List<BouquetPhoto>,
    val branchBouquetInfo: List<BranchBouquetInfo>,
    val bouquetStyle: String,
    val flowerVarietyInfo: List<FlowerVarietyInfo>,
    val additionalElements: List<AdditionalElements>
)

@Serializable
data class FlowerVarietyInfo(
    val id: Long,
    val name: String,
    val image: String?,
    val quantity: Int
)

@Serializable
data class AdditionalElements(
    val id: Long,
    val name: String,
    val quantity: Int,
    val color: String? = null
)

@Serializable
data class BranchBouquetInfo(
    val branchId: Long,
    val divisionType: String,
    val price: Double,
    val address: String,
    val phoneNumber: String,
    val email: String
)
