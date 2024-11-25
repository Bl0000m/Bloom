package kz.bloom.ui.main.profile.component

import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

interface ProfileMainComponent {
    data class Model(
        val name: String,
        val coinBalance: Double,
        val profileCategories: List<ProfileCategory>
    )

    public val model: Value<Model>


    @Serializable
    data class ProfileCategory(
        val type: ProfileCategoryType
    )

    fun onCategoryClick(categoryType: ProfileCategoryType)
}