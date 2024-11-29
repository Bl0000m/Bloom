package kz.bloom.ui.subscription.subs_list.component

import com.arkivanov.decompose.value.Value

interface SubsListComponent {
    data class Model(
        val subsList: List<SubsItem>
    )

    data class SubsItem(
        val data: String
    )

    val model: Value<Model>

    fun createSubscription()

    fun onBackPressed()
}