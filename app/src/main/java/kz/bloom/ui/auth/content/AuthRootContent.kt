package kz.bloom.ui.auth.content

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import kz.bloom.ui.auth.component.AuthRootComponent
import kz.bloom.ui.auth.component.AuthRootComponent.Child
import kz.bloom.ui.auth.confirm.confirm_email.content.CheckForEmailContent
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.content.CheckEmailFillCodeContent
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.content.CreateNewPassContent
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.content.FillEmailContent
import kz.bloom.ui.auth.outcome.content.OutcomePageContent
import kz.bloom.ui.auth.pass_code.content.PassContent
import kz.bloom.ui.auth.sign_in.content.SignInContent
import kz.bloom.ui.auth.sign_up.content.SignUpContent
import kz.bloom.ui.auth.country_chooser.ChooseCountryContent
import kz.bloom.ui.auth.pass_code.user_has_pin_code.content.UserHasPincodeContent


@Composable
fun AuthRootContent(component: AuthRootComponent) {
    Scaffold (
        content = { contentPadding ->
            Children(
                stack = component.childStack,
                animation = stackAnimation(animator = slide())
            ) { child ->
                val contentPaddingModifier = Modifier.padding(paddingValues = contentPadding)

                when (val childInstance = child.instance) {
                    is Child.SignIn -> SignInContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.SignUp -> SignUpContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.ConfirmEmail -> CheckForEmailContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.FillEmail -> FillEmailContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.CheckEmailFillCode -> CheckEmailFillCodeContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.CreateNewPass -> CreateNewPassContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.OutcomePage -> OutcomePageContent(
                        modifier = contentPaddingModifier,
                        component = childInstance.component
                    )
                    is Child.CountryChoose -> ChooseCountryContent(
                        component = childInstance.component,
                        modifier = contentPaddingModifier
                    )
                    is Child.PassCode -> PassContent(
                        component = childInstance.component,
                        modifier = contentPaddingModifier
                    )
                    is Child.UserHasPassCode -> UserHasPincodeContent(
                        component = childInstance.component,
                        modifier = contentPaddingModifier
                    )
                }
            }
        }
    )
}