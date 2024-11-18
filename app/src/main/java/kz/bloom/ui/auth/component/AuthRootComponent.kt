package kz.bloom.ui.auth.component

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kz.bloom.ui.auth.confirm.confirm_email.component.ConfirmEmailComponent
import kz.bloom.ui.auth.confirm.forgot_password.check_email_fill_code.component.CheckEmailFillCodeComponent
import kz.bloom.ui.auth.confirm.forgot_password.creating_new_password.component.CreateNewPasswordComponent
import kz.bloom.ui.auth.confirm.forgot_password.fill_email.component.FillEmailComponent
import kz.bloom.ui.auth.outcome.component.OutcomeComponent
import kz.bloom.ui.auth.pass_code.component.PassCodeComponent
import kz.bloom.ui.auth.sign_in.component.SignInComponent
import kz.bloom.ui.auth.sign_up.component.SignUpComponent
import kz.bloom.ui.auth.country_chooser.component.ChooseCountryComponent
import kz.bloom.ui.auth.pass_code.user_has_pin_code.component.UserHasPincodeComponent

public interface AuthRootComponent {
    public data class Model(
        public val userHasPinAndTokenExpired: Boolean
    )

    public sealed interface Child {
        public data class SignIn(
            public val component: SignInComponent
        ) : Child
        public data class UserHasPassCode(
            public val component: UserHasPincodeComponent
        ) : Child
        public data class SignUp(
            public val component: SignUpComponent
        ) : Child
        public data class ConfirmEmail(
            public val component: ConfirmEmailComponent
        ) : Child
        public data class FillEmail(
            public val component: FillEmailComponent
        ) : Child
        public data class CheckEmailFillCode(
            public val component: CheckEmailFillCodeComponent
        ) : Child
        public data class CreateNewPass(
            public val component: CreateNewPasswordComponent
        ) : Child
        public data class OutcomePage(
            public val component: OutcomeComponent
        ) : Child
        public data class CountryChoose(
            public val component: ChooseCountryComponent
        ) : Child
        public data class PassCode(
            public val component: PassCodeComponent
        ) : Child
    }

    public val model: Value<Model>

    public val childStack: Value<ChildStack<*, Child>>
}