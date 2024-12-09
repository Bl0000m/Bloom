package kz.bloom.ui.main.profile.component

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kz.bloom.R
import org.koin.core.component.KoinComponent
import kz.bloom.ui.main.profile.component.ProfileMainComponent.Model
import kz.bloom.ui.main.profile.component.ProfileMainComponent.ProfileCategory
import kz.bloom.ui.ui_components.preference.SharedPreferencesSetting
import org.koin.core.component.inject

class ProfileMainComponentImpl(
    componentContext: ComponentContext,
    private val onOpenSubscriptions: () -> Unit
) : ProfileMainComponent,
    KoinComponent,
    ComponentContext by componentContext {

    private val sharedPreferences by inject<SharedPreferencesSetting>()

    private val _model = MutableValue(
        initialValue = Model(
            name = sharedPreferences.name ?: "Гость",
            coinBalance = 42.5,
            profileCategories = categories
        )
    )
    override val model: Value<Model> = _model

    override fun onCategoryClick(categoryType: ProfileCategoryType) {
        when(categoryType) {
            ProfileCategoryType.MY_ORDERS -> { /* Navigate to My Orders */ }
            ProfileCategoryType.MY_SUBSCRIPTIONS -> { onOpenSubscriptions() }
            ProfileCategoryType.EVENT_CALENDAR -> { /* Handle calendar action */ }
            ProfileCategoryType.PAYMENT_METHOD -> { /* Handle payment method */ }
            ProfileCategoryType.APP_LANGUAGE -> { /* Handle language change */ }
            ProfileCategoryType.NOTIFICATIONS -> { /* Handle notifications */ }
            ProfileCategoryType.INVITE_FRIEND -> { /* Handle invite friend */ }
            ProfileCategoryType.ABOUT_APP -> { /* Handle about app */ }
        }
    }
}
private val categories = ProfileCategoryType.entries.map { type ->
    ProfileCategory(type)
}

enum class ProfileCategoryType(
    @DrawableRes val icon: Int,
    @StringRes val categoryName: Int
) {
    MY_ORDERS(R.drawable.ic_my_orders, R.string.my_orders),
    MY_SUBSCRIPTIONS(R.drawable.ic_my_subs, R.string.my_subscriptions),
    EVENT_CALENDAR(R.drawable.ic_calendar, R.string.event_calendar),
    PAYMENT_METHOD(R.drawable.ic_payment_card, R.string.payment_method),
    APP_LANGUAGE(R.drawable.ic_globe, R.string.app_language),
    NOTIFICATIONS(R.drawable.ic_bell, R.string.notifications),
    INVITE_FRIEND(R.drawable.ic_inter_connect, R.string.invite_friend),
    ABOUT_APP(R.drawable.ic_layers, R.string.about_app)
}