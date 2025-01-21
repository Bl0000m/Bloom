package kz.bloom.ui.subscription.choose_company.component

import com.arkivanov.decompose.value.Value

interface ChooseCompanyComponent {
    data class Model(
        val companies: List<Company>
    )

    data class Company(
        val companyName: String,
        val price: String,
        val branchId: Long
    )

    val model: Value<Model>

    fun branchPicked(branchId: Long, price: String)

    fun navigateBack()
}